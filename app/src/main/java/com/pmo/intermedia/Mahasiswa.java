package com.pmo.intermedia;

public class Mahasiswa {
    private String nim;
    private String nama;
    private String divisi;
    private String pertemuan;
    private String tanggal;
    private String saran;
    private String nohp;
    private String image;
    private String key;

    public Mahasiswa(String nim, String nama, String divisi, String pertemuan, String tanggal, String saran, String nohp, String image) {
        this.nim = nim;
        this.nama = nama;
        this.divisi = divisi;
        this.pertemuan = pertemuan;
        this.tanggal = tanggal;
        this.saran = saran;
        this.nohp = nohp;
        this.image = image;
    }

    public Mahasiswa() {
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getPertemuan() {
        return pertemuan;
    }

    public void setPertemuan(String pertemuan) {
        this.pertemuan = pertemuan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getSaran() {
        return saran;
    }

    public void setSaran(String saran) {
        this.saran = saran;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
