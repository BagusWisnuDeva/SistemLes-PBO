package com.tk2pbo.model;

public class PembayaranTransfer extends MetodePembayaran{

    public static final double BiayaTetap = 2500;
    
    public PembayaranTransfer() {
        super("Transfer");
    }

    @Override 
    public double hitungBiayaAdmin(double tagihan) {
        return BiayaTetap;
    }

    @Override
    public String getKeterangan() {
        return "Biaya admin sebesar Rp " + BiayaTetap + " untuk setiap transaksi transfer.";
    }

}
