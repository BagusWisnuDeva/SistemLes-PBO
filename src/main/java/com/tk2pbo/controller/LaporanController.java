package com.tk2pbo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import com.tk2pbo.dao.KursusDao;
import com.tk2pbo.dao.PengajarDao;
import com.tk2pbo.laporan.Laporan;
import com.tk2pbo.laporan.LaporanJadwalPengajar;
import com.tk2pbo.laporan.LaporanPendaftaran;
import com.tk2pbo.laporan.LaporanPendapatan;
import com.tk2pbo.model.Kursus;
import com.tk2pbo.model.Pengajar;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class LaporanController {

    private static final String PENDAFTARAN = "Laporan Pendaftaran";
    private static final String PENDAPATAN = "Laporan Pendapatan";
    private static final String JADWAL = "Laporan Jadwal Pengajar";

    @FXML private ComboBox<String> cbJenis;
    @FXML private ComboBox<Kursus> cbKursus;
    @FXML private ComboBox<Pengajar> cbPengajar;
    @FXML private DatePicker dpDari;
    @FXML private DatePicker dpSampai;
    @FXML private Label lblJudul;
    @FXML private Label lblRingkasan;
    @FXML private TableView<List<String>> tabel;

    private Laporan laporanAktif;

    @FXML
    private void initialize() {
        TabelUtil.formatTanggal(dpDari, dpSampai);
        cbJenis.setItems(FXCollections.observableArrayList(PENDAFTARAN, PENDAPATAN, JADWAL));
        cbJenis.setValue(PENDAFTARAN);
        cbJenis.valueProperty().addListener((obs, lama, baru) -> aturFilter());
        resetFilter();
        try {
            cbKursus.setItems(FXCollections.observableArrayList(new KursusDao().semua()));
            cbPengajar.setItems(FXCollections.observableArrayList(new PengajarDao().semua()));
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
        aturFilter();
    }

    private void aturFilter() {
        String jenis = cbJenis.getValue();
        cbKursus.setDisable(!PENDAFTARAN.equals(jenis));
        cbPengajar.setDisable(!JADWAL.equals(jenis));
        dpDari.setDisable(!PENDAPATAN.equals(jenis));
        dpSampai.setDisable(!PENDAPATAN.equals(jenis));
    }

    @FXML
    private void resetFilter() {
        cbKursus.setValue(null);
        cbPengajar.setValue(null);
        dpSampai.setValue(LocalDate.now());
        dpDari.setValue(LocalDate.now().minusMonths(3));
    }

    private Laporan buatLaporan() {
        String jenis = cbJenis.getValue();
        if (PENDAPATAN.equals(jenis)) {
            return new LaporanPendapatan(dpDari.getValue(), dpSampai.getValue());
        }
        if (JADWAL.equals(jenis)) {
            return new LaporanJadwalPengajar(cbPengajar.getValue());
        }
        return new LaporanPendaftaran(cbKursus.getValue());
    }

    @FXML
    private void tampilkan() {
        try {
            Laporan laporan = buatLaporan();
            List<List<String>> baris = laporan.muat();

            tabel.getColumns().clear();
            List<String> kolom = laporan.getKolom();
            for (int i = 0; i < kolom.size(); i++) {
                final int indeks = i;
                TableColumn<List<String>, String> kol = new TableColumn<>(kolom.get(i));
                kol.setCellValueFactory(sel -> new SimpleStringProperty(sel.getValue().get(indeks)));
                tabel.getColumns().add(kol);
            }
            tabel.setItems(FXCollections.observableArrayList(baris));
            lblJudul.setText(laporan.getJudul());
            lblRingkasan.setText(laporan.getRingkasan());
            laporanAktif = laporan;
        } catch (IllegalArgumentException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void ekspor() {
        if (laporanAktif == null) {
            Notifikasi.error("Tampilkan laporan terlebih dahulu sebelum diekspor.");
            return;
        }
        FileChooser pemilih = new FileChooser();
        pemilih.setTitle("Ekspor Laporan ke CSV");
        pemilih.setInitialFileName("laporan.csv");
        pemilih.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV (*.csv)", "*.csv"));
        File berkas = pemilih.showSaveDialog(tabel.getScene().getWindow());
        if (berkas == null) {
            return;
        }
        try {
            Files.writeString(berkas.toPath(), "﻿" + laporanAktif.keCsv(), StandardCharsets.UTF_8);
            Notifikasi.info("Laporan diekspor ke:\n" + berkas.getAbsolutePath());
        } catch (IOException e) {
            Notifikasi.error("Gagal mengekspor laporan: " + e.getMessage());
        }
    }
}
