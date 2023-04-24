package com.pmo.intermedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    public CardView card1,card2,card3,card4,card5,card6;
    private TextView textJam,textTanggal;
    public Button logout;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        card1 = (CardView) findViewById((R.id.cv1));
        card2 = (CardView) findViewById((R.id.cv2));
        card3 = (CardView) findViewById((R.id.cv3));
        card4 = (CardView) findViewById((R.id.cv4));
        card5 = (CardView) findViewById((R.id.cv5));
        card6 = (CardView) findViewById((R.id.cv6));

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                signOutUser();
            }
        });

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);

        textJam = findViewById(R.id.jam);
        textTanggal = findViewById(R.id.tanggal);

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    /*Toast.makeText(MainActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();*/
                    Intent intent = new Intent(MainActivity.this, LoginUtama.class);
                    startActivity(intent);
                    finish();
                }
            }
        };


        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                @SuppressLint("SimpleDateFormat")
                DateFormat clockFormat = new SimpleDateFormat("HH:mm:ss");

                @SuppressLint("SimpleDateFormat")
                DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");

                textJam.setText(clockFormat.format(new Date()));
                textTanggal.setText(dateFormat.format(new Date()));

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void signOutUser() {
        Intent MainActivity = new Intent(MainActivity.this, com.pmo.intermedia.MainActivity.class);
        Toast.makeText(MainActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
        MainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        finish();
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                AlertDialog.Builder alertt = new AlertDialog.Builder(this);
                alertt.setTitle("Anda yakin ingin keluar?");
                alertt.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { System.exit(1);}
                })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { return;}
                        });
                alertt.create();
                alertt.show();
                auth.signOut();
                break;
        }
        return true;
    }*/

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.cv1:
                i = new Intent(this, Pengurus.class);
                startActivity(i);
                break;
            case R.id.cv2:
                i = new Intent(this, Presensi.class);
                startActivity(i);
                break;
            case R.id.cv3:
                i = new Intent(this, Modul.class);
                startActivity(i);
                break;
            case R.id.cv4:
                i = new Intent(this, Kegiatan.class);
                startActivity(i);
                break;
            case R.id.cv5:
                i = new Intent(this, Site.class);
                startActivity(i);
                break;
            case R.id.cv6:
                i = new Intent(this, Info.class);
                startActivity(i);
                break;
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }
}