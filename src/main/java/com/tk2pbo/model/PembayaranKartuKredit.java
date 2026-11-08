package com.tk2pbo.model;

public class PembayaranKartuKredit extends MetodePembayaran{

    public static final double Persen = 0.25;
    public PembayaranKartuKredit() {
        super("Kartu Kredit");
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
