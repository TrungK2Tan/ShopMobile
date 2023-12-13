package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.adapter.LoaiSanPhamAdapter;
import com.example.shop.adapter.SanPhamMoiAdapter;
import com.example.shop.model.LoaiSPModel;
import com.example.shop.model.LoaiSanPham;
import com.example.shop.model.SanPhamMoi;
import com.example.shop.model.SanPhamMoiModel;
import com.example.shop.retrofit.APIShop;
import com.example.shop.retrofit.RetrofitClient;
import com.example.shop.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSanPhamAdapter loaiSanPhamAdapter;
    List<LoaiSanPham> sanPhams;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIShop apiShop;
    List<SanPhamMoi> sanPhamMois;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(APIShop.class);
        Anhxa();
        ActionBar();

        if(isConnected(this)){
//            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            GetLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(),"Not Internet",Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;

                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiShop.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                sanPhamMois = sanPhamMoiModel.getResult();
                                sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(),sanPhamMois);
                                recyclerView.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Not Internet"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void GetLoaiSanPham() {
    compositeDisposable.add(apiShop.getLoaiSp()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    loaiSPModel -> {
                        if(loaiSPModel.isSuccess()){
// câu lệnh này để hiển thị ra màn hình  Toast.makeText(getApplicationContext(),loaiSPModel.getResult().get(0).getTensanpham(),Toast.LENGTH_LONG).show();
                        sanPhams = loaiSPModel.getResult();
                            // khoi  tap adapter
                            loaiSanPhamAdapter = new LoaiSanPhamAdapter(getApplicationContext(),sanPhams);
                            listViewManHinhChinh.setAdapter(loaiSanPhamAdapter);
                        }
                    }
            )
    );

    }

    public void ActionViewFlipper(){
        List<String> banner = new ArrayList<>();
        banner.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        banner.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        banner.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i =0;i<banner.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(banner.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    protected  void Anhxa(){
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper= findViewById(R.id.viewlipper);
        recyclerView = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        imgsearch = findViewById(R.id.imgsearch);
        //khoi tao list
        sanPhams = new ArrayList<>();
        sanPhamMois = new ArrayList<>();
        if(Utils.gioHangs == null){
            Utils.gioHangs = new ArrayList<>();
        }else{
            int totalitem = 0;
            for(int i=0;i<Utils.gioHangs.size();i++){
                totalitem = totalitem+ Utils.gioHangs.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalitem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

    }
    protected  void onResume(){
        super.onResume();
        int totalitem = 0;
        for(int i=0;i<Utils.gioHangs.size();i++){
            totalitem = totalitem+ Utils.gioHangs.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalitem));
    }
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// nho them quyen lai khong bi loi
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected())||(mobile != null && mobile.isConnected())){
            return  true;
        }else {
            return  false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}