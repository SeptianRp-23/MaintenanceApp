package com.azis.skripsiproject.Controller.Perbaikan;

public class DataItemPengajuan {

    private String id, id_user, nama_user, id_barang, jenis, tipe, nama, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status;


    public DataItemPengajuan() {

    }

    public DataItemPengajuan(String id, String id_user, String nama_user, String id_barang, String jenis, String tipe, String nama, String pokja, String kerusakan, String uraian, String tanggal, String keterangan, String biaya, String gambar, String status){
        this.id = id;
        this.id_user = id_user;
        this.nama_user = nama_user;
        this.id_barang = id_barang;
        this.jenis = jenis;
        this.tipe = tipe;
        this.nama = nama;
        this.pokja = pokja;
        this.kerusakan = kerusakan;
        this.uraian = uraian;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.biaya = biaya;
        this.gambar = gambar;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getId_user() {
        return id_user;
    }

    public String getNama_user() {
        return nama_user;
    }

    public String getId_barang() {
        return id_barang;
    }

    public String getJenis() {
        return jenis;
    }

    public String getTipe() {
        return tipe;
    }

    public String getNama() {
        return nama;
    }

    public String getPokja() {
        return pokja;
    }

    public String getKerusakan() {
        return kerusakan;
    }

    public String getUraian() {
        return uraian;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getBiaya() {
        return biaya;
    }

    public String getGambar() {
        return gambar;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setPokja(String pokja) {
        this.pokja = pokja;
    }

    public void setKerusakan(String kerusakan) {
        this.kerusakan = kerusakan;
    }

    public void setUraian(String uraian) {
        this.uraian = uraian;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
