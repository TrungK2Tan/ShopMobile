package com.example.shop.retrofit;

import com.example.shop.model.DonHangModel;
import com.example.shop.model.LoaiSPModel;
import com.example.shop.model.SanPhamMoiModel;
import com.example.shop.model.TaiKhoanModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIShop {
    //GET DATA
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();

    //POST DATA
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
      @Field("page") int page,
      @Field("loai") int loai
    );
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> DangKy(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> DangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("resetpass.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> resetPass(
            @Field("email") String email
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel> createOrder(
            @Field("email") String email,
            @Field("sodienthoai") String sodienthoai,
            @Field("tongtien") String tongtien,
            @Field("idtaikhoan") int idtaikhoan,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("idtaikhoan") int id
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
}
