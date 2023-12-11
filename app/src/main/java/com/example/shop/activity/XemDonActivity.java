package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.adapter.DonHangAdapter;
import com.example.shop.retrofit.APIShop;
import com.example.shop.retrofit.RetrofitClient;
import com.example.shop.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIShop apiShop;
    RecyclerView recyclerViewdonhang;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();

    }

    private void getOrder() {

        compositeDisposable.add(apiShop.xemDonHang(Utils.taiKhoan_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            recyclerViewdonhang.setAdapter(adapter);
                        },
                        throwable -> {

                        }
                ));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    private void initView() {
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(APIShop.class);
        recyclerViewdonhang = findViewById(R.id.recycleview_donhang);
        toolbar = findViewById(R.id.toolbardonhang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewdonhang.setLayoutManager(layoutManager);
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}