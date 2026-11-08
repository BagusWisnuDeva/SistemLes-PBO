package com.tk2pbo.model;

import java.time.LocalDate;
import java.util.List;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.PasswordUtil;

public class Siswa extends Pengguna {
    
    private String alamat;
    private LocalDate tanggalLahir;
    private String passwordHash;
    private String kelas;
    private String username;

    public Siswa(int id, String nama, String email, String noTelepon, String alamat,
        LocalDate tanggalLahir, String kelas, String username, String passwordHash) {
        super(id, nama, email, noTelepon);
        setAlamat(alamat);
        setTanggalLahir(tanggalLahir);
        setKelas(kelas);
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getAlamat() {
        return alamat;
    }

    public final void setAlamat(String alamat) {
        this.alamat = FormatUtil.kosongjadinull(alamat);
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public final void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getKelas() {
        return kelas;
    }

    public final void setKelas(String kelas) {
        this.kelas = FormatUtil.kosongjadinull(kelas);
    }

    public String getUsername() {
        return username;
    }

    public final void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public final void setPasswordHash(String passwordBiasa) {
        this.passwordHash = PasswordUtil.hashPassword(passwordBiasa);
    }

    public double hitungBiayaKursus(List<Pendaftaran> daftar) {
    return daftar.stream()
            .filter(p -> p.getSiswa().getId() == this.id)
            .mapToDouble(Pendaftaran::getTotalBiaya)
            .sum();
}

    @Override
    public String tampilkanInfo() {
        return "Siswa:" + nama + " | Kelas: " + (kelas == null ? "-" : kelas)
        + " | Tanggal Lahir " + FormatUtil.formatTanggal(tanggalLahir)
        + " | Alamat: " + (alamat != null ? alamat : "-");
    }  

    @Override
    public String getPeran() {
        return "Siswa";
    }
}
