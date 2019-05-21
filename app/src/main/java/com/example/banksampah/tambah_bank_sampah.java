package com.example.banksampah;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class tambah_bank_sampah extends AppCompatActivity {

    private String geoCode;
    private LocationManager locationManager;
    private LocationListener listener;
    private TextInputEditText editText_tambah_bank;
    private Button button_tambah_bank;
    public Double locLat, locLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_bank_sampah);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText_tambah_bank = findViewById(R.id.editText_tambah_bank);
        button_tambah_bank = findViewById(R.id.button_tambah_bank);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locLat = location.getLongitude();
                locLong = location.getLongitude();
                Intent i = new Intent(tambah_bank_sampah.this, HomeFragment.class);
                cekTempatMarker(locLat, locLong);
                Log.d("locaLat", locLat+"");
                editText_tambah_bank.append(location.getLatitude()+"");
                i.putExtra("lattitude", locLat);
                i.putExtra("longitude", locLong);

}

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        button_tambah_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                if (ActivityCompat.checkSelfPermission(tambah_bank_sampah.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(tambah_bank_sampah.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }int i=0;
                Log.d(""+i++, "masih jalan");
                locationManager.requestLocationUpdates("gps", 0, 0, listener);
            }
        });
    }

    public void cekTempatMarker(Double lattitude, Double longitude){
        OkHttpClient client = new OkHttpClient();
        String urlGeo = "https://us1.locationiq.com/v1/reverse.php?key=0e78c0c461a3de&lat="+lattitude
                +"&lon="+longitude+"&format=json";

        Request requestGeo = new Request.Builder()
                .url(urlGeo)
                .build();

        client.newCall(requestGeo).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponse = response.body().string();
                    tambah_bank_sampah.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject weather = new JSONObject(myResponse); //wadah json
                                geoCode = weather.getString("display_name");
                                Log.d("ini geoCode", geoCode);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                geoCode = "sistem tidak mendukung ";
                            }
                        }
                    });
                }
            }
        });
    }


}










