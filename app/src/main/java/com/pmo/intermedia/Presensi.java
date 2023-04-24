package com.pmo.intermedia;

import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;


public class Presensi extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText NIM,Nama,Tanggal,Saran,NoHp;
    private ImageView Image;
    private Button Simpan,LihatData,Upload, Iklan;
    private RewardedAd mRewardedAd;
    private Spinner Pertemuan;
    private RadioButton Rb1,Rb2,Rb3;
    private String getNIM,getNama,getDivisi,getPertemuan,getTanggal,getSaran,getNoHP,getImage;

    final int kodeGallery = 100, kodeKamera = 99;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri fotoUrl;

    //Koneksi Firebase
    DatabaseReference getReference;

    //Calendar
    EditText mtgl;
    DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presensi);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        Simpan = findViewById(R.id.btn_simpan);
        LihatData = findViewById(R.id.btn_data);
        Iklan = findViewById(R.id.lihat_iklan);

        NIM = findViewById(R.id.nim);
        Nama = findViewById(R.id.nama);
        Saran = findViewById(R.id.saran);
        NoHp = findViewById(R.id.nohp);
        Tanggal = findViewById(R.id.tanggal);
        Rb1 = findViewById(R.id.rb1);
        Rb2 = findViewById(R.id.rb2);
        Rb3 = findViewById(R.id.rb3);
        Pertemuan = findViewById(R.id.spinner_pert);
        Upload = findViewById(R.id.uploadfoto);
        Image = findViewById(R.id.lihatfoto);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        rewAds();

        Iklan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRewardedAd != null) {
                    Activity activityContext = Presensi.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();

                            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {

                                    rewAds();
                                    mRewardedAd = null;
                                    super.onAdDismissedFullScreenContent();
                                }
                            });

                        }
                    });
                } else {

                }

            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentUpload, kodeGallery);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        getReference = database.getReference();

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference filePath = mStorage.getReference().child("imagePost").child(fotoUrl.getLastPathSegment());
                filePath.putFile(fotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                getNIM = NIM.getText().toString();
                getNama = Nama.getText().toString();
                getPertemuan = Pertemuan.getSelectedItem().toString();
                getTanggal = Tanggal.getText().toString();
                getSaran = Saran.getText().toString();
                getNoHP = NoHp.getText().toString();
                getImage = task.getResult().toString();
                progressBar.setVisibility(View.VISIBLE);

                checkDivisi();
                clear();

                            }
                        });
                    }
                });





            }
        });
        LihatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Presensi.this, ListDataActivity.class);
                startActivity(intent);

            }
        });


        //Calendar
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mtgl = findViewById(R.id.tanggal);
        mtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Presensi.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                mtgl.setText(date);

            }
        };

    }

    public void rewAds(){

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, getString(R.string.rewarded_ads),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kodeGallery && resultCode == RESULT_OK) {
            fotoUrl = data.getData();
            Image.setImageURI(fotoUrl);
        }
    }
    private void clear(){
        NIM.setText("");
        Nama.setText("");
        Tanggal.setText("");
        Saran.setText("");
        NoHp.setText("");
    }
    private void checkDivisi(){
        if (Rb1.isChecked()){
            getDivisi = Rb1.getText().toString();
            checkUser();
        }else if(Rb2.isChecked()){
            getDivisi = Rb2.getText().toString();
            checkUser();
        }else if(Rb3.isChecked()){
            getDivisi = Rb3.getText().toString();
            checkUser();
        }else{
            Toast.makeText(Presensi.this, "Masukan Divisi", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
    private void checkUser(){
        if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getDivisi) || isEmpty(getPertemuan) ||
                isEmpty(getTanggal) || isEmpty(getSaran) || isEmpty(getNoHP)){
            Toast.makeText( Presensi.this,"Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
        }else{
            getReference.child("Admin").child("Mahasiswa").push()
                    .setValue(new Mahasiswa(getNIM,getNama,getDivisi,getPertemuan,getTanggal,getSaran,getNoHP,getImage))
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            NIM.setText("");
                            Nama.setText("");
                            Tanggal.setText("");
                            Saran.setText("");
                            NoHp.setText("");
                            Toast.makeText(Presensi.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}