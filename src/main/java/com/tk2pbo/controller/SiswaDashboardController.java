package com.tk2pbo.controller;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.tk2pbo.dao.JadwalDao;
import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.model.Jadwal;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.model.Pengguna;
import com.tk2pbo.model.Siswa;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Navigator;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.Sesi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class SiswaDashboardController {

    @FXML private Label lblSalam;
    @FXML private Label lblRingkasan;
    @FXML private TableView<Jadwal> tabelJadwal;
    @FXML private TableColumn<Jadwal, String> colHari;
    @FXML private TableColumn<Jadwal, String> colJam;
    @FXML private TableColumn<Jadwal, String> colRuangan;
    @FXML private TableColumn<Jadwal, String> colKursusJadwal;
    @FXML private TableColumn<Jadwal, String> colPengajarJadwal;
    @FXML private TableView<Pendaftaran> tabelRiwayat;
    @FXML private TableColumn<Pendaftaran, Integer> colId;
    @FXML private TableColumn<Pendaftaran, String> colKursus;
    @FXML private TableColumn<Pendaftaran, String> colPengajar;
    @FXML private TableColumn<Pendaftaran, String> colTanggal;
    @FXML private TableColumn<Pendaftaran, Integer> colPertemuan;
    @FXML private TableColumn<Pendaftaran, Double> colTotal;
    @FXML private TableColumn<Pendaftaran, String> colMetode;
    @FXML private TableColumn<Pendaftaran, String> colStatus;

    @FXML
    private void initialize() {
        Pengguna pengguna = Sesi.getPengguna();
        if (!(pengguna instanceof Siswa)) {
            Notifikasi.error("Sesi siswa tidak ditemukan. Silakan login kembali.");
            return;
        }
        Siswa siswa = (Siswa) pengguna;
        lblSalam.setText("Halo, " + siswa.getNama() + (siswa.getKelas() == null ? "" : " (" + siswa.getKelas() + ")"));

        TabelUtil.kolom(colHari, Jadwal::getHari);
        TabelUtil.kolom(colJam, Jadwal::getWaktuTeks);
        TabelUtil.kolom(colRuangan, Jadwal::getRuangan);
        TabelUtil.kolom(colKursusJadwal, j -> j.getPendaftaran().getKursus().getNamaKursus());
        TabelUtil.kolom(colPengajarJadwal, j -> j.getPendaftaran().getPengajar().getNama());

        TabelUtil.kolom(colId, Pendaftaran::getIdPendaftaran);
        TabelUtil.kolom(colKursus, p -> p.getKursus().getNamaKursus());
        TabelUtil.kolom(colPengajar, p -> p.getPengajar().getNama());
        TabelUtil.kolom(colTanggal, p -> FormatUtil.formatTanggal(p.getTanggalPendaftaran()));
        TabelUtil.kolom(colPertemuan, Pendaftaran::getJumlahPertemuan);
        TabelUtil.kolomRupiah(colTotal, Pendaftaran::getTotalBiaya);
        TabelUtil.kolom(colMetode, p -> p.getMetodePembayaran() == null ? "-" : p.getMetodePembayaran().getNamaMetode());
        TabelUtil.kolom(colStatus, Pendaftaran::getStatusPembayaran);
        TabelUtil.lebarProporsional(tabelJadwal);
        TabelUtil.lebarProporsional(tabelRiwayat);

        try {
            List<Pendaftaran> riwayat = new PendaftaranDao().bySiswa(siswa.getId());
            tabelRiwayat.setItems(FXCollections.observableArrayList(riwayat));
            tabelJadwal.setItems(FXCollections.observableArrayList(new JadwalDao().bySiswa(siswa.getId())));

            double belumBayar = riwayat.stream().filter(p -> !p.isLunas())
                    .mapToDouble(Pendaftaran::getTotalBiaya).sum();
            lblRingkasan.setText(riwayat.size() + " pendaftaran | Total tagihan "
                    + FormatUtil.rupiah(siswa.hitungBiayaKursus(riwayat))
                    + " | Belum dibayar " + FormatUtil.rupiah(belumBayar));
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void logout() {
        if (Notifikasi.konfirmasi("Keluar dari aplikasi?")) {
            Sesi.akhiri();
            Navigator.tampilLogin();
        }
    }
}
