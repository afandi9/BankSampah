package com.example.banksampah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainDaftar extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup, buttonSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String email,password;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_daftar);

        editTextEmail = (EditText) findViewById(R.id.et_email);
        editTextPassword = (EditText) findViewById(R.id.et_pass);

        buttonSignup = (Button) findViewById(R.id.btn_daftar);

        progressDialog = new ProgressDialog(MainDaftar.this);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if(TextUtils.isEmpty(email)){

                    Toast.makeText(MainDaftar.this,"Please enter email", Toast.LENGTH_LONG).show();

                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainDaftar.this,"Please enter password",Toast.LENGTH_LONG).show();

                }
                else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(email,password);
                }
            }
        });

    }


    private void registerUser(String email,String password){
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainDaftar.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //display some message here
                            sendVerif();
                            Toast.makeText(MainDaftar.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainDaftar.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            //display some message here
                            Toast.makeText(MainDaftar.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }

                });

    }

    private void sendVerif(){
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainDaftar.this,"Terkirim",Toast.LENGTH_LONG).show();
            }

        });

    }
}




