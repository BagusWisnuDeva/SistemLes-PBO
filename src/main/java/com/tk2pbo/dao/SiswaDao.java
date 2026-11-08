package com.tk2pbo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Siswa;
import com.tk2pbo.util.DatabaseConnection;
import com.tk2pbo.util.PasswordUtil;

public class SiswaDao implements CrudDao<Siswa> {

    static final String KOLOM = "id_siswa, nama, email, no_telepon, alamat, tanggal_lahir, kelas, username, password";

    static Siswa map(ResultSet rs) throws SQLException {
        Date lahir = rs.getDate("tanggal_lahir");
        return new Siswa(rs.getInt("id_siswa"), rs.getString("nama"), rs.getString("email"),
                rs.getString("no_telepon"), rs.getString("alamat"),
                lahir == null ? null : lahir.toLocalDate(), rs.getString("kelas"),
                rs.getString("username"), rs.getString("password"));
    }

    @Override
    public void tambah(Siswa s) throws SQLException {
        String sql = "INSERT INTO siswa (nama, email, no_telepon, alamat, tanggal_lahir, kelas, username, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            isiParameter(ps, s);
            ps.setString(8, s.getPasswordHash());
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    s.setId(kunci.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Siswa> semua() throws SQLException {
        List<Siswa> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM + " FROM siswa ORDER BY nama");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(map(rs));
            }
        }
        return hasil;
    }

    @Override
    public Siswa cariById(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM + " FROM siswa WHERE id_siswa = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public void ubah(Siswa s) throws SQLException {
        boolean gantiPassword = s.getPasswordHash() != null;
        String sql = "UPDATE siswa SET nama = ?, email = ?, no_telepon = ?, alamat = ?, tanggal_lahir = ?, "
                + "kelas = ?, username = ?" + (gantiPassword ? ", password = ?" : "") + " WHERE id_siswa = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, s);
            int urutan = 8;
            if (gantiPassword) {
                ps.setString(urutan++, s.getPasswordHash());
            }
            ps.setInt(urutan, s.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void hapus(int id) throws SQLException, ValidasiException {
        try (Connection c = DatabaseConnection.getConnection()) {
            try (PreparedStatement cek = c.prepareStatement("SELECT COUNT(*) FROM pendaftaran WHERE id_siswa = ?")) {
                cek.setInt(1, id);
                try (ResultSet rs = cek.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new ValidasiException("Siswa tidak dapat dihapus karena masih memiliki "
                                + rs.getInt(1) + " pendaftaran. Batalkan pendaftarannya terlebih dahulu.");
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM siswa WHERE id_siswa = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    public Siswa login(String username, String passwordBiasa) throws SQLException {
        String sql = "SELECT " + KOLOM + " FROM siswa WHERE username = ? AND password = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hashPassword(passwordBiasa));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private void isiParameter(PreparedStatement ps, Siswa s) throws SQLException {
        ps.setString(1, s.getNama());
        setNullableString(ps, 2, s.getEmail());
        setNullableString(ps, 3, s.getNoTelepon());
        setNullableString(ps, 4, s.getAlamat());
        if (s.getTanggalLahir() == null) {
            ps.setNull(5, Types.DATE);
        } else {
            ps.setDate(5, Date.valueOf(s.getTanggalLahir()));
        }
        setNullableString(ps, 6, s.getKelas());
        ps.setString(7, s.getUsername());
    }

    static void setNullableString(PreparedStatement ps, int indeks, String nilai) throws SQLException {
        if (nilai == null) {
            ps.setNull(indeks, Types.VARCHAR);
        } else {
            ps.setString(indeks, nilai);
        }
    }
}
