package com.example.banksampah;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.VisibilityAwareImageButton;
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
    FloatingActionButton tambah_bank;
    FloatingActionButton btn_tambah;

    private String geoCode, parent_name;
    private RecyclerView.Adapter adapter;
    private DatabaseReference database;
    private Double lattitude[], longitude[];
    double lattitudeBank, longitudeBank;
    private RecyclerView rvView;
    private ArrayList<Sampah> sampahArrayList;
    LatLng filkom, fisip;


    int i = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        btn_tambah =  view.findViewById(R.id.btn_tambah);
        tambah_bank = view.findViewById(R.id.tambah_bank);



        mMapView.onCreate(savedInstanceState);

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
                tempat();

                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    //Toast.makeText(getContext(), R.string.error_permission_map, Toast.LENGTH_LONG).show();
                }


                arrayMarker[0] = googleMap.addMarker(new MarkerOptions().position(filkom).title("FILKOM")
                        .snippet("FILKOM"));
                arrayMarker[0].setTag(0);


                arrayMarker[1] = googleMap.addMarker(new MarkerOptions().position(fisip).title("Polinema").snippet("Polinema"));
                arrayMarker[1].setTag(0);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String childMarker = null;
                        for (int i = 0; i< 5; i++){
                        if (marker.equals(arrayMarker[i])) {
                            Log.d("marker lewat ", arrayMarker[i]+"");
                            childMarker = arrayMarker[i].getTitle();
                            for (int a = 0;a<50;a++){
                                Log.d("ini a"+a,"ini i"+i);
                                if (geoCode == null) {
                                    cekTempatMarker(lattitude[i], longitude[i]);

                                } else {
                                    cekTempatMarker(lattitude[i], longitude[i]);
                                    arrayMarker[i].setSnippet(geoCode);
                                    break;
                                    }
                            }
                        }
                        }
                        final String finalChildMarker = childMarker;
                        parent_name = childMarker;
                        Intent intent = new Intent(getContext(), Main3Activity.class);
                        intent.putExtra("parent_name", parent_name);
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
                                    sampahArrayList.add(post);

                                }

                                adapter = new AdapterSampah(getContext(), sampahArrayList);

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


        tambah_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), tambah_bank_sampah.class);
                getContext().startActivity(intent);
                Bundle extras;
                if (savedInstanceState == null)
                {
//fetching extra data passed with intents in a Bundle type variable
                    extras = getActivity().getIntent().getExtras();
                    if(extras == null)
                    {        longitudeBank= 0;
                              lattitudeBank= 0;
                    }
                    else

                    {            /* fetching the string passed with intent using ‘extras’*/

                        longitudeBank= extras.getDouble("longitude");
                        lattitudeBank= extras.getDouble("lattitude");
                        Log.d(longitudeBank+" ini longitude",lattitudeBank+" ini lattitude");

                    }

                }

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

    public void tempat(){
        lattitude=new Double[5];
        longitude=new Double[5];
        filkom = new LatLng(-7.953688, 112.61467);
        fisip = new LatLng(-7.946049 , 112.615671);
        lattitude[0]=-7.953688; longitude[0]=112.61467;
        lattitude[1]=-7.946049; longitude[1]=112.615671;

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
