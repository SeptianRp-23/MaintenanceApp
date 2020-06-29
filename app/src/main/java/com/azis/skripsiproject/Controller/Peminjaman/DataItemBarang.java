package com.azis.skripsiproject.Controller.Peminjaman;

public class DataItemBarang {

    private String no_invesBrg, jenisBrg, tipeBrg, tanggalBrg, statusBrg;

    public DataItemBarang() {
    }

    public DataItemBarang(String no_invesBrg, String jenisBrg, String tipeBrg, String tanggalBrg, String statusBrg) {
        this.no_invesBrg = no_invesBrg;
        this.jenisBrg = jenisBrg;
        this.tipeBrg = tipeBrg;
        this.tanggalBrg = tanggalBrg;
        this.statusBrg = statusBrg;
    }

    public String getNo_invesBrg() {
        return no_invesBrg;
    }

    public void setNo_invesBrg(String no_invesBrg) {
        this.no_invesBrg = no_invesBrg;
    }

    public String getJenisBrg() {
        return jenisBrg;
    }

    public void setJenisBrg(String jenisBrg) {
        this.jenisBrg = jenisBrg;
    }

    public String getTipeBrg() {
        return tipeBrg;
    }

    public void setTipeBrg(String tipeBrg) {
        this.tipeBrg = tipeBrg;
    }

    public String getTanggalBrg() {
        return tanggalBrg;
    }

    public void setTanggalBrg(String tanggalBrg) {
        this.tanggalBrg = tanggalBrg;
    }

    public String getStatusBrg() {
        return statusBrg;
    }

    public void setStatusBrg(String statusBrg) {
        this.statusBrg = statusBrg;
    }
}
