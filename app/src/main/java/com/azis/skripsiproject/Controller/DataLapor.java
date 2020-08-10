package com.azis.skripsiproject.Controller;

import com.google.gson.annotations.SerializedName;

public class DataLapor {
    @SerializedName("id")
    private int id;
    @SerializedName("id_perbaikan")
    private String id_perbaikan;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("nama")
    private String nama;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("tipe")
    private String tipe;
    @SerializedName("kerusakan")
    private String kerusakan;
    @SerializedName("detail")
    private String detail;
    @SerializedName("perbaikan")
    private String perbaikan;
    @SerializedName("biaya")
    private String biaya;
    @SerializedName("tanggal")
    private String tanggal;
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

    public String getId_perbaikan() {
        return id_perbaikan;
    }

    public void setId_perbaikan(String id_perbaikan) {
        this.id_perbaikan = id_perbaikan;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getKerusakan() {
        return kerusakan;
    }

    public void setKerusakan(String kerusakan) {
        this.kerusakan = kerusakan;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPerbaikan() {
        return perbaikan;
    }

    public void setPerbaikan(String perbaikan) {
        this.perbaikan = perbaikan;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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
