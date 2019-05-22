package com.example.banksampah;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AdapterSampah extends RecyclerView.Adapter<AdapterSampah.ViewHolder> {
    private ArrayList<Sampah> dataList;
    private Context mContext;

    public AdapterSampah(Context context, ArrayList<Sampah> data)
    {
        this.mContext = context;
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewJenis, textViewHarga;
        ImageView imageViewGambar;
        LinearLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewJenis = (TextView) itemView.findViewById(R.id.tv_jenis);
            this.textViewHarga = (TextView) itemView.findViewById(R.id.tv_harga);
            this.imageViewGambar = (ImageView) itemView.findViewById(R.id.img_gambar);
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
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
        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + dataList.get(position));

                Toast.makeText(mContext, ""+dataList.get(position).getJenis_sampah(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, sampahClick.class);
                intent.putExtra("jenis_sampah", dataList.get(position).getJenis_sampah());
                intent.putExtra("harga_sampah", dataList.get(position).getHarga_sampah());
                intent.putExtra("berat_sampah", dataList.get(position).getKuantitas_sampah());
                intent.putExtra("gambar_sampah", dataList.get(position).getFoto_sampah());
                intent.putExtra("id_sampah", dataList.get(position).getId());


                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }
}
