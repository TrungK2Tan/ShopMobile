package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.ULocale;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.model.GioHang;
import com.example.shop.model.SanPhamMoi;
import com.example.shop.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView txttensp,txtgiasp,mota;
    ImageView imghinhanh;
    Spinner spinner;
    Button btnthem;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Utils.gioHangs.size()>0){
        boolean flag = false;
        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
        for(int i=0;i<Utils.gioHangs.size();i++){
            if(Utils.gioHangs.get(i).getIdsp() == sanPhamMoi.getId_sanphammoi()){
                Utils.gioHangs.get(i).setSoluong(soluong+ Utils.gioHangs.get(i).getSoluong());
                long gia = Long.parseLong(sanPhamMoi.getGiasp())+Utils.gioHangs.get(i).getSoluong();
                Utils.gioHangs.get(i).setGiasp(gia);
                flag = true;
            }
        }
        if(flag == false){
            long gia = Long.parseLong(sanPhamMoi.getGiasp())*soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setHinhsp(sanPhamMoi.getHinhsp());
            gioHang.setIdsp(sanPhamMoi.getId_sanphammoi());
            gioHang.setTensp(sanPhamMoi.getTensp());
            Utils.gioHangs.add(gioHang);
        }

        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp())*soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setHinhsp(sanPhamMoi.getHinhsp());
            gioHang.setIdsp(sanPhamMoi.getId_sanphammoi());
            gioHang.setTensp(sanPhamMoi.getTensp());
            Utils.gioHangs.add(gioHang);
        }
        badge.setText(String.valueOf(Utils.gioHangs.size()));
    }

    private void initData(){
         sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        txttensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhsp()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtgiasp.setText("Giá:"+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }
    private void initView(){
        txttensp = findViewById(R.id.txttensanpham);
        txtgiasp = findViewById(R.id.txtgiasanpham);
        mota= findViewById(R.id.txtmotachitiet);
        imghinhanh =findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        badge = findViewById(R.id.menu_sl);
        if(Utils.gioHangs != null){
            badge.setText(String.valueOf(Utils.gioHangs.size()));
        }
    }
    private void ActionToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}