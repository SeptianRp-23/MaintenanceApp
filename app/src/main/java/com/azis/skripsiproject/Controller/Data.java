package com.azis.skripsiproject.Controller;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    private int id;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("id_barang")
    private String id_barang;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("tipe")
    private String tipe;
    @SerializedName("nama")
    private String nama;
    @SerializedName("pokja")
    private String pokja;
    @SerializedName("kerusakan")
    private String kerusakan;
    @SerializedName("uraian")
    private String uraian;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("biaya")
    private String biaya;
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPokja() {
        return pokja;
    }

    public void setPokja(String pokja) {
        this.pokja = pokja;
    }

    public String getKerusakan() {
        return kerusakan;
    }

    public void setKerusakan(String kerusakan) {
        this.kerusakan = kerusakan;
    }

    public String getUraian() {
        return uraian;
    }

    public void setUraian(String uraian) {
        this.uraian = uraian;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
