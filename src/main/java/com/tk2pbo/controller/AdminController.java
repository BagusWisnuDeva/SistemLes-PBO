package com.tk2pbo.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import com.tk2pbo.util.Navigator;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.Sesi;

public class AdminController {

    @FXML private BorderPane root;
    @FXML private Label lblNama;
    @FXML private Button btnBeranda;
    @FXML private Button btnSiswa;
    @FXML private Button btnPengajar;
    @FXML private Button btnKursus;
    @FXML private Button btnPendaftaran;
    @FXML private Button btnJadwal;
    @FXML private Button btnPembayaran;
    @FXML private Button btnLaporan;

    @FXML
    private void initialize() {
        lblNama.setText(Sesi.getPengguna().getPeran() + ": " + Sesi.getPengguna().getNama());
        buka(btnBeranda, "beranda.fxml");
    }

    private void buka(Button aktif, String namaFxml) {
        List<Button> semua = List.of(btnBeranda, btnSiswa, btnPengajar, btnKursus,
                btnPendaftaran, btnJadwal, btnPembayaran, btnLaporan);
        for (Button b : semua) {
            b.getStyleClass().remove("menu-aktif");
        }
        aktif.getStyleClass().add("menu-aktif");
        Navigator.ganti(root, namaFxml);
    }

    @FXML private void bukaBeranda() { buka(btnBeranda, "beranda.fxml"); }

    @FXML private void bukaSiswa() { buka(btnSiswa, "siswa.fxml"); }

    @FXML private void bukaPengajar() { buka(btnPengajar, "pengajar.fxml"); }

    @FXML private void bukaKursus() { buka(btnKursus, "kursus.fxml"); }

    @FXML private void bukaPendaftaran() { buka(btnPendaftaran, "pendaftaran.fxml"); }

    @FXML private void bukaJadwal() { buka(btnJadwal, "jadwal.fxml"); }

    @FXML private void bukaPembayaran() { buka(btnPembayaran, "pembayaran.fxml"); }

    @FXML private void bukaLaporan() { buka(btnLaporan, "laporan.fxml"); }

    @FXML
    private void logout() {
        if (Notifikasi.konfirmasi("Keluar dari aplikasi?")) {
            Sesi.akhiri();
            Navigator.tampilLogin();
        }
    }
}
