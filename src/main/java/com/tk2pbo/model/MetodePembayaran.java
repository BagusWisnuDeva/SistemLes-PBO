package com.tk2pbo.model;

import java.util.List;
public abstract class MetodePembayaran {

    private final String namaMetode;

    protected MetodePembayaran(String namaMetode) {
        this.namaMetode = namaMetode;
    }

    public String getNamaMetode() {
        return namaMetode;
    }

    public abstract double hitungBiayaAdmin(double tagihan);
    public abstract String getKeterangan();
    public static MetodePembayaran dari (String nama) {
        if (nama == null) {
            return null;
        }
        switch (nama) {
            case "Tunai":
                return new PembayaranTunai();
            case "Transfer":
                return new PembayaranTransfer();
            case "QRIS":
                return new PembayaranQris();
            case "Kartu Kredit":
                return new PembayaranKartuKredit();
            default:
                throw new IllegalArgumentException("Metode pembayaran tidak valid: " + nama);
        }
    }

    public static List<MetodePembayaran> semua() {
        return List.of(
            new PembayaranTunai(),
            new PembayaranTransfer(),
            new PembayaranQris(),
            new PembayaranKartuKredit()
        );
    }

    @Override 
    public String toString() {
        return namaMetode;
    }


}
