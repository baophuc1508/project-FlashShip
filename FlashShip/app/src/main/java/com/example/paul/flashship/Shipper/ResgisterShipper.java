package com.example.paul.flashship.Shipper;

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
import com.example.paul.flashship.Store.ResgisterStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ResgisterShipper extends AppCompatActivity implements Download {

    EditText txtHoTenShipper,txtSdtShipper,txtMKhauShipper,txtAgainShipper,txtBienSoShipper;
    ImageView imageAvatarShipper;
    Button btnResgisterShipper;
    int REQUEST_CODE_FOLDER = 1;
    String path;
    String hoTen,sDT,mKhau,mKhauAgain,bienSo;
    int check = 0;
    List<HashMap<String,String>> listInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister_shipper);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        PickImage();
        ClickResgister();

    }

    private void ClickResgister() {
        btnResgisterShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hoTen = txtHoTenShipper.getText().toString();
                sDT = txtSdtShipper.getText().toString();
                mKhau = txtMKhauShipper.getText().toString();
                mKhauAgain = txtAgainShipper.getText().toString();
                bienSo = txtBienSoShipper.getText().toString();

                xulitaikhoan();

                checkSDT();



                if(check ==2 ){

                    if(mKhau.length() > 5 && mKhau.equals(mKhauAgain)){

                        uploadTaiKhoan();

                        //String link1 = "http://ebook.xsolution.vn/flashship/uploadimage.php";
                        String link2 = "http://baophuc.000webhostapp.com/flashship/uploadimage.php";
                        UploadImage uploadImage = new UploadImage(path,sDT,link2,ResgisterShipper.this);
                        uploadImage.execute();

                        Toast.makeText(ResgisterShipper.this, "Tài Khoản Đã Được Tạo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResgisterShipper.this, Login.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(ResgisterShipper.this, "Mật Khẩu Không Đủ 6 Kí Tự hoặc Không Trùng Nhau", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ResgisterShipper.this, "tai khoan da co", Toast.LENGTH_SHORT).show();
                }





            }
        });
    }

    private void uploadTaiKhoan() {
        String link1 = "http://baophuc.000webhostapp.com/flashship/insertTaiKhoan.php";
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

        HashMap<String,String> bienso = new HashMap<>();
        bienso.put("bienso",bienSo);

        HashMap<String,String> chucvu = new HashMap<>();
        chucvu.put("chucvu","2");

        listInfo.add(hoten);
        listInfo.add(sdt);
        listInfo.add(mkhau);
        listInfo.add(bienso);
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
        imageAvatarShipper.setOnClickListener(new View.OnClickListener() {
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

                imageAvatarShipper.setImageBitmap(bitmap);
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
        txtHoTenShipper = findViewById(R.id.txtHoTenShipper);
        txtSdtShipper = findViewById(R.id.txtSdtShipper);
        txtMKhauShipper = findViewById(R.id.txtMKhauShipper);
        txtAgainShipper = findViewById(R.id.txtAgainShipper);
        txtBienSoShipper = findViewById(R.id.txtBienSoShipper);
        imageAvatarShipper = findViewById(R.id.imageAvatarShipper);
        btnResgisterShipper = findViewById(R.id.btnResgisterShipper);
    }

    @Override
    public Void Download(String data) {
        return null;
    }
}
