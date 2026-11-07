package com.tk2pbo.model;

import com.tk2pbo.util.FormatUtil;

public class Pengajar extends Pengguna{

    private String spesialisasi;
    private double tarifPerJam;
    private int pengalamanMengajar;

    public Pengajar(int id, String nama, String email, String noTelepon,
        String spesialisasi, double tarifPerJam, int pengalamanMengajar) {
        super(id, nama, email, noTelepon);
        setSpesialisasi(spesialisasi);
        setTarifPerJam(tarifPerJam);
        setPengalamanMengajar(pengalamanMengajar);
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public final void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = FormatUtil.kosongjadinull(spesialisasi);
    }

    public double getTarifPerJam() {
        return tarifPerJam;
    }

    public final void setTarifPerJam(double tarifPerJam) {
        if (tarifPerJam < 0) {
            throw new IllegalArgumentException("Tarif per jam tidak boleh negatif.");
        }
        this.tarifPerJam = tarifPerJam;
    }

    public int getPengalamanMengajar() {
        return pengalamanMengajar;
    }

    public final void setPengalamanMengajar(int pengalamanMengajar) {
        if (pengalamanMengajar < 0) {
            throw new IllegalArgumentException("Pengalaman mengajar tidak boleh negatif.");
        }
        this.pengalamanMengajar = pengalamanMengajar;
    }

    public double hitungHonor() {
        return tarifPerJam * jam;
    }

    @Override 
    public String tampilkanInfo() {
        return "Pengajar: " + nama + " | " + (spesialisasi == null ? "-" : spesialisasi)
        + " | " + FormatUtil.rupiah(tarifPerJam) + "/jam | " + pengalamanMengajar + " tahun";
    }
}
