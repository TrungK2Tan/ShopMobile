package com.example.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.model.LoaiSanPham;

import java.util.List;

public class LoaiSanPhamAdapter extends BaseAdapter {
    List<LoaiSanPham> array;
    Context context;

    public LoaiSanPhamAdapter(Context context,List<LoaiSanPham> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView texttensp;
        ImageView hinhsp;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             view = layoutInflater.inflate(R.layout.item_sanpham,null);
             viewHolder.texttensp = view.findViewById(R.id.item_tensp);
             viewHolder.hinhsp = view.findViewById(R.id.item_image);
             view.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder)  view.getTag();

        }
        viewHolder.texttensp.setText(array.get(i).getTensanpham());
        Glide.with(context).load(array.get(i).getHinhanh()).into(viewHolder.hinhsp);
        return view;
    }
}
