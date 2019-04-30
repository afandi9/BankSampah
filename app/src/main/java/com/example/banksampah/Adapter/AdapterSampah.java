package com.example.banksampah.Adapter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banksampah.Model.Sampah;
import com.example.banksampah.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSampah extends RecyclerView.Adapter<AdapterSampah.ViewHolder> {
    private ArrayList<Sampah> dataList;

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
        holder.textViewJenis.setText(dataList.get(position).getJenis_sampah());
        holder.textViewHarga.setText(dataList.get(position).getHarga_sampah());
        Picasso.get().load(dataList.get(position).getFoto_sampah()).into(holder.imageViewGambar);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }
}
