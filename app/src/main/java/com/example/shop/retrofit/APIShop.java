package com.example.shop.retrofit;

import com.example.shop.model.LoaiSPModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface APIShop {
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();
}
