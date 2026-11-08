package com.tk2pbo.model;

public class PembayaranTunai extends MetodePembayaran{

    public PembayaranTunai() {
        super("Tunai");
    }

    @Override 
    public double hitungBiayaAdmin(double tagihan) {
        return 0;
    }

    @Override 
    public String getKeterangan() {
        return "Tidak ada biaya admin untuk pembayaran tunai.";
    }

}
