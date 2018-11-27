package com.example.paul.flashship.Class;

public class InfoLichSu {

    int idHangHoa;
    String tenHang;
    String tenNhan;
    String sdtNhan;
    String sdtTaiKhoan;

    public InfoLichSu(int idHangHoa, String tenHang, String tenNhan, String sdtNhan, String sdtTaiKhoan) {
        this.idHangHoa = idHangHoa;
        this.tenHang = tenHang;
        this.tenNhan = tenNhan;
        this.sdtNhan = sdtNhan;
        this.sdtTaiKhoan = sdtTaiKhoan;
    }

    public InfoLichSu() {
    }

    public int getIdHangHoa() {
        return idHangHoa;
    }

    public void setIdHangHoa(int idHangHoa) {
        this.idHangHoa = idHangHoa;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getTenNhan() {
        return tenNhan;
    }

    public void setTenNhan(String tenNhan) {
        this.tenNhan = tenNhan;
    }

    public String getSdtNhan() {
        return sdtNhan;
    }

    public void setSdtNhan(String sdtNhan) {
        this.sdtNhan = sdtNhan;
    }

    public String getSdtTaiKhoan() {
        return sdtTaiKhoan;
    }

    public void setSdtTaiKhoan(String sdtTaiKhoan) {
        this.sdtTaiKhoan = sdtTaiKhoan;
    }
}
