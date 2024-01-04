package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.retrofit.APIShop;
import com.example.shop.retrofit.RetrofitClient;
import com.example.shop.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangkitk,txtresetpass;
    EditText email,pass;
    AppCompatButton  btndangnhap;
    APIShop apiShop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangkitk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(intent);
            }
        });
        txtresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }else{
                    //save
                    Paper.book().write("email",str_email);
                    Paper.book().write("pass",str_pass);
                    dangnhap(str_email,str_pass);
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(APIShop.class);
        txtdangkitk = findViewById(R.id.txtdangkitk);
        email = findViewById(R.id.emaildangnhap);
        pass = findViewById(R.id.matkhaudangnhap);
        btndangnhap = findViewById(R.id.btndangnhap);
        txtresetpass = findViewById(R.id.txtresetpass);
        //read data
        if(Paper.book().read("email") !=null && Paper.book().read("pass")!=null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            if(Paper.book().read("isLogin")!=null){
                boolean flag = Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dangnhap(Paper.book().read("email"),Paper.book().read("pass"));
                        }
                    },1000);
                }
            }
        }

    }

    private void dangnhap(String str_email, String str_pass) {
        compositeDisposable.add(apiShop.DangNhap(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taiKhoanModel -> {

                            if(taiKhoanModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin",isLogin);
                                Utils.taiKhoan_current = taiKhoanModel.getResult().get(0);
                                //luu thong tin nguoi dung
                                Paper.book().write("user",taiKhoanModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(Utils.taiKhoan_current.getEmail() !=null && Utils.taiKhoan_current.getPass()!=null){
            email.setText(Utils.taiKhoan_current.getEmail());
            pass.setText(Utils.taiKhoan_current.getPass());
        }
    }
    @Override
    protected  void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}