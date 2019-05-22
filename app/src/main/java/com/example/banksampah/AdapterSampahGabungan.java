package com.example.banksampah;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSampahGabungan extends RecyclerView.Adapter<AdapterSampahGabungan.ViewHolder> {
    private ArrayList<Sampah> dataList;

    public AdapterSampahGabungan(ArrayList<Sampah> data)
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
    public AdapterSampahGabungan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sampah, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterSampahGabungan.ViewHolder holder, final int position)
    {
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
