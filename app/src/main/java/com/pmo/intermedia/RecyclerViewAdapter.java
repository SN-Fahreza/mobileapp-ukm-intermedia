package com.pmo.intermedia;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Mahasiswa> listMahasiswa;
    private Context context;

    public interface dataListener{
        void onDeleteData(Mahasiswa data, int position);
    }
    dataListener listener;

    public RecyclerViewAdapter(ArrayList<Mahasiswa> listMahasiswa, Context context) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        listener = (ListDataActivity)context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String NIM = listMahasiswa.get(position).getNim();
        final String Nama = listMahasiswa.get(position).getNama();
        final String Divisi = listMahasiswa.get(position).getDivisi();
        final String Pertemuan = listMahasiswa.get(position).getPertemuan();
        final String Tanggal = listMahasiswa.get(position).getTanggal();
        final String Saran = listMahasiswa.get(position).getSaran();
        final String NoHp = listMahasiswa.get(position).getNohp();
        final String Gambar =listMahasiswa.get(position).getImage();

        holder.NIM.setText("NIM: "+NIM);
        holder.Nama.setText("Nama: "+Nama);
        holder.Divisi.setText("Divisi: "+Divisi);
        holder.Pertemuan.setText("Pertemuan: "+Pertemuan);
        holder.Tanggal.setText("Tanggal: "+Tanggal);
        holder.Saran.setText("Saran: "+Saran);
        holder.Nohp.setText("No HP: "+NoHp);
        Picasso.get().load(Gambar).into(holder.Image);

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String[] action = {"Update","Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNIM", listMahasiswa.get(position).getNim());
                                bundle.putString("dataNama", listMahasiswa.get(position).getNama());
                                bundle.putString("dataDivisi", listMahasiswa.get(position).getDivisi());
                                bundle.putString("dataPertemuan", listMahasiswa.get(position).getPertemuan());
                                bundle.putString("dataTanggal", listMahasiswa.get(position).getTanggal());
                                bundle.putString("dataSaran", listMahasiswa.get(position).getSaran());
                                bundle.putString("dataNoHp", listMahasiswa.get(position).getNohp());
                                bundle.putString("dataImage", listMahasiswa.get(position).getImage());
                                bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                                Intent intent = new Intent(v.getContext(),UpdateActivity.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                listener.onDeleteData(listMahasiswa.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView NIM,Nama,Divisi,Pertemuan,Tanggal,Saran,Nohp;
        private ImageView Image;
        private LinearLayout ListItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            NIM = itemView.findViewById(R.id.nim);
            Nama = itemView.findViewById(R.id.nama);
            Divisi = itemView.findViewById(R.id.rg_div);
            Pertemuan = itemView.findViewById(R.id.spinner_pert);
            Tanggal = itemView.findViewById(R.id.tanggal);
            Saran = itemView.findViewById(R.id.saran);
            Nohp = itemView.findViewById(R.id.nohp);
            Image = itemView.findViewById(R.id.gambar1);
            ListItem = itemView.findViewById(R.id.list_item);
        }
    }
}
