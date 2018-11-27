package com.example.paul.flashship.Dowload;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadData extends AsyncTask<String,Void,String>{
    Download download;
    List<HashMap<String,String>> listData;
    String link;
    boolean kiemtra = true;


    public DownloadData(Download download, List<HashMap<String, String>> listData, String link) {
        this.download = download;
        this.listData = listData;
        this.link = link;
        kiemtra = false;
    }

    public DownloadData(Download download, String link) {
        this.download = download;
        this.link = link;
        kiemtra = true;
    }

    @Override
    protected String doInBackground(String... strings) {
        String dulieu = "";
        if(!kiemtra){
            dulieu = methodPost(link);
        }else {
            dulieu = methodGet(link);
        }
        return dulieu;
    }

    private String methodPost(String link) {
        StringBuilder stringBuilder = new StringBuilder();
        String key = "";
        String value = "";
        String data = "";

        try {
            URL url = new URL(this.link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            Uri.Builder builder = new Uri.Builder();
            int count = listData.size();
            for (int i = 0; i < count;i++){
                for (Map.Entry<String,String> values : listData.get(i).entrySet()){
                    key = values.getKey();
                    value = values.getValue();
                }
                builder.appendQueryParameter(key,value);
            }
            String query = builder.build().getEncodedQuery();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            writer.write(query);
            writer.flush();
            writer.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private String methodGet(String link) {
        String data = "";

        try {
            URL url = new URL(this.link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }

            data = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        download.Download(s);
    }
}
