package com.tk2pbo.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public final class TabelUtil {

    private TabelUtil() { }

    public static <S, T> void kolom(TableColumn<S, T> kolom, Function<S, T> pengambil) {
        kolom.setCellValueFactory(sel -> new SimpleObjectProperty<>(pengambil.apply(sel.getValue())));
    }

    public static <S> void kolomRupiah(TableColumn<S, Double> kolom, Function<S, Double> pengambil) {
        kolom(kolom, pengambil);
        kolom.setCellFactory(k -> new TableCell<S, Double>() {
            @Override
            protected void updateItem(Double nilai, boolean kosong) {
                super.updateItem(nilai, kosong);
                setText(kosong || nilai == null ? null : FormatUtil.rupiah(nilai));
            }
        });
    }

    public static <T> void pasang(TableView<T> tabel, ObservableList<T> data,
                                  TextField cari, Function<T, String> teksCari) {
        FilteredList<T> terfilter = new FilteredList<>(data, x -> true);
        cari.textProperty().addListener((obs, lama, baru) -> {
            String kata = baru == null ? "" : baru.trim().toLowerCase();
            terfilter.setPredicate(x -> kata.isEmpty() || teksCari.apply(x).toLowerCase().contains(kata));
        });
        SortedList<T> terurut = new SortedList<>(terfilter);
        terurut.comparatorProperty().bind(tabel.comparatorProperty());
        tabel.setItems(terurut);
        lebarProporsional(tabel);
    }

    public static void lebarProporsional(TableView<?> tabel) {
        int jumlah = tabel.getColumns().size();
        double[] bobot = new double[jumlah];
        double total = 0;
        for (int i = 0; i < jumlah; i++) {
            bobot[i] = tabel.getColumns().get(i).getPrefWidth();
            total += bobot[i];
        }
        final double jumlahBobot = total;
        Runnable atur = () -> {
            double lebar = tabel.getWidth() - 20; 
            if (lebar <= 0 || jumlahBobot <= 0) {
                return;
            }
            for (int i = 0; i < jumlah; i++) {
                tabel.getColumns().get(i).setPrefWidth(lebar * bobot[i] / jumlahBobot);
            }
        };
        tabel.widthProperty().addListener((obs, lama, baru) -> atur.run());
        atur.run();
    }

    public static void formatTanggal(DatePicker... pemilih) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        StringConverter<LocalDate> konverter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate tanggal) {
                return tanggal == null ? "" : tanggal.format(format);
            }

            @Override
            public LocalDate fromString(String teks) {
                if (teks == null || teks.isBlank()) {
                    return null;
                }
                try {
                    return LocalDate.parse(teks.trim(), format);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        };
        for (DatePicker p : pemilih) {
            p.setConverter(konverter);
            p.setPromptText("dd-mm-yyyy");
        }
    }

    public static <T> void pilih(ComboBox<T> kombo, Predicate<T> kriteria) {
        kombo.setValue(kombo.getItems().stream().filter(kriteria).findFirst().orElse(null));
    }
}
