package com.tk2pbo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.model.MetodePembayaran;
import com.tk2pbo.model.Pembayaranable;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class PembayaranController {

    @FXML private TableView<Pendaftaran> tabel;
    @FXML private TableColumn<Pendaftaran, Integer> colId;
    @FXML private TableColumn<Pendaftaran, String> colSiswa;
    @FXML private TableColumn<Pendaftaran, String> colKursus;
    @FXML private TableColumn<Pendaftaran, Double> colTagihan;
    @FXML private TableColumn<Pendaftaran, String> colMetode;
    @FXML private TableColumn<Pendaftaran, String> colStatus;
    @FXML private TableColumn<Pendaftaran, String> colTanggalBayar;
    @FXML private TextField tfCari;
    @FXML private Label lblDetail;
    @FXML private ComboBox<MetodePembayaran> cbMetode;
    @FXML private Label lblRincian;
    @FXML private Button btnBayar;
    @FXML private Button btnSimpanStruk;
    @FXML private TextArea taStruk;

    private final PendaftaranDao dao = new PendaftaranDao();
    private final ObservableList<Pendaftaran> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TabelUtil.kolom(colId, Pendaftaran::getIdPendaftaran);
        TabelUtil.kolom(colSiswa, p -> p.getSiswa().getNama());
        TabelUtil.kolom(colKursus, p -> p.getKursus().getNamaKursus());
        TabelUtil.kolomRupiah(colTagihan, Pendaftaran::getTotalBiaya);
        TabelUtil.kolom(colMetode, p -> p.getMetodePembayaran() == null ? "-" : p.getMetodePembayaran().getNamaMetode());
        TabelUtil.kolom(colStatus, Pendaftaran::getStatusPembayaran);
        TabelUtil.kolom(colTanggalBayar, p -> FormatUtil.formatTanggalJam(p.getTanggalPembayaran()));
        TabelUtil.pasang(tabel, data, tfCari, p -> p.getSiswa().getNama() + " " + p.getKursus().getNamaKursus()
                + " " + p.getStatusPembayaran());

        cbMetode.setItems(FXCollections.observableArrayList(MetodePembayaran.semua()));
        cbMetode.valueProperty().addListener((obs, lama, baru) -> perbaruiRincian());
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> tampilkan(baru));
        tampilkan(null);
        muatData();
    }

    private void muatData() {
        try {
            data.setAll(dao.semua());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    private void tampilkan(Pendaftaran p) {
        if (p == null) {
            lblDetail.setText("Pilih pendaftaran pada tabel.");
            cbMetode.setValue(null);
            cbMetode.setDisable(true);
            btnBayar.setDisable(true);
            btnSimpanStruk.setDisable(true);
            taStruk.clear();
            lblRincian.setText("");
            return;
        }
        lblDetail.setText("#" + p.getIdPendaftaran() + " | " + p.getSiswa().getNama() + "\n"
                + p.getKursus().getNamaKursus() + " - " + p.getJumlahPertemuan() + " pertemuan\n"
                + "Tagihan pokok: " + FormatUtil.rupiah(p.getTotalBiaya()));
        if (p.isLunas()) {
            TabelUtil.pilih(cbMetode, m -> m.getNamaMetode().equals(p.getMetodePembayaran().getNamaMetode()));
            cbMetode.setDisable(true);
            btnBayar.setDisable(true);
            btnSimpanStruk.setDisable(false);
            taStruk.setText(p.cetakStruk());
            lblRincian.setText("Sudah lunas pada " + FormatUtil.formatTanggalJam(p.getTanggalPembayaran()));
        } else {
            cbMetode.setValue(null);
            cbMetode.setDisable(false);
            btnBayar.setDisable(false);
            btnSimpanStruk.setDisable(true);
            taStruk.clear();
            lblRincian.setText("Pilih metode untuk melihat total yang harus dibayar.");
        }
    }

    private void perbaruiRincian() {
        Pendaftaran p = tabel.getSelectionModel().getSelectedItem();
        MetodePembayaran m = cbMetode.getValue();
        if (p == null || m == null || p.isLunas()) {
            return;
        }
        double layanan = m.hitungBiayaAdmin(p.getTotalBiaya());
        lblRincian.setText(m.getKeterangan() + "\nBiaya layanan: " + FormatUtil.rupiah(layanan)
                + "\nTotal bayar: " + FormatUtil.rupiah(p.getTotalBiaya() + layanan));
    }

    @FXML
    private void bayar() {
        Pendaftaran dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih pendaftaran pada tabel terlebih dahulu.");
            return;
        }
        if (cbMetode.getValue() == null) {
            Notifikasi.error("Pilih metode pembayaran terlebih dahulu.");
            return;
        }
        dipilih.setMetodePembayaran(cbMetode.getValue());
        Pembayaranable bisaBayar = dipilih;
        if (!Notifikasi.konfirmasi("Proses pembayaran sebesar " + FormatUtil.rupiah(bisaBayar.hitungTotalBayar())
                + " melalui " + cbMetode.getValue().getNamaMetode() + "?")) {
            return;
        }
        int id = dipilih.getIdPendaftaran();
        try {
            bisaBayar.prosesPembayaran();
            dao.ubah(dipilih);
            Notifikasi.info("Pembayaran berhasil dicatat. Struk dapat dilihat dan disimpan.");
        } catch (IllegalStateException e) {
            Notifikasi.error(e.getMessage());
            return;
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
        muatData();
        tabel.getItems().stream().filter(p -> p.getIdPendaftaran() == id).findFirst()
                .ifPresent(p -> tabel.getSelectionModel().select(p));
    }

    @FXML
    private void simpanStruk() {
        Pendaftaran dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null || !dipilih.isLunas()) {
            Notifikasi.error("Struk hanya tersedia untuk pendaftaran yang sudah lunas.");
            return;
        }
        FileChooser pemilih = new FileChooser();
        pemilih.setTitle("Simpan Struk Pembayaran");
        pemilih.setInitialFileName("struk-pendaftaran-" + dipilih.getIdPendaftaran() + ".txt");
        pemilih.getExtensionFilters().add(new FileChooser.ExtensionFilter("Berkas teks (*.txt)", "*.txt"));
        File berkas = pemilih.showSaveDialog(tabel.getScene().getWindow());
        if (berkas == null) {
            return;
        }
        try {
            Files.writeString(berkas.toPath(), dipilih.cetakStruk(), StandardCharsets.UTF_8);
            Notifikasi.info("Struk disimpan di:\n" + berkas.getAbsolutePath());
        } catch (IOException e) {
            Notifikasi.error("Gagal menyimpan struk: " + e.getMessage());
        }
    }
}
