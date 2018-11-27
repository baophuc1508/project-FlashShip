package com.example.paul.flashship;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoadInfo extends AppCompatActivity implements Download {
    SharedPreferences share;
    String sdt;
    int chucvu;
    TextView txtHoTenInfo,txtSdtInfo,txtChucVuInfo;
    ImageView imageAvatar;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_info);

        share = getSharedPreferences("sharePre",MODE_PRIVATE);

        sdt = share.getString("SDT","");
        chucvu = share.getInt("CHUCVU",0);

        init();

        String linkhinh = "http://baophuc.000webhostapp.com/flashship/images/"+sdt+".jpg";
        Picasso.get().load(linkhinh).into(imageAvatar);
        if(chucvu == 1){
            link = "http://baophuc.000webhostapp.com/flashship/loadinfostore.php?sdt=" + sdt;
        }else {
            link = "http://baophuc.000webhostapp.com/flashship/loadinfoshipper.php?sdt=" + sdt;
        }

        txtSdtInfo.setText("Số Điện Thoại: " + sdt);

        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();

        String data = "";
        try {
            data = downloadData.get();
            Log.d("LLL",data.toString());
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("thongtin");

            for (int i=0; i < jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String ten = "Họ và Tên: " + jsonObject1.getString("hoten");
                String infoChucVu = "";
                if (chucvu == 1){
                    infoChucVu = "Địa Chỉ: " + jsonObject1.getString("diachi");
                }else {
                    infoChucVu ="Biển Số: " +  jsonObject1.getString("bienso");
                }

                txtHoTenInfo.setText(ten);
                txtChucVuInfo.setText(infoChucVu);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        txtHoTenInfo = findViewById(R.id.txtHoTenInfo);
        txtSdtInfo = findViewById(R.id.txtSdtInfo);
        txtChucVuInfo = findViewById(R.id.txtChucVuInfo);
        imageAvatar = findViewById(R.id.imageAvatar);
    }

    @Override
    public Void Download(String data) {
        return null;
    }
}
