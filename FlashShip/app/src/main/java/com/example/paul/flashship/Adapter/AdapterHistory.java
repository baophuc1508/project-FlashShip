package com.example.paul.flashship.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paul.flashship.Class.InfoHangHoa;
import com.example.paul.flashship.Class.InfoLichSu;
import com.example.paul.flashship.R;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryHolder>{
    Context context;
    List<InfoLichSu> lichSus;

    String tenHang,tenNhan,sdtNhan,sdtHis;
    int chucVu;

    public AdapterHistory(Context context, List<InfoLichSu> lichSus, int chucVu) {
        this.context = context;
        this.lichSus = lichSus;
        this.chucVu = chucVu;
    }

    @Override
    public AdapterHistory.HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.item_lichsu,viewGroup,false);
        HistoryHolder historyHolder = new HistoryHolder(itemView);
        return historyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.HistoryHolder historyHolder, int i) {
        InfoLichSu infoLichSu = lichSus.get(i);

        tenHang = "Tên Hàng: " + infoLichSu.getTenHang().toString();
        tenNhan = "Tên Người Nhận: " + infoLichSu.getTenNhan().toString();
        sdtNhan = "Số Điện Thoại Người Nhận: " + infoLichSu.getSdtNhan().toString();
        if(chucVu == 1){
            sdtHis = "Số Điện Thoại Shipper: " + infoLichSu.getSdtTaiKhoan().toString();
        }else {
            sdtHis = "Số Điện Thoại Cửa Hàng: " + infoLichSu.getSdtTaiKhoan().toString();
        }

        historyHolder.txtTenHangHis.setText(tenHang);
        historyHolder.txtTenNhanHis.setText(tenNhan);
        historyHolder.txtSdtNhanHis.setText(sdtNhan);
        historyHolder.txtSdtHis.setText(sdtHis);

    }

    @Override
    public int getItemCount() {
        return lichSus.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView txtTenHangHis,txtTenNhanHis,txtSdtNhanHis,txtSdtHis ;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            txtTenHangHis = itemView.findViewById(R.id.txtTenHangHis);
            txtTenNhanHis = itemView.findViewById(R.id.txtTenNhanHis);
            txtSdtNhanHis = itemView.findViewById(R.id.txtSdtNhanHis);
            txtSdtHis = itemView.findViewById(R.id.txtSdtHis);
        }
    }
}
