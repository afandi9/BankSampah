package com.example.banksampah;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sampahClick extends AppCompatActivity {

    String idSampah = null,namaSampah = "FILKOM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sampah_click);

        getIncomingIntent();

        Button jualSampah = findViewById(R.id.jual_sampah);
        jualSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idSampah = getIntent().getStringExtra("id_sampah");
                namaSampah = getIntent().getStringExtra("parent_name");
                hapusAkun(idSampah);
            }
        });
    }

    private void hapusAkun(String idSampah){
        DatabaseReference hapusAkun = FirebaseDatabase.getInstance().getReference("sampah");

        Log.d("data sudah dihapus", idSampah+" "+namaSampah);
//        hapusAkun.child(namaSampah).child(idSampah).removeValue();
        if (hapusAkun.child(idSampah).child(getIntent().getStringExtra("id_sampah"))==null){
            Log.d("data sudah dihapus", "yey");
        }
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
