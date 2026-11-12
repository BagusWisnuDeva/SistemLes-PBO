package com.tk2pbo.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public final class Notifikasi {

    private Notifikasi() { }

    public static void info(String pesan) {
        tampil(Alert.AlertType.INFORMATION, "Informasi", pesan);
    }

    public static void error(String pesan) {
        tampil(Alert.AlertType.ERROR, "Terjadi Kesalahan", pesan);
    }

    public static boolean konfirmasi(String pesan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, pesan, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert.showAndWait().filter(tombol -> tombol == ButtonType.YES).isPresent();
    }

    private static void tampil(Alert.AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe, pesan, ButtonType.OK);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
