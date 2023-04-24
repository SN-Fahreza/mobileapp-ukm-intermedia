package com.pmo.intermedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity implements RecyclerViewAdapter.dataListener {
    private EditText searchView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<Mahasiswa>dataMahasiswa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        recyclerView = findViewById(R.id.datalist);
        MyRecyclerView();
        GetData();

        searchView = findViewById(R.id.etSearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString() != null){
                    GetData(s.toString());
                }
                else {
                    GetData("");
                }

            }
        });
    }
    private void GetData(String data){
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Mahasiswa").orderByChild("nama").startAt(data).endAt(data)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataMahasiswa = new ArrayList<>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.exists()){
                                Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);
                                mahasiswa.setKey(snapshot.getKey());
                                dataMahasiswa.add(mahasiswa);
                                Toast.makeText(getApplicationContext(), "Data Ditemukan", Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter = new RecyclerViewAdapter(dataMahasiswa, ListDataActivity.this);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Data gagal dimuat", Toast.LENGTH_SHORT).show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());

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

    private void GetData() {
        Toast.makeText(getApplicationContext(), "Loading..", Toast.LENGTH_SHORT).show();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Mahasiswa")
                .addValueEventListener((new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataMahasiswa = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);

                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswa.add(mahasiswa);
                        }
                        adapter = new RecyclerViewAdapter(dataMahasiswa, ListDataActivity.this);
                        recyclerView.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "Data disimpan", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                }));
    }

    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onDeleteData(Mahasiswa data, int position) {
        if(reference != null){
            reference.child("Admin").child("Mahasiswa")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(ListDataActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}