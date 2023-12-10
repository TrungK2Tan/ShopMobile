package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.retrofit.APIShop;
import com.example.shop.retrofit.RetrofitClient;
import com.example.shop.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbarthanhtoan;
    TextView txttongtien,txtemaildathang,txtsdtdathang;
    EditText edtdiachi;
    AppCompatButton btndathang;
    APIShop apiShop;
    long tongtiensp;
    int totalitem;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
         totalitem = 0;
        for(int i=0;i<Utils.gioHangs.size();i++){
            totalitem = totalitem+ Utils.gioHangs.get(i).getSoluong();
        }
    }

    private void initControl(){
        setSupportActionBar(toolbarthanhtoan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarthanhtoan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtiensp = getIntent().getLongExtra("tongtien", 0);
        txttongtien.setText( decimalFormat.format(tongtiensp));
        txtemaildathang.setText(Utils.taiKhoan_current.getEmail());
        txtsdtdathang.setText(Utils.taiKhoan_current.getMobile());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Ban chua nhap dia chi",Toast.LENGTH_SHORT).show();
                }else{
                    //post data
                    String str_email = Utils.taiKhoan_current.getEmail();
                    String str_sodienthoai = Utils.taiKhoan_current.getMobile();
                  int idtaikhoan = Utils.taiKhoan_current.getId();
                    Log.d("test",new Gson().toJson(Utils.gioHangs));
                    compositeDisposable.add(apiShop.createOrder(str_email,str_sodienthoai,String.valueOf(tongtiensp),idtaikhoan,str_diachi,totalitem,new Gson().toJson(Utils.gioHangs))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    taiKhoanModel -> {
                                        Toast.makeText(getApplicationContext(),"Thanh Cong",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }
    private void initView(){
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(APIShop.class);
        toolbarthanhtoan = findViewById(R.id.toolbarthanhtoan);
        txttongtien = findViewById(R.id.txttongtien);
        txtemaildathang = findViewById(R.id.txtemaildathang);
        txtsdtdathang = findViewById(R.id.txtsdtdathang);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}