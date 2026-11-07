package com.tk2pbo.model;

import com.tk2pbo.util.PasswordUtil;

public class admin extends Pengguna {

    public admin(int id, String nama, String email, String noTelepon) {
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
