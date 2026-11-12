package com.tk2pbo.util;

import java.sql.SQLException;

public final class SqlErrorUtil {

    private SqlErrorUtil() { }

    public static String pesan(SQLException e) {
        String state = e.getSQLState() == null ? "" : e.getSQLState();
        switch (e.getErrorCode()) {
            case 1062:
                return "Data duplikat: username atau email sudah dipakai data lain.";
            case 1451:
                return "Data tidak dapat dihapus karena masih dipakai data lain.";
            case 1452:
                return "Data rujukan (siswa/kursus/pengajar/pendaftaran) tidak ditemukan.";
            case 1045:
                return "Login database ditolak. Periksa user dan password di db.properties.";
            case 1049:
                return "Database db_kursus belum ada. Import database/schema.sql terlebih dahulu.";
            default:
                break;
        }
        if (state.startsWith("08")) {
            return "Tidak dapat terhubung ke MySQL. Pastikan MySQL (XAMPP) sudah berjalan.";
        }
        return "Kesalahan database: " + e.getMessage();
    }
}
