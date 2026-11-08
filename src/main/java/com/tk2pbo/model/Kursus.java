package com.tk2pbo.model;

import com.tk2pbo.util.FormatUtil;

public class Kursus {
    
    private int idKursus;
    private String namaKursus;
    private String deskripsiKursus;
    private double biayaPerPertemuan;
    private int durasiMenit; 

    public Kursus(int idKursus, String namaKursus, String deskripsiKursus,
        double biayaPerPertemuan, int durasiMenit) {
        this.idKursus = idKursus;
        setNamaKursus(namaKursus);
        setDeskripsiKursus(deskripsiKursus);
        setBiayaPerPertemuan(biayaPerPertemuan);
        setDurasiMenit(durasiMenit);
    }

    public int getIdKursus() {
        return idKursus;
    }

    public final void setIdKursus(int idKursus) {
        this.idKursus = idKursus;
    }

    public String getNamaKursus() {
        return namaKursus;
    }

    public final void setNamaKursus (String namaKursus) {
        String namaKursusError = FormatUtil.kosongjadinull(namaKursus);
        if (namaKursusError == null) {
            throw new IllegalArgumentException("Nama kursus tidak boleh kosong.");
        }
        this.namaKursus = namaKursusError;
    }

    public String getDeskripsiKursus() {
        return deskripsiKursus;
    }

    public final void setDeskripsiKursus(String deskripsiKursus) {
        this.deskripsiKursus = FormatUtil.kosongjadinull(deskripsiKursus);
    }

    public double getBiayaPerPertemuan() {
        return biayaPerPertemuan;
    }

    public final void setBiayaPerPertemuan(double biayaPerPertemuan) {
        if (biayaPerPertemuan < 0) {
            throw new IllegalArgumentException("Biaya per pertemuan tidak boleh negatif.");
        }
        this.biayaPerPertemuan = biayaPerPertemuan;
    }

    public int getDurasiMenit() {
        return durasiMenit;
    }

    public final void setDurasiMenit(int durasiMenit) {
        if (durasiMenit <= 0) {
            throw new IllegalArgumentException("Durasi pertemuan harus lebih dari 0 menit.");
        }
        this.durasiMenit = durasiMenit;
    }       

    public String tampilkanInfoKursus() {
        return namaKursus + " | " + FormatUtil.rupiah(biayaPerPertemuan)
        + "/pertemuan | " + durasiMenit + " menit";
    }

    @Override 
    public String toString() {
        return namaKursus;
    }
    
}
