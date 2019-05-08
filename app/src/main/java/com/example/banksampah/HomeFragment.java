package com.example.banksampah;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    MapView mMapView;
    GoogleMap googleMap;
    Marker marker1, marker2, arrayMarker[];
    FloatingActionButton btn_tambah;
    private String geoCode;
    private RecyclerView.Adapter adapter;
    private DatabaseReference database;
    private int lattitude, longitude;
    private RecyclerView rvView;
    private ArrayList<Sampah> sampahArrayList;

    int i = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        btn_tambah = (FloatingActionButton) view.findViewById(R.id.btn_tambah);
        mMapView.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 10000);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();

        rvView = (RecyclerView) view.findViewById(R.id.rv_sampah);
        int jlh_kolom = 2;
//        rvView.setLayoutManager(new GridLayoutManager(getContext(),jlh_kolom));
        rvView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                arrayMarker=new Marker[5];
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    Toast.makeText(getContext(), R.string.error_permission_map, Toast.LENGTH_LONG).show();
                }

                LatLng filkom = new LatLng(-7.953688, 112.61467);
                LatLng fisip = new LatLng(-7.950256 , 112.611998);

                Log.d("filkom",geoCode+"");
                arrayMarker[0] = googleMap.addMarker(new MarkerOptions().position(filkom).title("FILKOM")
                        .snippet("uu"));
                arrayMarker[0].setTag(0);


                arrayMarker[1] = googleMap.addMarker(new MarkerOptions().position(fisip).title("FISIP").snippet("FISIP"));
                arrayMarker[1].setTag(0);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String childMarker = null;
                        for (int i = 0; i< arrayMarker.length; i++){
                        if (marker.equals(arrayMarker[i])) {
                            childMarker = arrayMarker[i].getTitle();
                            for (int a = 0;a<50;a++){
                                if (geoCode == null) {
                                    cekTempatMarker(-7.953688, 112.61467);
                                } else {
                                    arrayMarker[i].setSnippet(geoCode);
                                }
                            }
                        }
                        }
                        final String finalChildMarker = childMarker;
                        btn_tambah.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), Main3Activity.class);
                                intent.putExtra("arrayMarker", finalChildMarker);
                                startActivity(intent);
                            }
                        });
                        database.child("sampah").child(childMarker).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                sampahArrayList = new ArrayList<>();
                                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                                    Sampah post = noteDataSnapshot.getValue(Sampah.class);
                                    post.setId(noteDataSnapshot.getKey());
                                        Log.d("Filkom dalam", geoCode+"");
                                    sampahArrayList.add(post);
                                }

                                adapter = new AdapterSampah(sampahArrayList);

                                rvView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                            }
                        });
                        return false;
                    }
                });
                CameraPosition cameraPosition = new CameraPosition.Builder().target(filkom).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });



      // tampilData();

        return view;

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject weather = new JSONObject(myResponse); //wadah json
                                geoCode = weather.getString("display_name");
                                Log.d("weather", geoCode);
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
