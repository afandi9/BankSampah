package com.example.banksampah;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSampah extends RecyclerView.Adapter<AdapterSampah.ViewHolder> {
    private ArrayList<Sampah> dataList;
    private Context mContext;

    public AdapterSampah(ArrayList<Sampah> data)
    {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewJenis, textViewHarga;
        ImageView imageViewGambar;




        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewJenis = (TextView) itemView.findViewById(R.id.tv_jenis);
            this.textViewHarga = (TextView) itemView.findViewById(R.id.tv_harga);
            this.imageViewGambar = (ImageView) itemView.findViewById(R.id.img_gambar);
        }
    }

    @Override
    public AdapterSampah.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sampah, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterSampah.ViewHolder holder, final int position)
    {
//
//        Intent intent = new Intent(mContext, imageSampah.class);
//        intent.putExtra("image_url", dataList.get(position).getJenis_sampah());
//        intent.putExtra("image_name", dataList.get(position).getFoto_sampah());
//        mContext.startActivity(intent);
        holder.textViewJenis.setText(dataList.get(position).getJenis_sampah());
        holder.textViewHarga.setText(dataList.get(position).getHarga_sampah());
        Picasso.get().load(dataList.get(position).getFoto_sampah()).into(holder.imageViewGambar);

    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }
}
