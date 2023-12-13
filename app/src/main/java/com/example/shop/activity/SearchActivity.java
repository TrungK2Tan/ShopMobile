package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.adapter.DienThoaiAdapter;
import com.example.shop.model.SanPhamMoi;
import com.example.shop.retrofit.APIShop;
import com.example.shop.retrofit.RetrofitClient;
import com.example.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar_search;
    RecyclerView recycleview_search;
    EditText editsearch;
    DienThoaiAdapter dienThoaiAdapter;
    List<SanPhamMoi> sanPhamMoiList;
    APIShop apiShop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolBar();
    }

    private void initView() {
        sanPhamMoiList = new ArrayList<>();
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(APIShop.class);
        toolbar_search= findViewById(R.id.toolbarsearch);
        recycleview_search = findViewById(R.id.recycleview_search);
        editsearch = findViewById(R.id.editsearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleview_search.setHasFixedSize(true);
        recycleview_search.setLayoutManager(layoutManager);
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    sanPhamMoiList.clear();
                    dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                    recycleview_search.setAdapter(dienThoaiAdapter);
                }
                else{
                    getDataSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();
        compositeDisposable.add(apiShop.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                                recycleview_search.setAdapter(dienThoaiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void ActionToolBar(){
        setSupportActionBar(toolbar_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_search.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}