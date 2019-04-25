package com.example.banksampah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText et_u_email, et_u_pass;
    Button btn_login;
    TextView btn_daftar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        btn_daftar = (TextView) findViewById(R.id.btn_dftr);
        btn_login = (Button) findViewById(R.id.btn_masuk);
        et_u_email = (EditText) findViewById(R.id.et_username);
        et_u_pass = (EditText) findViewById(R.id.et_password);


        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainDaftar.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }


    private void loginUser() {
        String email = et_u_email.getText().toString().trim();
        String password = et_u_pass.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkIfEmailVerified();
                        Intent intent = new Intent(MainActivity.this, main.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


        private void checkIfEmailVerified(){
            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                Toast.makeText(MainActivity.this, "Login berhasil", Toast.LENGTH_SHORT);
                //getDataUser();
            } else {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "E-mail belum tereverifikasi, silahkan cek email Anda", Toast.LENGTH_SHORT).show();
            }
        }
    }

