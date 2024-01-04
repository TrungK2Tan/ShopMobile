package com.example.shop.utils;

import com.example.shop.model.GioHang;
import com.example.shop.model.TaiKhoan;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://172.16.16.110/banhang/";
    public static List<GioHang> gioHangs;
    public static List<GioHang> muahangne = new ArrayList<>();
    public static TaiKhoan taiKhoan_current = new TaiKhoan();
}
