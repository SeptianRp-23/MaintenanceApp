package com.azis.skripsiproject.Controller;

public class DataItemAdmin {

    private String id, no_inventaris, jenis, tipe, tanggal, pengguna, pokja;

    public DataItemAdmin() {
    }

    public DataItemAdmin(String id, String no_inventaris, String jenis, String tipe, String tanggal, String pengguna, String pokja) {
        this.id = id;
        this.no_inventaris = no_inventaris;
        this.jenis = jenis;
        this.tipe = tipe;
        this.tanggal = tanggal;
        this.pengguna = pengguna;
        this.pokja = pokja;
    }

    public String getId() {
        return id;
    }

    public String getNo_inventaris() {
        return no_inventaris;
    }

    public String getJenis() {
        return jenis;
    }

    public String getTipe() {
        return tipe;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getPengguna() {
        return pengguna;
    }

    public String getPokja() {
        return pokja;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNo_inventaris(String no_inventaris) {
        this.no_inventaris = no_inventaris;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setPengguna(String pengguna) {
        this.pengguna = pengguna;
    }

    public void setPokja(String pokja) {
        this.pokja = pokja;
    }
}
