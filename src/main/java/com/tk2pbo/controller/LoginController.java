package com.tk2pbo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField enterpassword;

    @FXML
    private Button loginButton;

    @FXML
    private Button daftarakunButton;

    @FXML
    private Label invalidLabel;

    @FXML
    private void handleLogin() {

        String user = username.getText().trim();
        String password = enterpassword.getText();

        if (user.isEmpty() || password.isEmpty()) {
            invalidLabel.setText("Username dan password harus diisi!");
            return;
        }

        invalidLabel.setText("Memeriksa akun...");
    }

    @FXML
    private void handleDaftarAkun() {
        System.out.println("Tombol Daftar Akun diklik!");
    }
}
