package com.tk2pbo.controller;

import java.sql.SQLException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.tk2pbo.dao.KursusDao;
import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Kursus;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class KursusController {

    @FXML private TableView<Kursus> tabel;
    @FXML private TableColumn<Kursus, Integer> colId;
    @FXML private TableColumn<Kursus, String> colNama;
    @FXML private TableColumn<Kursus, String> colDeskripsi;
    @FXML private TableColumn<Kursus, Double> colBiaya;
    @FXML private TableColumn<Kursus, Integer> colDurasi;
    @FXML private TextField tfCari;
    @FXML private TextField tfNama;
    @FXML private TextArea taDeskripsi;
    @FXML private TextField tfBiaya;
    @FXML private TextField tfDurasi;

    private final KursusDao dao = new KursusDao();
    private final ObservableList<Kursus> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TabelUtil.kolom(colId, Kursus::getIdKursus);
        TabelUtil.kolom(colNama, Kursus::getNamaKursus);
        TabelUtil.kolom(colDeskripsi, k -> Objects.toString(k.getDeskripsiKursus(), "-"));
        TabelUtil.kolomRupiah(colBiaya, Kursus::getBiayaPerPertemuan);
        TabelUtil.kolom(colDurasi, Kursus::getDurasiMenit);
        TabelUtil.pasang(tabel, data, tfCari, Kursus::getNamaKursus);
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru != null) {
                isiForm(baru);
            }
        });
        muatData();
    }

    private void muatData() {
        try {
            data.setAll(dao.semua());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    private void isiForm(Kursus k) {
        tfNama.setText(k.getNamaKursus());
        taDeskripsi.setText(k.getDeskripsiKursus());
        tfBiaya.setText(String.valueOf((long) k.getBiayaPerPertemuan()));
        tfDurasi.setText(String.valueOf(k.getDurasiMenit()));
    }

    private Kursus bacaForm(int id) {
        return new Kursus(id, tfNama.getText(), taDeskripsi.getText(),
                FormatUtil.parseDouble(tfBiaya.getText(), "Biaya per pertemuan"),
                FormatUtil.parseInt(tfDurasi.getText(), "Durasi"));
    }

    @FXML
    private void tambah() {
        try {
            dao.tambah(bacaForm(0));
            Notifikasi.info("Data kursus berhasil ditambahkan.");
            bersihkan();
            muatData();
        } catch (IllegalArgumentException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void ubah() {
        Kursus dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih kursus pada tabel terlebih dahulu.");
            return;
        }
        try {
            dao.ubah(bacaForm(dipilih.getIdKursus()));
            Notifikasi.info("Data kursus berhasil diubah.");
            bersihkan();
            muatData();
        } catch (IllegalArgumentException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void hapus() {
        Kursus dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih kursus pada tabel terlebih dahulu.");
            return;
        }
        if (!Notifikasi.konfirmasi("Hapus kursus \"" + dipilih.getNamaKursus() + "\"?")) {
            return;
        }
        try {
            dao.hapus(dipilih.getIdKursus());
            Notifikasi.info("Data kursus berhasil dihapus.");
            bersihkan();
            muatData();
        } catch (ValidasiException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void bersihkan() {
        tabel.getSelectionModel().clearSelection();
        tfNama.clear();
        taDeskripsi.clear();
        tfBiaya.clear();
        tfDurasi.clear();
    }
}
