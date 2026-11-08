package com.tk2pbo.model;

public class PembayaranQris extends MetodePembayaran{
    
    public static final double Persen = 0.010;
    
    public PembayaranQris() {
        super("QRIS");
    }

    @Override 
    public double hitungBiayaAdmin(double tagihan) {
        return tagihan * Persen;
    }

    @Override
    public String getKeterangan() {
        return "Biaya admin sebesar " + Persen * 100 + "% dari total tagihan.";
    }
}
