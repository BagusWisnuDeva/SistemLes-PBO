package com.tk2pbo.controller;

import java.sql.SQLException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.tk2pbo.dao.PengajarDao;
import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Pengajar;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

/** CRUD data pengajar lewat PengajarDao. */
public class PengajarController {

    @FXML private TableView<Pengajar> tabel;
    @FXML private TableColumn<Pengajar, Integer> colId;
    @FXML private TableColumn<Pengajar, String> colNama;
    @FXML private TableColumn<Pengajar, String> colEmail;
    @FXML private TableColumn<Pengajar, String> colTelepon;
    @FXML private TableColumn<Pengajar, String> colSpesialisasi;
    @FXML private TableColumn<Pengajar, Double> colTarif;
    @FXML private TableColumn<Pengajar, Integer> colPengalaman;
    @FXML private TextField tfCari;
    @FXML private TextField tfNama;
    @FXML private TextField tfEmail;
    @FXML private TextField tfTelepon;
    @FXML private TextField tfSpesialisasi;
    @FXML private TextField tfTarif;
    @FXML private TextField tfPengalaman;

    private final PengajarDao dao = new PengajarDao();
    private final ObservableList<Pengajar> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TabelUtil.kolom(colId, Pengajar::getId);
        TabelUtil.kolom(colNama, Pengajar::getNama);
        TabelUtil.kolom(colEmail, p -> Objects.toString(p.getEmail(), "-"));
        TabelUtil.kolom(colTelepon, p -> Objects.toString(p.getNoTelepon(), "-"));
        TabelUtil.kolom(colSpesialisasi, p -> Objects.toString(p.getSpesialisasi(), "-"));
        TabelUtil.kolomRupiah(colTarif, Pengajar::getTarifPerJam);
        TabelUtil.kolom(colPengalaman, Pengajar::getPengalamanMengajar);
        TabelUtil.pasang(tabel, data, tfCari,
                p -> p.getNama() + " " + Objects.toString(p.getSpesialisasi(), ""));
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

    private void isiForm(Pengajar p) {
        tfNama.setText(p.getNama());
        tfEmail.setText(p.getEmail());
        tfTelepon.setText(p.getNoTelepon());
        tfSpesialisasi.setText(p.getSpesialisasi());
        tfTarif.setText(String.valueOf((long) p.getTarifPerJam()));
        tfPengalaman.setText(String.valueOf(p.getPengalamanMengajar()));
    }

    private Pengajar bacaForm(int id) {
        return new Pengajar(id, tfNama.getText(), tfEmail.getText(), tfTelepon.getText(),
                tfSpesialisasi.getText(),
                FormatUtil.parseDouble(tfTarif.getText(), "Tarif per jam"),
                FormatUtil.parseInt(tfPengalaman.getText(), "Pengalaman"));
    }

    @FXML
    private void tambah() {
        try {
            dao.tambah(bacaForm(0));
            Notifikasi.info("Data pengajar berhasil ditambahkan.");
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
        Pengajar dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih pengajar pada tabel terlebih dahulu.");
            return;
        }
        try {
            dao.ubah(bacaForm(dipilih.getId()));
            Notifikasi.info("Data pengajar berhasil diubah.");
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
        Pengajar dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih pengajar pada tabel terlebih dahulu.");
            return;
        }
        if (!Notifikasi.konfirmasi("Hapus pengajar \"" + dipilih.getNama() + "\"?")) {
            return;
        }
        try {
            dao.hapus(dipilih.getId());
            Notifikasi.info("Data pengajar berhasil dihapus.");
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
        tfEmail.clear();
        tfTelepon.clear();
        tfSpesialisasi.clear();
        tfTarif.clear();
        tfPengalaman.clear();
    }
}
