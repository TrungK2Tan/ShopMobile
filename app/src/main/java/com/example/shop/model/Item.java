package com.example.shop.model;

public class Item {
    int idsanpham;
    String tensp;
    int soluong;
    String hinhsp;

    public int getIdsanpham() {
        return idsanpham;
    }

    public void setIdsanpham(int idsanpham) {
        this.idsanpham = idsanpham;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhsp;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhsp = hinhanh;
    }
}
