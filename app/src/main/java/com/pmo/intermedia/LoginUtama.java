package com.pmo.intermedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginUtama extends AppCompatActivity {

    private EditText et_email2, et_password2;
    private Button register2, login2;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener Listener;
    private String getEmail,getPassword;
    private ProgressBar pbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_utama);



        et_email2 = findViewById(R.id.et_email2);
        et_password2 = findViewById(R.id.et_password2);
        login2 = findViewById(R.id.login2);
        register2 = findViewById(R.id.register2);
        pbar2 = findViewById(R.id.pbar2);
        pbar2.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null && user.isEmailVerified()){
                    startActivity(new Intent(LoginUtama.this, MainActivity.class));
                    finish();
                }
            }
        };
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar2.setVisibility(View.VISIBLE);
                getEmail = et_email2.getText().toString();
                getPassword = et_password2.getText().toString();

                if(TextUtils.isEmpty(getEmail)||TextUtils.isEmpty(getPassword)){
                    Toast.makeText(LoginUtama.this, "Email atau sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    LoginUserAccount();
                }
            }
        });
        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUtama.this, Register.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(Listener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(Listener != null){
            auth.removeAuthStateListener(Listener);
        }
    }
    private void LoginUserAccount(){
        auth.signInWithEmailAndPassword(getEmail,getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pbar2.setVisibility(View.GONE);
                            if(auth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginUtama.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginUtama.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginUtama.this);
                                alert.setTitle("Periksa email anda untuk verifikasi!");
                                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        return;
                                    }
                                });
                                alert.create();
                                alert.show();
                            }
                        }else{
                            pbar2.setVisibility(View.GONE);
                            Toast.makeText(LoginUtama.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}