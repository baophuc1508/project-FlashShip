package com.example.paul.flashship.Dowload;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImage extends AsyncTask <String,Void,String> {

    String path,sdt,link;
    Context context;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
            .build();



    public UploadImage (String path, String sdt, String link, Context context) {
        this.path = path;
        this.sdt = sdt;
        this.link = link;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        File file = new File(path);
        String content_type = getType(file.getPath());
        String file_path = file.getAbsolutePath();
        Log.d("AAAA", link);
        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),file);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("uploaded_image",sdt+".jpg",file_body)
                .setType(MultipartBody.FORM)
                .build();

        Request request = new Request.Builder()
                .url(link)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        super.onPostExecute(s);
    }

    private String getType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
