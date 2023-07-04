package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.adapter.LoaiSanPhamAdapter;
import com.example.shop.model.LoaiSPModel;
import com.example.shop.model.LoaiSanPham;
import com.example.shop.retrofit.APIShop;
import com.example.shop.retrofit.RetrofitClient;
import com.example.shop.utils.Utils;
import com.google.android.material.navigation.NavigationView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(APIShop.class);
        Anhxa();
        ActionBar();

        if(isConnected(this)){
            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            GetLoaiSanPham();
        }else{
            Toast.makeText(getApplicationContext(),"Not Internet",Toast.LENGTH_LONG).show();
        }
    }

    private void GetLoaiSanPham() {
    compositeDisposable.add(apiShop.getLoaiSp()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    loaiSPModel -> {
                        if(loaiSPModel.isSuccess()){
//                            Toast.makeText(getApplicationContext(),loaiSPModel.getResult().get(0).getTensanpham(),Toast.LENGTH_LONG).show();
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
        navigationView = findViewById(R.id.navigationview);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);
        //khoi tao list
        sanPhams = new ArrayList<>();

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