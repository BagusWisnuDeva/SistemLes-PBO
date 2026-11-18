package com.tk2pbo.controller;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.tk2pbo.dao.JadwalDao;
import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Jadwal;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class JadwalController {

    @FXML private TableView<Jadwal> tabel;
    @FXML private TableColumn<Jadwal, Integer> colId;
    @FXML private TableColumn<Jadwal, String> colHari;
    @FXML private TableColumn<Jadwal, String> colJam;
    @FXML private TableColumn<Jadwal, String> colRuangan;
    @FXML private TableColumn<Jadwal, String> colSiswa;
    @FXML private TableColumn<Jadwal, String> colKursus;
    @FXML private TableColumn<Jadwal, String> colPengajar;
    @FXML private TextField tfCari;
    @FXML private ComboBox<Pendaftaran> cbPendaftaran;
    @FXML private ComboBox<String> cbHari;
    @FXML private TextField tfMulai;
    @FXML private TextField tfSelesai;
    @FXML private TextField tfRuangan;

    private final JadwalDao dao = new JadwalDao();
    private final ObservableList<Jadwal> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TabelUtil.kolom(colId, Jadwal::getIdJadwal);
        TabelUtil.kolom(colHari, Jadwal::getHari);
        TabelUtil.kolom(colJam, Jadwal::getWaktuTeks);
        TabelUtil.kolom(colRuangan, Jadwal::getRuangan);
        TabelUtil.kolom(colSiswa, j -> j.getPendaftaran().getSiswa().getNama());
        TabelUtil.kolom(colKursus, j -> j.getPendaftaran().getKursus().getNamaKursus());
        TabelUtil.kolom(colPengajar, j -> j.getPendaftaran().getPengajar().getNama());
        TabelUtil.pasang(tabel, data, tfCari, j -> j.getHari() + " " + j.getRuangan() + " "
                + j.getPendaftaran().getSiswa().getNama() + " " + j.getPendaftaran().getKursus().getNamaKursus()
                + " " + j.getPendaftaran().getPengajar().getNama());

        cbHari.setItems(FXCollections.observableArrayList(Jadwal.DaftarHari));
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru != null) {
                isiForm(baru);
            }
        });
        try {
            cbPendaftaran.setItems(FXCollections.observableArrayList(new PendaftaranDao().semua()));
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
        muatData();
    }

    private void muatData() {
        try {
            data.setAll(dao.semua());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    private void isiForm(Jadwal j) {
        TabelUtil.pilih(cbPendaftaran, p -> p.getIdPendaftaran() == j.getPendaftaran().getIdPendaftaran());
        cbHari.setValue(j.getHari());
        tfMulai.setText(FormatUtil.formatJam(j.getJamMulai()));
        tfSelesai.setText(FormatUtil.formatJam(j.getJamSelesai()));
        tfRuangan.setText(j.getRuangan());
    }

    private Jadwal bacaForm(int id) {
        return new Jadwal(id, cbPendaftaran.getValue(), cbHari.getValue(),
                FormatUtil.parseJam(tfMulai.getText(), "Jam mulai"),
                FormatUtil.parseJam(tfSelesai.getText(), "Jam selesai"),
                tfRuangan.getText());
    }

    @FXML
    private void tambah() {
        try {
            dao.tambah(bacaForm(0));
            Notifikasi.info("Jadwal berhasil ditambahkan.");
            bersihkan();
            muatData();
        } catch (IllegalArgumentException | ValidasiException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void ubah() {
        Jadwal dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih jadwal pada tabel terlebih dahulu.");
            return;
        }
        try {
            dao.ubah(bacaForm(dipilih.getIdJadwal()));
            Notifikasi.info("Jadwal berhasil diubah.");
            bersihkan();
            muatData();
        } catch (IllegalArgumentException | ValidasiException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void hapus() {
        Jadwal dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih jadwal pada tabel terlebih dahulu.");
            return;
        }
        if (!Notifikasi.konfirmasi("Hapus jadwal " + dipilih + "?")) {
            return;
        }
        try {
            dao.hapus(dipilih.getIdJadwal());
            Notifikasi.info("Jadwal berhasil dihapus.");
            bersihkan();
            muatData();
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void bersihkan() {
        tabel.getSelectionModel().clearSelection();
        cbPendaftaran.setValue(null);
        cbHari.setValue(null);
        tfMulai.clear();
        tfSelesai.clear();
        tfRuangan.clear();
    }
}
