package com.example.shop.model;

import java.util.List;

public class SanPhamMoiModel {
    boolean success;
    String messange;
    List<SanPhamMoi> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessange() {
        return messange;
    }

    public void setMessange(String messange) {
        this.messange = messange;
    }

    public List<SanPhamMoi> getResult() {
        return result;
    }

    public void setResult(List<SanPhamMoi> result) {
        this.result = result;
    }
}
