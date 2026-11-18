package com.tk2pbo.controller;

import java.sql.SQLException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.tk2pbo.dao.SiswaDao;
import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Siswa;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.PasswordUtil;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class SiswaController {

    @FXML private TableView<Siswa> tabel;
    @FXML private TableColumn<Siswa, Integer> colId;
    @FXML private TableColumn<Siswa, String> colNama;
    @FXML private TableColumn<Siswa, String> colEmail;
    @FXML private TableColumn<Siswa, String> colTelepon;
    @FXML private TableColumn<Siswa, String> colLahir;
    @FXML private TableColumn<Siswa, String> colKelas;
    @FXML private TableColumn<Siswa, String> colUsername;
    @FXML private TextField tfCari;
    @FXML private TextField tfNama;
    @FXML private TextField tfEmail;
    @FXML private TextField tfTelepon;
    @FXML private TextField tfAlamat;
    @FXML private TextField tfKelas;
    @FXML private TextField tfUsername;
    @FXML private DatePicker dpLahir;
    @FXML private PasswordField pfPassword;
    @FXML private Label lblPasswordHint;

    private final SiswaDao dao = new SiswaDao();
    private final ObservableList<Siswa> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TabelUtil.formatTanggal(dpLahir);
        TabelUtil.kolom(colId, Siswa::getId);
        TabelUtil.kolom(colNama, Siswa::getNama);
        TabelUtil.kolom(colEmail, s -> Objects.toString(s.getEmail(), "-"));
        TabelUtil.kolom(colTelepon, s -> Objects.toString(s.getNoTelepon(), "-"));
        TabelUtil.kolom(colLahir, s -> FormatUtil.formatTanggal(s.getTanggalLahir()));
        TabelUtil.kolom(colKelas, s -> Objects.toString(s.getKelas(), "-"));
        TabelUtil.kolom(colUsername, s -> Objects.toString(s.getUsername(), "-"));
        TabelUtil.pasang(tabel, data, tfCari, s -> s.getNama() + " "
                + Objects.toString(s.getKelas(), "") + " " + Objects.toString(s.getUsername(), ""));
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

    private void isiForm(Siswa s) {
        tfNama.setText(s.getNama());
        tfEmail.setText(s.getEmail());
        tfTelepon.setText(s.getNoTelepon());
        tfAlamat.setText(s.getAlamat());
        dpLahir.setValue(s.getTanggalLahir());
        tfKelas.setText(s.getKelas());
        tfUsername.setText(s.getUsername());
        pfPassword.clear();
        lblPasswordHint.setText("Kosongkan jika password tidak diubah");
    }

    private Siswa bacaForm(int id, String hashLama) {
        String username = FormatUtil.wajib(tfUsername.getText(), "Username");
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username minimal 3 karakter");
        }
        String hash = hashLama;
        String password = pfPassword.getText();
        if (!password.isEmpty()) {
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password minimal 6 karakter");
            }
            hash = PasswordUtil.hashPassword(password);
        }
        if (hash == null) {
            throw new IllegalArgumentException("Password wajib diisi untuk siswa baru");
        }
        return new Siswa(id, tfNama.getText(), tfEmail.getText(), tfTelepon.getText(), tfAlamat.getText(),
                dpLahir.getValue(), tfKelas.getText(), username, hash);
    }

    @FXML
    private void tambah() {
        try {
            dao.tambah(bacaForm(0, null));
            Notifikasi.info("Data siswa berhasil ditambahkan.");
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
        Siswa dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih siswa pada tabel terlebih dahulu.");
            return;
        }
        try {
            dao.ubah(bacaForm(dipilih.getId(), dipilih.getPasswordHash()));
            Notifikasi.info("Data siswa berhasil diubah.");
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
        Siswa dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih siswa pada tabel terlebih dahulu.");
            return;
        }
        if (!Notifikasi.konfirmasi("Hapus siswa \"" + dipilih.getNama() + "\"?")) {
            return;
        }
        try {
            dao.hapus(dipilih.getId());
            Notifikasi.info("Data siswa berhasil dihapus.");
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
        tfAlamat.clear();
        dpLahir.setValue(null);
        tfKelas.clear();
        tfUsername.clear();
        pfPassword.clear();
        lblPasswordHint.setText("Minimal 6 karakter");
    }
}
