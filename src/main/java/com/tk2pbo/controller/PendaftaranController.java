package com.tk2pbo.controller;

import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.tk2pbo.dao.JadwalDao;
import com.tk2pbo.dao.KursusDao;
import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.dao.PengajarDao;
import com.tk2pbo.dao.SiswaDao;
import com.tk2pbo.exception.ValidasiException;
import com.tk2pbo.model.Kursus;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.model.Pengajar;
import com.tk2pbo.model.Siswa;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class PendaftaranController {

    @FXML private TableView<Pendaftaran> tabel;
    @FXML private TableColumn<Pendaftaran, Integer> colId;
    @FXML private TableColumn<Pendaftaran, String> colSiswa;
    @FXML private TableColumn<Pendaftaran, String> colKursus;
    @FXML private TableColumn<Pendaftaran, String> colPengajar;
    @FXML private TableColumn<Pendaftaran, String> colTanggal;
    @FXML private TableColumn<Pendaftaran, Integer> colPertemuan;
    @FXML private TableColumn<Pendaftaran, Double> colTotal;
    @FXML private TableColumn<Pendaftaran, String> colStatus;
    @FXML private TextField tfCari;
    @FXML private ComboBox<Siswa> cbSiswa;
    @FXML private ComboBox<Kursus> cbKursus;
    @FXML private ComboBox<Pengajar> cbPengajar;
    @FXML private DatePicker dpTanggal;
    @FXML private TextField tfPertemuan;
    @FXML private Label lblTotal;

    private final PendaftaranDao dao = new PendaftaranDao();
    private final ObservableList<Pendaftaran> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TabelUtil.formatTanggal(dpTanggal);
        TabelUtil.kolom(colId, Pendaftaran::getIdPendaftaran);
        TabelUtil.kolom(colSiswa, p -> p.getSiswa().getNama());
        TabelUtil.kolom(colKursus, p -> p.getKursus().getNamaKursus());
        TabelUtil.kolom(colPengajar, p -> p.getPengajar().getNama());
        TabelUtil.kolom(colTanggal, p -> FormatUtil.formatTanggal(p.getTanggalPendaftaran()));
        TabelUtil.kolom(colPertemuan, Pendaftaran::getJumlahPertemuan);
        TabelUtil.kolomRupiah(colTotal, Pendaftaran::getTotalBiaya);
        TabelUtil.kolom(colStatus, Pendaftaran::getStatusPembayaran);
        TabelUtil.pasang(tabel, data, tfCari, p -> p.getSiswa().getNama() + " " + p.getKursus().getNamaKursus()
                + " " + p.getPengajar().getNama() + " " + p.getStatusPembayaran());

        dpTanggal.setValue(LocalDate.now());
        cbKursus.valueProperty().addListener((obs, lama, baru) -> perbaruiTotal());
        tfPertemuan.textProperty().addListener((obs, lama, baru) -> perbaruiTotal());
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru != null) {
                isiForm(baru);
            }
        });
        try {
            cbSiswa.setItems(FXCollections.observableArrayList(new SiswaDao().semua()));
            cbKursus.setItems(FXCollections.observableArrayList(new KursusDao().semua()));
            cbPengajar.setItems(FXCollections.observableArrayList(new PengajarDao().semua()));
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

    private void perbaruiTotal() {
        Kursus k = cbKursus.getValue();
        try {
            int jumlah = Integer.parseInt(tfPertemuan.getText().trim());
            if (k != null && jumlah > 0) {
                lblTotal.setText("Total biaya: " + FormatUtil.rupiah(k.getBiayaPerPertemuan() * jumlah)
                        + "  (" + jumlah + " x " + FormatUtil.rupiah(k.getBiayaPerPertemuan()) + ")");
                return;
            }
        } catch (NumberFormatException e) {
        }
        lblTotal.setText("Total biaya: Rp 0");
    }

    private void isiForm(Pendaftaran p) {
        TabelUtil.pilih(cbSiswa, s -> s.getId() == p.getSiswa().getId());
        TabelUtil.pilih(cbKursus, k -> k.getIdKursus() == p.getKursus().getIdKursus());
        TabelUtil.pilih(cbPengajar, g -> g.getId() == p.getPengajar().getId());
        dpTanggal.setValue(p.getTanggalPendaftaran());
        tfPertemuan.setText(String.valueOf(p.getJumlahPertemuan()));
    }

    @FXML
    private void tambah() {
        try {
            Pendaftaran baru = new Pendaftaran(cbSiswa.getValue(), cbKursus.getValue(), cbPengajar.getValue(),
                    dpTanggal.getValue(), FormatUtil.parseInt(tfPertemuan.getText(), "Jumlah pertemuan"));
            boolean kembar = data.stream().anyMatch(p -> !p.isLunas()
                    && p.getSiswa().getId() == baru.getSiswa().getId()
                    && p.getKursus().getIdKursus() == baru.getKursus().getIdKursus());
            if (kembar && !Notifikasi.konfirmasi("Siswa ini sudah punya pendaftaran kursus yang sama yang belum "
                    + "dibayar. Tetap tambahkan pendaftaran baru?")) {
                return;
            }
            dao.tambah(baru);
            Notifikasi.info("Pendaftaran berhasil disimpan dengan total biaya "
                    + FormatUtil.rupiah(baru.getTotalBiaya()) + ".");
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
        Pendaftaran dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih pendaftaran pada tabel terlebih dahulu.");
            return;
        }
        if (dipilih.isLunas()) {
            Notifikasi.error("Pendaftaran yang sudah lunas tidak dapat diubah.");
            return;
        }
        Siswa siswaForm = cbSiswa.getValue();
        if (siswaForm == null || siswaForm.getId() != dipilih.getSiswa().getId()) {
            Notifikasi.error("Siswa tidak dapat diganti. Batalkan pendaftaran ini lalu daftarkan ulang.");
            return;
        }
        try {
            Kursus kursus = cbKursus.getValue();
            Pengajar pengajar = cbPengajar.getValue();
            if (kursus == null || pengajar == null) {
                throw new IllegalArgumentException("Kursus dan pengajar wajib dipilih");
            }
            int jumlah = FormatUtil.parseInt(tfPertemuan.getText(), "Jumlah pertemuan");
            if (jumlah < 1) {
                throw new IllegalArgumentException("Jumlah pertemuan minimal 1");
            }
            if (pengajar.getId() != dipilih.getPengajar().getId()
                    && new JadwalDao().hitungByPendaftaran(dipilih.getIdPendaftaran()) > 0) {
                throw new IllegalArgumentException("Pengajar tidak dapat diganti karena pendaftaran ini sudah punya "
                        + "jadwal. Hapus jadwalnya terlebih dahulu di menu Jadwal.");
            }
            Pendaftaran diubah = new Pendaftaran(dipilih.getIdPendaftaran(), dipilih.getSiswa(), kursus, pengajar,
                    dipilih.getTanggalPendaftaran(), jumlah, kursus.getBiayaPerPertemuan() * jumlah,
                    dipilih.getStatusPembayaran(), dipilih.getMetodePembayaran(), dipilih.getTanggalPembayaran());
            dao.ubah(diubah);
            Notifikasi.info("Pendaftaran berhasil diubah.");
            bersihkan();
            muatData();
        } catch (IllegalArgumentException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }

    @FXML
    private void batalkan() {
        Pendaftaran dipilih = tabel.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            Notifikasi.error("Pilih pendaftaran pada tabel terlebih dahulu.");
            return;
        }
        if (!Notifikasi.konfirmasi("Batalkan pendaftaran " + dipilih + "? Jadwal terkait ikut dihapus.")) {
            return;
        }
        try {
            dao.hapus(dipilih.getIdPendaftaran());
            Notifikasi.info("Pendaftaran berhasil dibatalkan.");
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
        cbSiswa.setValue(null);
        cbKursus.setValue(null);
        cbPengajar.setValue(null);
        dpTanggal.setValue(LocalDate.now());
        tfPertemuan.clear();
    }
}
