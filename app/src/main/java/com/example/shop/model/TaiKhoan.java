package com.example.shop.model;

public class TaiKhoan {
    int id_taikhoan;
    String email;
    String pass;
    String username;
    String mobile;

    public int getId() {
        return id_taikhoan;
    }

    public void setId(int id) {
        this.id_taikhoan = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
