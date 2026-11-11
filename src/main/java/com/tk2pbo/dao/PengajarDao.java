package com.tk2pbo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kursus.exception.ValidasiException;
import kursus.model.Pengajar;
import kursus.util.DatabaseConnection;

/** DAO tabel pengajar. */
public class PengajarDao implements CrudDao<Pengajar> {

    static final String KOLOM = "id_pengajar, nama, email, no_telepon, spesialisasi, tarif_per_jam, pengalaman";

    static Pengajar map(ResultSet rs) throws SQLException {
        return new Pengajar(rs.getInt("id_pengajar"), rs.getString("nama"), rs.getString("email"),
                rs.getString("no_telepon"), rs.getString("spesialisasi"),
                rs.getDouble("tarif_per_jam"), rs.getInt("pengalaman"));
    }

    @Override
    public void tambah(Pengajar p) throws SQLException {
        String sql = "INSERT INTO pengajar (nama, email, no_telepon, spesialisasi, tarif_per_jam, pengalaman) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            isiParameter(ps, p);
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    p.setId(kunci.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Pengajar> semua() throws SQLException {
        List<Pengajar> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM + " FROM pengajar ORDER BY nama");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(map(rs));
            }
        }
        return hasil;
    }

    @Override
    public Pengajar cariById(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM + " FROM pengajar WHERE id_pengajar = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public void ubah(Pengajar p) throws SQLException {
        String sql = "UPDATE pengajar SET nama = ?, email = ?, no_telepon = ?, spesialisasi = ?, "
                + "tarif_per_jam = ?, pengalaman = ? WHERE id_pengajar = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, p);
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void hapus(int id) throws SQLException, ValidasiException {
        try (Connection c = DatabaseConnection.getConnection()) {
            try (PreparedStatement cek = c.prepareStatement("SELECT COUNT(*) FROM pendaftaran WHERE id_pengajar = ?")) {
                cek.setInt(1, id);
                try (ResultSet rs = cek.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new ValidasiException("Pengajar tidak dapat dihapus karena masih mengajar pada "
                                + rs.getInt(1) + " pendaftaran.");
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM pengajar WHERE id_pengajar = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    private void isiParameter(PreparedStatement ps, Pengajar p) throws SQLException {
        ps.setString(1, p.getNama());
        SiswaDao.setNullableString(ps, 2, p.getEmail());
        SiswaDao.setNullableString(ps, 3, p.getNoTelepon());
        SiswaDao.setNullableString(ps, 4, p.getSpesialisasi());
        ps.setDouble(5, p.getTarifPerJam());
        ps.setInt(6, p.getPengalaman());
    }
}
