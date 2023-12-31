package com.example.shop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop.Interface.IImageClickListener;
import com.example.shop.R;
import com.example.shop.model.EventBus.TinhTongEventBus;
import com.example.shop.model.GioHang;
import com.example.shop.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang giohang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(giohang.getTensp());
        holder.item_giohang_soluong.setText(giohang.getSoluong()+"");
        Glide.with(context).load(giohang.getHinhsp()).into(holder.item_giohang_img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimalFormat.format(giohang.getGiasp()));
        long gia = giohang.getSoluong()*giohang.getGiasp();
        holder.item_giohang_giasp.setText(decimalFormat.format(gia));
        holder.checksanpham.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                     Utils.muahangne.add(giohang);
                    EventBus.getDefault().postSticky(new TinhTongEventBus());
                }else{
                    for(int i=0;i<Utils.muahangne.size();i++){
                        if(Utils.muahangne.get(i).getIdsp() == giohang.getIdsp()){
                            Utils.muahangne.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEventBus());
                        }
                    }
                }
            }
        });
        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG","onImageClick:"+pos+"..."+giatri);
                if(giatri ==1){
                    if(gioHangList.get(pos).getSoluong()>1){
                        int soluongmoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                        long gia = gioHangList.get(pos).getSoluong()*gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEventBus());
                    } else if(gioHangList.get(pos).getSoluong() ==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.gioHangs.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEventBus());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (giatri ==2) {
                    if(gioHangList.get(pos).getSoluong()<11){
                        int soluongmoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                    long gia = gioHangList.get(pos).getSoluong()*gioHangList.get(pos).getGiasp();
                    holder.item_giohang_giasp.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEventBus());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_img,img_tru,img_cong;
        TextView item_giohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_giasp;
        IImageClickListener listener;
        CheckBox checksanpham;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            item_giohang_img = itemView.findViewById(R.id.item_giohang_img);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp = itemView.findViewById(R.id.item_giohang_giasp);
            img_tru = itemView.findViewById(R.id.item_giohang_tru);
            img_cong = itemView.findViewById(R.id.item_giohang_cong);
            checksanpham = itemView.findViewById(R.id.item_giohang_check);
            //event click
            img_tru.setOnClickListener(this);
            img_cong.setOnClickListener(this);
        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if(view == img_tru){
                listener.onImageClick(view,getAdapterPosition(),1);
            }else if(view == img_cong){
                listener.onImageClick(view,getAdapterPosition(),2);
            }

        }
    }
}
