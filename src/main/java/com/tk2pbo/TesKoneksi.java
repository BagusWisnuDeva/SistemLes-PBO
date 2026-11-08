package com.tk2pbo;

import com.tk2pbo.util.DatabaseConnection;

public class TesKoneksi {
    public static void main(String[] args) {
        try (var c = DatabaseConnection.getConnection()) {
            System.out.println("Terhubung ke database!");
        } catch (Exception e) {
            System.out.println("Gagal: " + e.getMessage());
        }
    }
}