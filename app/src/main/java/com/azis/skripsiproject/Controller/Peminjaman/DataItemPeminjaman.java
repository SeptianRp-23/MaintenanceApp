package com.azis.skripsiproject.Controller.Peminjaman;

public class DataItemPeminjaman {
    private String id, no_inventaris, jenis, tipe, tanggal, pengguna, pokja, status;

    public DataItemPeminjaman() {
    }

    public DataItemPeminjaman(String id, String no_inventaris, String jenis, String tipe, String tanggal, String pengguna, String pokja, String status) {
        this.id = id;
        this.no_inventaris = no_inventaris;
        this.jenis = jenis;
        this.tipe = tipe;
        this.tanggal = tanggal;
        this.pengguna = pengguna;
        this.pokja = pokja;
        this.status = status;
    }

    public String getIdPmjn() {
        return id;
    }

    public String getNo_inventarisPmjn() {
        return no_inventaris;
    }

    public String getJenisPmjn() {
        return jenis;
    }

    public String getTipePmjn() {
        return tipe;
    }

    public String getTanggalPmjn() {
        return tanggal;
    }

    public String getPenggunaPmjn() {
        return pengguna;
    }

    public String getPokjaPmjn() {
        return pokja;
    }

    public String getStatusPmjn() {
        return status;
    }

    public void setIdPmjn(String id) {
        this.id = id;
    }

    public void setNo_inventarisPmjn(String no_inventaris) {
        this.no_inventaris = no_inventaris;
    }

    public void setJenisPmjn(String jenis) {
        this.jenis = jenis;
    }

    public void setTipePmjn(String tipe) {
        this.tipe = tipe;
    }

    public void setTanggalPmjn(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setPenggunaPmjn(String pengguna) {
        this.pengguna = pengguna;
    }

    public void setPokjaPmjn(String pokja) {
        this.pokja = pokja;
    }

    public void setStatusPmjn(String status) {
        this.status = status;
    }
}
