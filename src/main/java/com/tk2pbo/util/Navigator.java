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

    private static final double LEBAR = 1280;
    private static final double TINGGI = 720;

    private static Stage stage;
    private static Scene scene;

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

    private static void tampil(String namaFxml, String judul) {
        try {
            Parent root = muat(namaFxml);

            if (scene == null) {
                Rectangle2D layar = Screen.getPrimary().getVisualBounds();
                double lebar = Math.min(LEBAR, layar.getWidth());
                double tinggi = Math.min(TINGGI, layar.getHeight());

                scene = new Scene(root, lebar, tinggi);
                URL css = Navigator.class.getResource("/css/style.css");
                if (css != null) {
                    scene.getStylesheets().add(css.toExternalForm());
                }
                stage.setScene(scene);
                stage.setMinWidth(lebar);
                stage.setMinHeight(tinggi);
                stage.setMaximized(true);
                stage.show();
            } else {
                scene.setRoot(root);
            }
            stage.setTitle(judul);
        } catch (IOException e) {
            Notifikasi.error("Gagal memuat tampilan: " + e.getMessage());
        }
    }

    public static void tampilLogin() {
        tampil("login.fxml", "Sistem Pendaftaran Kursus / Les Privat");
    }

    public static void tampilAdmin() {
        tampil("admin.fxml", "Admin - Sistem Pendaftaran Kursus / Les Privat");
    }

    public static void tampilSiswa() {
        tampil("siswa-dashboard.fxml", "Siswa - Sistem Pendaftaran Kursus / Les Privat");
    }

    public static void ganti(BorderPane induk, String namaFxml) {
        try {
            induk.setCenter(muat(namaFxml));
        } catch (IOException e) {
            Notifikasi.error("Gagal memuat halaman: " + e.getMessage());
        }
    }
}