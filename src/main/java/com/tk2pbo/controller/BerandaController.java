package com.tk2pbo.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.tk2pbo.dao.KursusDao;
import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.dao.PengajarDao;
import com.tk2pbo.dao.SiswaDao;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.SqlErrorUtil;
import com.tk2pbo.util.TabelUtil;

public class BerandaController {

    @FXML private Label lblSiswa;
    @FXML private Label lblPengajar;
    @FXML private Label lblKursus;
    @FXML private Label lblPendaftaran;
    @FXML private Label lblPendapatan;
    @FXML private Label lblTunggakan;
    @FXML private TableView<Pendaftaran> tabelBelumBayar;
    @FXML private TableColumn<Pendaftaran, Integer> colId;
    @FXML private TableColumn<Pendaftaran, String> colSiswa;
    @FXML private TableColumn<Pendaftaran, String> colKursus;
    @FXML private TableColumn<Pendaftaran, String> colTanggal;
    @FXML private TableColumn<Pendaftaran, Double> colTotal;

    @FXML
    private void initialize() {
        TabelUtil.kolom(colId, Pendaftaran::getIdPendaftaran);
        TabelUtil.kolom(colSiswa, p -> p.getSiswa().getNama());
        TabelUtil.kolom(colKursus, p -> p.getKursus().getNamaKursus());
        TabelUtil.kolom(colTanggal, p -> FormatUtil.formatTanggal(p.getTanggalPendaftaran()));
        TabelUtil.kolomRupiah(colTotal, Pendaftaran::getTotalBiaya);
        TabelUtil.lebarProporsional(tabelBelumBayar);
        try {
            List<Pendaftaran> semua = new PendaftaranDao().semua();
            lblSiswa.setText(String.valueOf(new SiswaDao().semua().size()));
            lblPengajar.setText(String.valueOf(new PengajarDao().semua().size()));
            lblKursus.setText(String.valueOf(new KursusDao().semua().size()));
            lblPendaftaran.setText(String.valueOf(semua.size()));

            double pendapatan = semua.stream().filter(Pendaftaran::isLunas)
                    .mapToDouble(Pendaftaran::getTotalBiaya).sum();
            List<Pendaftaran> belumBayar = semua.stream().filter(p -> !p.isLunas()).collect(Collectors.toList());
            double tunggakan = belumBayar.stream().mapToDouble(Pendaftaran::getTotalBiaya).sum();
            lblPendapatan.setText(FormatUtil.rupiah(pendapatan));
            lblTunggakan.setText(FormatUtil.rupiah(tunggakan));
            tabelBelumBayar.setItems(FXCollections.observableArrayList(belumBayar));
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }
}
