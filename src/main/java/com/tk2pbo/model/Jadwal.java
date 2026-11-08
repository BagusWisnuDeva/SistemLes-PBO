package com.tk2pbo.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import com.tk2pbo.util.FormatUtil;

public class Jadwal {
    public static final List<String> DaftarHari = List.of("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu");
    private int idJadwal;
    private Pendaftaran pendaftaran;
    private String hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String ruangan;

    public Jadwal(int idJadwal, Pendaftaran pendaftaran, String hari, LocalTime jamMulai,
        LocalTime jamSelesai, String ruangan) {
        if (pendaftaran == null) {
            throw new IllegalArgumentException("Pendaftaran tidak boleh kosong.");
        }
        this.idJadwal = idJadwal;
        this.pendaftaran = pendaftaran;
        setHari(hari);
        setWaktu(jamMulai, jamSelesai);
        setRuangan(ruangan);
    }

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public Pendaftaran getPendaftaran() {
        return pendaftaran;
    }

    public String getHari() {
        return hari;
    }

    public final void setHari(String hari) {
        if (hari == null || !DaftarHari.contains(hari)) {
            throw new IllegalArgumentException("Hari tidak valid. Pilih dari: " + DaftarHari);
        }
        this.hari = hari;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public final void setWaktu(LocalTime jamMulai, LocalTime jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            throw new IllegalArgumentException("Jam mulai dan jam selesai wajib diisi");
        }
        if (!jamSelesai.isAfter(jamMulai)) {
            throw new IllegalArgumentException("Jam selesai harus setelah jam mulai");
        }
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public String getRuangan() {
        return ruangan;
    }

    public final void setRuangan(String ruangan) {
        String ruanganError = FormatUtil.kosongjadinull(ruangan);
        if (ruanganError == null) {
            throw new IllegalArgumentException("Ruangan wajib diisi");
        }
        this.ruangan = ruanganError;
    }

    public long getDurasiMenit() {
        return Duration.between(jamMulai, jamSelesai).toMinutes();
    }

    public String alasanBentrok(Jadwal lain) {
        if (lain == null || (idJadwal != 0 && idJadwal == lain.idJadwal)) {
            return null;
        }
        if (!hari.equals(lain.hari)) {
            return null;
        }
        boolean waktuBeririsan = jamMulai.isBefore(lain.jamSelesai) && lain.jamMulai.isBefore(jamSelesai);
        if (!waktuBeririsan) {
            return null;
        }
        if (pendaftaran.getPengajar().getId() == lain.pendaftaran.getPengajar().getId()) {
            return "pengajar " + pendaftaran.getPengajar().getNama() + " sudah mengajar pada waktu tersebut";
        }
        if (ruangan.equalsIgnoreCase(lain.ruangan)) {
            return "ruangan " + ruangan + " sudah dipakai pada waktu tersebut";
        }
        if (pendaftaran.getSiswa().getId() == lain.pendaftaran.getSiswa().getId()) {
            return "siswa " + pendaftaran.getSiswa().getNama() + " sudah memiliki jadwal pada waktu tersebut";
        }
        return null;
    }

    public boolean cekBentrok(Jadwal jadwalLain) {
        return alasanBentrok(jadwalLain) != null;
    }

    public String getWaktuTeks() {
        return FormatUtil.formatJam(jamMulai) + " - " + FormatUtil.formatJam(jamSelesai);
    }

    @Override
    public String toString() {
        return hari + " " + getWaktuTeks() + " (" + ruangan + ")";
    }
}