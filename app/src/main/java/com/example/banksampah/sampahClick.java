package com.example.banksampah;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class sampahClick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sampah_click);


        getIncomingIntent();
    }

    private void getIncomingIntent(){
//        Log.d("coba", "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("jenis_sampah") && getIntent().hasExtra("harga_sampah")&&
        getIntent().hasExtra("berat_sampah") && getIntent().hasExtra("gambar_sampah")){
//            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String jenisIntent = getIntent().getStringExtra("jenis_sampah");
            String hargaIntent = getIntent().getStringExtra("harga_sampah");
            String beratIntent = getIntent().getStringExtra("harga_sampah");
            String gambarIntent = getIntent().getStringExtra("gambar_sampah");

            TextView jenis = findViewById(R.id.jenis_sampah);
            TextView harga = findViewById(R.id.harga_sampah);
            TextView berat = findViewById(R.id.berat_sampah);
            jenis.setText(jenisIntent);
            harga.setText(hargaIntent);
            berat.setText(beratIntent);
            setImage(gambarIntent);
        }
    }
    private void setImage(String gambarIntent){
//        Log.d(TAG, "setImage: setting te image and name to widgets.");

        ImageView image = findViewById(R.id.gambar_sampah);
        Glide.with(this)
                .asBitmap()
                .load(gambarIntent)
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(image);
    }

}
