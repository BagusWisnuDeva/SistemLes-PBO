package com.tk2pbo.model;

import com.tk2pbo.util.PasswordUtil;

public class Admin extends Pengguna {

    private String username;
    private String passwordHash;

    public Admin(int id, String nama, String email, String noTelepon,
        String username, String passwordHash) {
        super(id, nama, email, noTelepon);
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = PasswordUtil.hashPassword(passwordHash);
    }

    @Override
    public String tampilkanInfo() {
        return "Admin: " + getNama();
    }

    @Override
    public String getPeran() {
        return "Admin";
    }
}
