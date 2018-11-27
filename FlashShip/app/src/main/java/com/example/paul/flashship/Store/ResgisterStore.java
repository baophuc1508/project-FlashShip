package com.example.paul.flashship.Store;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.example.paul.flashship.Dowload.UploadImage;
import com.example.paul.flashship.Login;
import com.example.paul.flashship.R;
import com.example.paul.flashship.Shipper.ResgisterShipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ResgisterStore extends AppCompatActivity implements Download{

    EditText txtHoTenStore,txtSdtStore,txtMKhauStore,txtAgainStore,txtDiaChiStore;
    ImageView imageAvatarStore;
    Button btnResgisterStore;
    int REQUEST_CODE_FOLDER = 1;
    String path;
    String hoTen,sDT,mKhau,mKhauAgain,diaChi;
    int check = 0;
    List<HashMap<String,String>> listInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister_store);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        PickImage();
        ClickResgister();
    }

    private void ClickResgister() {
        btnResgisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hoTen = txtHoTenStore.getText().toString();
                sDT = txtSdtStore.getText().toString();
                mKhau = txtMKhauStore.getText().toString();
                mKhauAgain = txtAgainStore.getText().toString();
                diaChi = txtDiaChiStore.getText().toString();

                xulitaikhoan();

                checkSDT();



                if(check ==2 ){

                    if(mKhau.length() > 5 && mKhau.equals(mKhauAgain)){

                        uploadTaiKhoan();

                        //String link1 = "http://ebook.xsolution.vn/flashship/uploadimage.php";
                        String link2 = "http://baophuc.000webhostapp.com/flashship/uploadimage.php";
                        UploadImage uploadImage = new UploadImage(path,sDT,link2,ResgisterStore.this);
                        uploadImage.execute();

                        Toast.makeText(ResgisterStore.this, "Tài Khoản Đã Được Tạo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResgisterStore.this, Login.class);
                        startActivity(intent);
                        finish();


                    }else {
                        Toast.makeText(ResgisterStore.this, "Mật Khẩu Không Đủ 6 Kí Tự hoặc Không Trùng Nhau", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ResgisterStore.this, "tai khoan da co", Toast.LENGTH_SHORT).show();
                }





            }
        });
    }

    private void uploadTaiKhoan() {
        String link1 = "http://baophuc.000webhostapp.com/flashship/insertTaiKhoan2.php";
        DownloadData downloadData1 = new DownloadData(this,listInfo,link1);
        downloadData1.execute();
    }

    private void xulitaikhoan() {
        listInfo = new ArrayList<>();
        HashMap<String,String> hoten = new HashMap<>();
        hoten.put("hoten",hoTen);

        HashMap<String,String> sdt = new HashMap<>();
        sdt.put("sdt",sDT);

        HashMap<String,String> mkhau = new HashMap<>();
        mkhau.put("mkhau",mKhau);

        HashMap<String,String> diachi = new HashMap<>();
        diachi.put("diachi",diaChi);

        HashMap<String,String> chucvu = new HashMap<>();
        chucvu.put("chucvu","1");

        listInfo.add(hoten);
        listInfo.add(sdt);
        listInfo.add(mkhau);
        listInfo.add(diachi);
        listInfo.add(chucvu);

    }

    private void checkSDT() {
        String link = "http://baophuc.000webhostapp.com/flashship/kiemtraSDT.php?sdt=" + sDT;

        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();

        String data="";

        try {
            data = downloadData.get();
            JSONObject jsonObject = new JSONObject(data);
            check = jsonObject.getInt("ketqua");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void PickImage() {
        imageAvatarStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_FOLDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            path = getRealPathFromURI(uri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                imageAvatarStore.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void init() {
        txtHoTenStore = findViewById(R.id.txtHoTenStore);
        txtSdtStore = findViewById(R.id.txtSdtStore);
        txtMKhauStore = findViewById(R.id.txtMKhauStore);
        txtAgainStore = findViewById(R.id.txtAgainStore);
        txtDiaChiStore = findViewById(R.id.txtDiaChiStore);
        imageAvatarStore = findViewById(R.id.imageAvatarStore);
        btnResgisterStore = findViewById(R.id.btnResgisterStore);
    }

    @Override
    public Void Download(String data) {
        return null;
    }
}
