package com.example.paul.flashship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.example.paul.flashship.Shipper.Shipper;
import com.example.paul.flashship.Store.Store;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity implements Download{

    EditText txtSdt,txtMKhau;
    CheckBox cbSave;
    SharedPreferences share;
    Button btnLogin,btnResgister;
    List<HashMap<String,String>> listNguoiDung;
    String lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        share = getSharedPreferences("sharePre",MODE_PRIVATE);

         lat = share.getString("Latitude","");
         lng = share.getString("Longitude", "");

        Toast.makeText(this, lat+ "   " + lng, Toast.LENGTH_SHORT).show();

        init();
        LoadTaiKhoan();
        btnResgister();
        btnLogin();

    }

    private void btnLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sdt = txtSdt.getText().toString();
                String mkhau= txtMKhau.getText().toString();
                XulyLogin(sdt,mkhau);
            }
        });
    }

    private void XulyLogin(String sDT, String mKhau) {

        String link = "http://baophuc.000webhostapp.com/flashship/dangnhap.php";
        listNguoiDung = new ArrayList<>();
        HashMap<String,String> sdt = new HashMap<>();
        sdt.put("sdt",sDT);
        HashMap<String,String> mkhau = new HashMap<>();
        mkhau.put("mkhau",mKhau);

        listNguoiDung.add(sdt);
        listNguoiDung.add(mkhau);

        DownloadData downloadData = new DownloadData(this,listNguoiDung,link);
        downloadData.execute();

        String data = "";
        int check = 5;
        int chucvu = 5;

        try {
            data = downloadData.get();
            Log.d("AAAA",data);
            JSONObject jsonObject = new JSONObject(data);
            check = jsonObject.getInt("ketqua");
            chucvu = jsonObject.getInt("chucvu");


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, check+ "    " +chucvu, Toast.LENGTH_SHORT).show();

        if(check == 1){
            String duongdan = "http://baophuc.000webhostapp.com/flashship/uploadToaDo.php?latitude=" + lat +"&longitude=" + lng + "&sdt=" + sDT;
            DownloadData update = new DownloadData(this,duongdan);
            update.execute();

            if(cbSave.isChecked()){
                SharedPreferences.Editor editor = share.edit();
                editor.putString("SDT",sDT);
                editor.putString("MATKHAU",mKhau);
                editor.putBoolean("CHECK",cbSave.isChecked());
                editor.commit();
                ThanhCong(chucvu);
            }else {

                ThanhCong(chucvu);
            }
        }else {
            Toast.makeText(getApplicationContext(),"Sai Số Điện Thoại hoặc Sai Mật Khẩu",Toast.LENGTH_SHORT).show();
        }

    }

    private void ThanhCong(int chucvu) {
        Intent intent;
        SharedPreferences.Editor editor = share.edit();
        editor.putInt("CHUCVU",chucvu);
        editor.commit();
        if (chucvu == 1){
            intent = new Intent(Login.this,Store.class);

        }else {
            intent = new Intent(Login.this,Shipper.class);
        }
        startActivity(intent);
        finish();
    }

    private void LoadTaiKhoan() {
        boolean kiemtra = share.getBoolean("CHECK",false);

        if(kiemtra){
            String sdt = share.getString("SDT","");
            String matkhau = share.getString("MATKHAU", "");
            txtSdt.setText(sdt);
            txtMKhau.setText(matkhau);
            cbSave.setChecked(true);
        }else {
            cbSave.setChecked(false);
        }
    }

    private void btnResgister() {
        btnResgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Resgister.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        txtSdt = findViewById(R.id.txtSdt);
        txtMKhau = findViewById(R.id.txtMKhau);
        cbSave = findViewById(R.id.cbSave);
        btnLogin = findViewById(R.id.btnLogin);
        btnResgister = findViewById(R.id.btnResgister);
    }

    @Override
    public Void Download(String data) {
        return null;
    }
}
