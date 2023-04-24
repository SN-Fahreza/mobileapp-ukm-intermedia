package com.pmo.intermedia;

import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateActivity extends AppCompatActivity {
    private EditText nimBaru,namaBaru,tanggalBaru,saranBaru,nohpBaru;
    private Spinner pertBaru;
    private ImageView fotoBaru;
    private RadioGroup radioGroup;
    private RadioButton Rb1,Rb2,Rb3;
    private Button update,uploadBaru;
    private DatabaseReference database;
    private String cekNIM,cekNama,cekDivisi,cekPert,cekTanggal,cekSaran,cekNoHp,cekImage;
    private static final int SELECT_IMAGE = 100;
    final int kodeGallery = 100, kodeKamera = 99;

    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri fotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        FirebaseDatabase databasebaru = FirebaseDatabase.getInstance();
        FirebaseStorage mStorage = FirebaseStorage.getInstance();


        nimBaru = findViewById(R.id.new_nim);
        namaBaru = findViewById(R.id.new_nama);
        Rb1 = findViewById(R.id.new_rb1);
        Rb2 = findViewById(R.id.new_rb2);
        Rb3 = findViewById(R.id.new_rb3);
        pertBaru = findViewById(R.id.new_spinner_pert);
        tanggalBaru = findViewById(R.id.new_tanggal);
        saranBaru = findViewById(R.id.new_saran);
        nohpBaru = findViewById(R.id.new_nohp);
        update = findViewById(R.id.btn_update);
        uploadBaru = findViewById(R.id.newuploadfoto);
        fotoBaru = findViewById(R.id.newlihat_foto);
        database = FirebaseDatabase.getInstance().getReference();

        getData();
        uploadBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, SELECT_IMAGE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference filePath = mStorage.getReference().child("imagePost").child(fotoUrl.getLastPathSegment());
                filePath.putFile(fotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                cekNIM = nimBaru.getText().toString();
                cekNama = namaBaru.getText().toString();
                cekPert = pertBaru.getSelectedItem().toString();
                cekTanggal = tanggalBaru.getText().toString();
                cekSaran = saranBaru.getText().toString();
                cekNoHp = nohpBaru.getText().toString();
                cekImage = fotoBaru.getDisplay().toString();

                if(isEmpty(cekNIM) || isEmpty(cekNama) || isEmpty(cekPert) ||
                isEmpty(cekTanggal) || isEmpty(cekSaran) || isEmpty(cekNoHp)){
                    Toast.makeText(UpdateActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }else{
                    Mahasiswa setMahasiswa = new Mahasiswa();
                    setMahasiswa.setNim(nimBaru.getText().toString());
                    setMahasiswa.setNama(namaBaru.getText().toString());
                    setMahasiswa.setPertemuan(pertBaru.getSelectedItem().toString());
                    setMahasiswa.setTanggal(tanggalBaru.getText().toString());
                    setMahasiswa.setSaran(saranBaru.getText().toString());
                    setMahasiswa.setNohp(nohpBaru.getText().toString());
                    setMahasiswa.setImage(task.getResult().toString());
                    updateMahasiswa(setMahasiswa);
                    if(Rb1.isChecked()){
                        setMahasiswa.setDivisi(Rb1.getText().toString());
                        updateMahasiswa(setMahasiswa);
                    }else if(Rb2.isChecked()){
                        setMahasiswa.setDivisi(Rb2.getText().toString());
                        updateMahasiswa(setMahasiswa);
                    }else if(Rb3.isChecked()){
                        setMahasiswa.setDivisi(Rb3.getText().toString());
                        updateMahasiswa(setMahasiswa);
                    }else{
                        Toast.makeText(UpdateActivity.this, "Pilih Divisi", Toast.LENGTH_SHORT).show();
                    }
                }
                            }
                        });
                    }
                });
            }
        });
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

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }
    private void getData(){
        final String getNIM = getIntent().getExtras().getString("dataNIM");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getPertemuan = getIntent().getExtras().getString("dataPertemuan");
        final String getTanggal = getIntent().getExtras().getString("dataTanggal");
        final String getSaran = getIntent().getExtras().getString("dataSaran");
        final String getNoHp = getIntent().getExtras().getString("dataNoHp");
        final  String getImageBaru = getIntent().getExtras().getString("dataImage");

        Picasso.get().load(getImageBaru).into(fotoBaru);
        nimBaru.setText(getNIM);
        namaBaru.setText(getNama);
        tanggalBaru.setText(getTanggal);
        saranBaru.setText(getSaran);
        nohpBaru.setText(getNoHp);
        pertBaru.setSelection(((ArrayAdapter<String>)pertBaru.getAdapter()).getPosition(getPertemuan));
        getDivisi();
    }
    private void getDivisi(){
        final String getDivisi = getIntent().getExtras().getString("dataDivisi");
        Toast.makeText(UpdateActivity.this, getDivisi, Toast.LENGTH_SHORT).show();
        if(getDivisi.toString().equals("PM")){
            Rb1.setChecked(true);
        }else if(getDivisi.toString().equals("MM")){
            Rb2.setChecked(true);
        }else{
            Rb3.setChecked(true);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kodeGallery && resultCode == RESULT_OK) {
            fotoUrl = data.getData();
            fotoBaru.setImageURI(fotoUrl);
        }
    }
    private void updateMahasiswa(Mahasiswa mahasiswa){
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Admin").child("Mahasiswa")
                .child(getKey)
                .setValue(mahasiswa)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid){
                        nimBaru.setText("");
                        namaBaru.setText("");
                        tanggalBaru.setText("");
                        saranBaru.setText("");
                        nohpBaru.setText("");
                        Toast.makeText(UpdateActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}