package com.tk2pbo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Kursus;
import com.tk2pbo.model.MetodePembayaran;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.model.Pengajar;
import com.tk2pbo.model.Siswa;
import com.tk2pbo.util.DatabaseConnection;

public class PendaftaranDao implements CrudDao<Pendaftaran> {

    static final String KOLOM =
            "p.id_pendaftaran, p.tanggal_daftar, p.jumlah_pertemuan, p.total_biaya, "
            + "p.status_pembayaran, p.metode_pembayaran, p.tanggal_bayar, "
            + "s.id_siswa, s.nama AS s_nama, s.email AS s_email, s.no_telepon AS s_telepon, "
            + "s.alamat AS s_alamat, s.tanggal_lahir AS s_lahir, s.kelas AS s_kelas, "
            + "s.username AS s_username, s.password AS s_password, "
            + "k.id_kursus, k.nama_kursus, k.deskripsi AS k_deskripsi, k.biaya_per_pertemuan, k.durasi_menit, "
            + "g.id_pengajar, g.nama AS g_nama, g.email AS g_email, g.no_telepon AS g_telepon, "
            + "g.spesialisasi AS g_spesialisasi, g.tarif_per_jam, g.pengalaman";

    static final String JOIN =
            "JOIN siswa s ON p.id_siswa = s.id_siswa "
            + "JOIN kursus k ON p.id_kursus = k.id_kursus "
            + "JOIN pengajar g ON p.id_pengajar = g.id_pengajar";

    static Pendaftaran map(ResultSet rs) throws SQLException {
        Date lahir = rs.getDate("s_lahir");
        Siswa siswa = new Siswa(rs.getInt("id_siswa"), rs.getString("s_nama"), rs.getString("s_email"),
                rs.getString("s_telepon"), rs.getString("s_alamat"),
                lahir == null ? null : lahir.toLocalDate(), rs.getString("s_kelas"),
                rs.getString("s_username"), rs.getString("s_password"));
        Kursus kursus = new Kursus(rs.getInt("id_kursus"), rs.getString("nama_kursus"),
                rs.getString("k_deskripsi"), rs.getDouble("biaya_per_pertemuan"), rs.getInt("durasi_menit"));
        Pengajar pengajar = new Pengajar(rs.getInt("id_pengajar"), rs.getString("g_nama"),
                rs.getString("g_email"), rs.getString("g_telepon"), rs.getString("g_spesialisasi"),
                rs.getDouble("tarif_per_jam"), rs.getInt("pengalaman"));
        Timestamp bayar = rs.getTimestamp("tanggal_bayar");
        return new Pendaftaran(rs.getInt("id_pendaftaran"), siswa, kursus, pengajar,
                rs.getDate("tanggal_daftar").toLocalDate(), rs.getInt("jumlah_pertemuan"),
                rs.getDouble("total_biaya"), rs.getString("status_pembayaran"),
                MetodePembayaran.dari(rs.getString("metode_pembayaran")),
                bayar == null ? null : bayar.toLocalDateTime());
    }

    private List<Pendaftaran> ambil(String kondisi, Pengisi pengisi) throws SQLException {
        String sql = "SELECT " + KOLOM + " FROM pendaftaran p " + JOIN + " " + kondisi;
        List<Pendaftaran> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            pengisi.isi(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(map(rs));
                }
            }
        }
        return hasil;
    }

    @Override
    public void tambah(Pendaftaran p) throws SQLException {
        String sql = "INSERT INTO pendaftaran (id_siswa, id_kursus, id_pengajar, tanggal_daftar, "
                + "jumlah_pertemuan, total_biaya, status_pembayaran) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getSiswa().getId());
            ps.setInt(2, p.getKursus().getIdKursus());
            ps.setInt(3, p.getPengajar().getId());
            ps.setDate(4, Date.valueOf(p.getTanggalPendaftaran()));
            ps.setInt(5, p.getJumlahPertemuan());
            ps.setDouble(6, p.getTotalBiaya());
            ps.setString(7, p.getStatusPembayaran());
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    p.setIdPendaftaran(kunci.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Pendaftaran> semua() throws SQLException {
        return ambil("ORDER BY p.id_pendaftaran", ps -> { });
    }

    @Override
    public Pendaftaran cariById(int id) throws SQLException {
        List<Pendaftaran> hasil = ambil("WHERE p.id_pendaftaran = ?", ps -> ps.setInt(1, id));
        return hasil.isEmpty() ? null : hasil.get(0);
    }

    public List<Pendaftaran> bySiswa(int idSiswa) throws SQLException {
        return ambil("WHERE p.id_siswa = ? ORDER BY p.tanggal_pendaftaran DESC, p.id_pendaftaran DESC",
                ps -> ps.setInt(1, idSiswa));
    }

    public List<Pendaftaran> byKursus(int idKursus) throws SQLException {
        return ambil("WHERE p.id_kursus = ? ORDER BY s.nama", ps -> ps.setInt(1, idKursus));
    }

    public List<Pendaftaran> lunasAntara(LocalDate dari, LocalDate sampai) throws SQLException {
        return ambil("WHERE p.status_pembayaran = 'Lunas' AND DATE(p.tanggal_bayar) BETWEEN ? AND ? "
                        + "ORDER BY p.tanggal_bayar",
                ps -> {
                    ps.setDate(1, Date.valueOf(dari));
                    ps.setDate(2, Date.valueOf(sampai));
                });
    }

    @Override
    public void ubah(Pendaftaran p) throws SQLException {
        String sql = "UPDATE pendaftaran SET id_kursus = ?, id_pengajar = ?, jumlah_pertemuan = ?, total_biaya = ?, "
                + "status_pembayaran = ?, metode_pembayaran = ?, tanggal_bayar = ? WHERE id_pendaftaran = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getKursus().getIdKursus());
            ps.setInt(2, p.getPengajar().getId());
            ps.setInt(3, p.getJumlahPertemuan());
            ps.setDouble(4, p.getTotalBiaya());
            ps.setString(5, p.getStatusPembayaran());
            if (p.getMetodePembayaran() == null) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, p.getMetodePembayaran().getNamaMetode());
            }
            LocalDateTime bayar = p.getTanggalPembayaran();
            if (bayar == null) {
                ps.setNull(7, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(7, Timestamp.valueOf(bayar));
            }
            ps.setInt(8, p.getIdPendaftaran());
            ps.executeUpdate();
        }
    }

    @Override
    public void hapus(int id) throws SQLException, ValidasiException {
        try (Connection c = DatabaseConnection.getConnection()) {
            try (PreparedStatement cek = c.prepareStatement(
                    "SELECT status_pembayaran FROM pendaftaran WHERE id_pendaftaran = ?")) {
                cek.setInt(1, id);
                try (ResultSet rs = cek.executeQuery()) {
                    if (rs.next() && Pendaftaran.Lunas.equals(rs.getString(1))) {
                        throw new ValidasiException("Pendaftaran yang sudah lunas tidak dapat dibatalkan.");
                    }
                }
            }
            c.setAutoCommit(false);
            try (PreparedStatement hapusJadwal = c.prepareStatement("DELETE FROM jadwal WHERE id_pendaftaran = ?");
                 PreparedStatement hapusDaftar = c.prepareStatement("DELETE FROM pendaftaran WHERE id_pendaftaran = ?")) {
                hapusJadwal.setInt(1, id);
                hapusJadwal.executeUpdate();
                hapusDaftar.setInt(1, id);
                hapusDaftar.executeUpdate();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}
