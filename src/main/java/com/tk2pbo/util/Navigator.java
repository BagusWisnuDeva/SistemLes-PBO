package com.tk2pbo.util;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class Navigator {

    private static Stage stage;

    private Navigator() { }

    public static void init(Stage jendela) {
        stage = jendela;
    }

    private static Parent muat(String namaFxml) throws IOException {
        URL lokasi = Navigator.class.getResource("/view/" + namaFxml);
        if (lokasi == null) {
            throw new IOException("File tampilan tidak ditemukan: " + namaFxml);
        }
        return FXMLLoader.load(lokasi);
    }

    private static void tampil(String namaFxml, String judul, double lebar, double tinggi) {
        try {
            Rectangle2D layar = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(muat(namaFxml), Math.min(lebar, layar.getWidth()), Math.min(tinggi, layar.getHeight()));
            URL css = Navigator.class.getResource("/css/style.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }
            stage.setTitle(judul);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            Notifikasi.error("Gagal memuat tampilan: " + e.getMessage());
        }
    }

    public static void tampilLogin() {
        tampil("login.fxml", "Sistem Pendaftaran Kursus / Les Privat", 480, 560);
    }

    public static void tampilAdmin() {
        tampil("admin.fxml", "Admin - Sistem Pendaftaran Kursus / Les Privat", 1500, 860);
    }

    public static void tampilSiswa() {
        tampil("siswa-dashboard.fxml", "Siswa - Sistem Pendaftaran Kursus / Les Privat", 1000, 660);
    }

    public static void ganti(BorderPane induk, String namaFxml) {
        try {
            induk.setCenter(muat(namaFxml));
        } catch (IOException e) {
            Notifikasi.error("Gagal memuat halaman: " + e.getMessage());
        }
    }
}
