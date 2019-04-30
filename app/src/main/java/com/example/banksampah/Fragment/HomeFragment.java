package com.example.banksampah.Fragment;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.banksampah.Adapter.AdapterSampah;
import com.example.banksampah.Model.Sampah;
import com.example.banksampah.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class HomeFragment extends Fragment{
    MapView mMapView;
    GoogleMap googleMap;
    Marker marker1;
    FloatingActionButton btn_tambah;
    private RecyclerView.Adapter adapter;

    private static final int PICK_IMAGE_REQUEST = 234;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference db;
    private Uri filePath;
    private RecyclerView rvView;
    private ArrayList<Sampah> sampahArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        btn_tambah = (FloatingActionButton) view.findViewById(R.id.btn_tambah);
        mMapView.onCreate(savedInstanceState);

        //rvView = (RecyclerView) view.findViewById(R.id.rv_sampah);
        int jlh_kolom = 2;
//        rvView.setLayoutManager(new GridLayoutManager(getContext(),jlh_kolom));


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference("sampah");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://banksampah-33c3b.appspot.com");


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

                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    Toast.makeText(getContext(), R.string.error_permission_map, Toast.LENGTH_LONG).show();
                }
                LatLng sydney = new LatLng(-34, 151);


                marker1 = googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker 1").snippet("Marker Description"));
                marker1.setTag(0);



                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                btn_tambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.tambah_sampah);
                        dialog.show();

                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(marker1)){
                                    Toast.makeText(getContext(),"Marker 1",Toast.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        });

                        ImageButton pilih = dialog.findViewById(R.id.btn_img);
                        pilih.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chooseImage();
                            }
                        });
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading..");
                        progressDialog.show();

                        final EditText editTextJenis =dialog.findViewById(R.id.et_jenis);
                        final EditText editTextHarga =dialog.findViewById(R.id.et_harga);

                        final StorageReference ref = storageReference.child("sampah/"+ UUID.randomUUID().toString());
                        ref.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri downloadUrl = uri;

                                                //String artikel = db.push().getKey();
                                                Sampah post = new Sampah(editTextJenis.getText().toString(), editTextHarga.getText().toString(),downloadUrl.toString());
                                                db.child(marker1.getTitle()).push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        editTextJenis.setText("");
                                                        editTextHarga.setText("");
                                                        Toast.makeText(getContext(),"Uploaded 1",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                        Toast.makeText(getContext(),"Uploaded 2",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(),"Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage("Uploaded.."+(int)progress+"%");
                                    }
                                });
                    }
                });


            }
        });
        return view;

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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
