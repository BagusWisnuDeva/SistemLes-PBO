package com.tk2pbo.controller;

import java.sql.SQLException;

import com.tk2pbo.dao.AdminDao;
import com.tk2pbo.dao.SiswaDao;
import com.tk2pbo.model.Pengguna;
import com.tk2pbo.util.FormatUtil;
import com.tk2pbo.util.Navigator;
import com.tk2pbo.util.Notifikasi;
import com.tk2pbo.util.Sesi;
import com.tk2pbo.util.SqlErrorUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private ComboBox<String> cmbPeran;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnMasuk;

    private final AdminDao adminDao = new AdminDao();
    private final SiswaDao siswaDao = new SiswaDao();

    @FXML
    private void initialize() {
        cmbPeran.getItems().setAll("Admin", "Siswa");
        cmbPeran.setValue("Admin");
    }

    @FXML
    private void masuk() {
        try {
            boolean sebagaiAdmin = "Admin".equals(cmbPeran.getValue());
            String username = FormatUtil.wajib(txtUsername.getText(), "Username");
            String password = txtPassword.getText();
            FormatUtil.wajib(password, "Password");

            Pengguna pengguna = sebagaiAdmin
                    ? adminDao.login(username, password)
                    : siswaDao.login(username, password);

            if (pengguna == null) {
                Notifikasi.error("Username atau password salah.");
                return;
            }

            Sesi.mulai(pengguna);
            if (sebagaiAdmin) {
                Navigator.tampilAdmin();
            } else {
                Navigator.tampilSiswa();
            }
        } catch (IllegalArgumentException e) {
            Notifikasi.error(e.getMessage());
        } catch (SQLException e) {
            Notifikasi.error(SqlErrorUtil.pesan(e));
        }
    }
}
