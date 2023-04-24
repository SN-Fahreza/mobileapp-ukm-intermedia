package com.pmo.intermedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Activity {
    private EditText et_email, et_password;
    private Button register,login;
    private ProgressBar pbar;

    private FirebaseAuth auth;
    private String getEmail,getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        pbar = findViewById(R.id.pbar);
        pbar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                cekDataUser();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginUtama.class);
                startActivity(intent);
            }
        });
    }
    private void cekDataUser(){
        getEmail = et_email.getText().toString();
        getPassword = et_password.getText().toString();

        if(TextUtils.isEmpty(getEmail)|| TextUtils.isEmpty(getPassword)){
            Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else {
            if (getPassword.length()<6){
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            }else{
                createUserAccount();
            }
        }
    }
    private void createUserAccount(){
        auth.createUserWithEmailAndPassword(getEmail,getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        Pengguna user = new Pengguna(getEmail,getPassword);
                        FirebaseDatabase.getInstance().getReference("Pengguna")
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    pbar.setVisibility(View.GONE);
                                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this, "Register Berhasil, please check your email for verification", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Register.this,LoginUtama.class));
                                                finish();
                                            }else{
                                                Toast.makeText(Register.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    pbar.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pbar.setVisibility(View.GONE);
                                Toast.makeText(Register.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.setVisibility(View.GONE);
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}