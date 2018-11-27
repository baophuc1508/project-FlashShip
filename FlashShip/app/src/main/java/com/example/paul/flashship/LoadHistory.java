package com.example.paul.flashship;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.flashship.Adapter.AdapterHistory;
import com.example.paul.flashship.Class.InfoLichSu;
import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoadHistory extends AppCompatActivity implements Download {
    SharedPreferences share;
    String sdt;
    int chucvu;
    String link;
    List<InfoLichSu> lichSuList;
    AdapterHistory adapterHistory;
    RecyclerView recyclerHis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_history);

        share = getSharedPreferences("sharePre",MODE_PRIVATE);

        sdt = share.getString("SDT","");
        chucvu = share.getInt("CHUCVU",0);

        lichSuList = new ArrayList<>();
        init();

        if(chucvu == 1){
            link = "http://baophuc.000webhostapp.com/flashship/lichsustore.php?sdtstore=" + sdt;
        }else {
            link = "http://baophuc.000webhostapp.com/flashship/lichsushipper.php?sdtshipper=" + sdt;
        }

        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();

        String data = "";
        try {
            data = downloadData.get();

            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("chitiet");

            for (int i=0; i < jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                int id = jsonObject1.getInt("idhanghoa");
                String ten =jsonObject1.getString("tenhanghoa");
                String tennhan =jsonObject1.getString("tennguoinhan");
                String sdtnhan =jsonObject1.getString("sdtnguoinhan");
                String sdtHis = "";
                if(chucvu == 1){
                    sdtHis = jsonObject1.getString("sdtshipper");
                }else {
                    sdtHis = jsonObject1.getString("sdtstore");
                }

                InfoLichSu infoLichSu = new InfoLichSu(id,ten,tennhan,sdtnhan,sdtHis);

                lichSuList.add(infoLichSu);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerHis.setLayoutManager(layoutManager);

        adapterHistory = new AdapterHistory(this,lichSuList,chucvu);
        recyclerHis.setAdapter(adapterHistory);
    }

    private void init() {
        recyclerHis = findViewById(R.id.recyclerHis);
    }


    @Override
    public Void Download(String data) {
        return null;
    }
}
