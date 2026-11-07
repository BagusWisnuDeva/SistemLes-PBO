package com.tk2pbo.model;

import com.tk2pbo.util.FormatUtil;

public abstract class Pengguna {

    protected int id;
    protected String nama;
    protected String email;
    protected String noTelepon;

    protected Pengguna(int id, String nama, String email, String noTelepon) {
        setId(id);
        setNama(nama);
        setEmail(email);
        setNoTelepon(noTelepon);
    }

    public abstract String tampilkanInfo();

    public abstract String getPeran();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID harus lebih besar dari 0.");
        }
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String emailError = FormatUtil.kosongjadinull(email);
        if (emailError != null && !emailError.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email tidak valid.");
        }
        this.email = emailError;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        String noTeleponError = FormatUtil.kosongjadinull(noTelepon);
        if (noTeleponError != null && !noTeleponError.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Nomor telepon tidak valid. Harus berupa angka dan dapat diawali dengan +.");
        }
        this.noTelepon = noTeleponError;
    }
}
