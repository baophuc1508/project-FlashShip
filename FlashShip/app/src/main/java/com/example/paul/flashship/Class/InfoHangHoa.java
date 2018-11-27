package com.example.paul.flashship.Class;

public class InfoHangHoa {

    int id;
    String tenHangHoa;
    String sdtGui;
    String tenNguoiNhan;
    String sdtNhan;
    Double quangDuongDi;
    Double chiPhi;

    public InfoHangHoa(int id, String tenHangHoa, String sdtGui, String tenNguoiNhan, String sdtNhan, Double quangDuongDi, Double chiPhi) {
        this.id = id;
        this.tenHangHoa = tenHangHoa;
        this.sdtGui = sdtGui;
        this.tenNguoiNhan = tenNguoiNhan;
        this.sdtNhan = sdtNhan;
        this.quangDuongDi = quangDuongDi;
        this.chiPhi = chiPhi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenHangHoa() {
        return tenHangHoa;
    }

    public void setTenHangHoa(String tenHangHoa) {
        this.tenHangHoa = tenHangHoa;
    }

    public String getSdtGui() {
        return sdtGui;
    }

    public void setSdtGui(String sdtGui) {
        this.sdtGui = sdtGui;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getSdtNhan() {
        return sdtNhan;
    }

    public void setSdtNhan(String sdtNhan) {
        this.sdtNhan = sdtNhan;
    }

    public Double getQuangDuongDi() {
        return quangDuongDi;
    }

    public void setQuangDuongDi(Double quangDuongDi) {
        this.quangDuongDi = quangDuongDi;
    }

    public Double getChiPhi() {
        return chiPhi;
    }

    public void setChiPhi(Double chiPhi) {
        this.chiPhi = chiPhi;
    }
}
