package com.example.paul.flashship.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.flashship.Class.InfoHangHoa;
import com.example.paul.flashship.DirectionsOnMap;
import com.example.paul.flashship.Dowload.Download;
import com.example.paul.flashship.Dowload.DownloadData;
import com.example.paul.flashship.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterHangHoa extends RecyclerView.Adapter<AdapterHangHoa.HangHoaHolder> implements Download {
    Context context;
    List<InfoHangHoa> hangHoas;
    TextView txtTenHang,txtSdtNguoiGui,txtTenNguoiNhan,txtSdtNguoiNhan,txtQuangDuong,txtChiPhi;
    Button btnHuyHang,btnNhanHang;
    String sdtShipper;

    public AdapterHangHoa(Context context, List<InfoHangHoa> hangHoas, String sdtShiper) {
        this.context = context;
        this.hangHoas = hangHoas;
        this.sdtShipper = sdtShiper;
    }

    @Override
    public AdapterHangHoa.HangHoaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.item_hanghoa,parent,false);
        HangHoaHolder hangHoaHolder = new HangHoaHolder(itemView);
        return  hangHoaHolder;
    }

    @Override
    public void onBindViewHolder(AdapterHangHoa.HangHoaHolder holder, int position) {
        final InfoHangHoa infoHangHoa = hangHoas.get(position);
        holder.txtTen.setText("Tên Hàng: " + infoHangHoa.getTenHangHoa());
        final NumberFormat format = new DecimalFormat("0.#");

        holder.txtKhoangCach.setText("Quãng Đường: " + format.format(infoHangHoa.getQuangDuongDi()) + " mét");

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.dialog_infohanghoa,null);
                builder.setView(view1);

                initDialog(view1);

                txtTenHang.setText("Tên Hàng: " + infoHangHoa.getTenHangHoa());
                txtSdtNguoiGui.setText("SĐT Người Gửi: " + infoHangHoa.getSdtGui());
                txtTenNguoiNhan.setText("Tên Người Nhận: " + infoHangHoa.getTenNguoiNhan());
                txtSdtNguoiNhan.setText("SĐT Người Nhận: " + infoHangHoa.getSdtNhan());
                txtQuangDuong.setText("Quãng Đường: " + format.format(infoHangHoa.getQuangDuongDi()) + " mét");
                txtChiPhi.setText("Chi Phí: " + format.format(infoHangHoa.getChiPhi()) + " VND");
                final AlertDialog dialog = builder.create();
                btnHuyHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                btnNhanHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        XacNhan(infoHangHoa.getId());
                        dialog.cancel();
                    }
                });


                dialog.show();
            }
        });

    }

    private void XacNhan(int id) {
        String link = "http://baophuc.000webhostapp.com/flashship/updateNhanHang.php?sdtshipper="+ sdtShipper +"&idhanghoa=" + id;
        DownloadData downloadData = new DownloadData(this,link);
        downloadData.execute();
        Intent intent = new Intent(context,DirectionsOnMap.class);
        Bundle bundle = new Bundle();
        bundle.putInt("hanghoaid",id);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    private void initDialog(View view1) {
        txtTenHang = view1.findViewById(R.id.txtTenHang);
        txtSdtNguoiGui = view1.findViewById(R.id.txtSdtNguoiGui);
        txtTenNguoiNhan = view1.findViewById(R.id.txtTenNguoiNhan);
        txtSdtNguoiNhan = view1.findViewById(R.id.txtSdtNguoiNhan);
        txtQuangDuong = view1.findViewById(R.id.txtQuangDuong);
        txtChiPhi = view1.findViewById(R.id.txtChiPhi);
        btnHuyHang = view1.findViewById(R.id.btnHuyHang);
        btnNhanHang = view1.findViewById(R.id.btnNhanHang);
    }

    @Override
    public int getItemCount() {
        return hangHoas.size();
    }

    @Override
    public Void Download(String data) {
        return null;
    }

    public class HangHoaHolder extends RecyclerView.ViewHolder {
        TextView txtTen,txtGia,txtKhoangCach;
        LinearLayout item;
        public HangHoaHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemHangHoa);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtKhoangCach = itemView.findViewById(R.id.txtKhoangCach);
        }
    }
}
