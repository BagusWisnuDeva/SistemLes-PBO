package com.tk2pbo.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class FormatUtil {

    private static final DateTimeFormatter Tanggal = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter Tanggal_Jam = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final DateTimeFormatter Jam = DateTimeFormatter.ofPattern("HH:mm");

    private FormatUtil() {}

    public static String rupiah(double nilai) {
        DecimalFormatSymbols simbol = new DecimalFormatSymbols(Locale.ROOT);
        simbol.setGroupingSeparator('.');
        simbol.setDecimalSeparator(',');
        return new DecimalFormat("Rp #,##0.00", simbol).format(nilai);
    }

    public static String formatTanggal(LocalDate tanggal) {
        return tanggal==null? "-" : tanggal.format(Tanggal);
    }

    public static String formatTanggalJam(LocalDateTime tanggalJam) {
        return tanggalJam==null? "-" : tanggalJam.format(Tanggal_Jam);
    }

    public static String formatJam(LocalTime jam) {
        return jam==null? "-" : jam.format(Jam);
    }

    public static LocalTime parseJam(String teks, String namaField) {
        try {
            return LocalTime.parse(teks==null?"-": teks.trim(), Jam);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(namaField + " tidak valid. Harus Berformat HH:mm. Contoh: 08:30");
        }
    }

    public static int parseInt(String teks, String namaField) {
        try {
            return Integer.parseInt(teks==null?"-": teks.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(namaField + " tidak valid. Harus berupa angka bulat.");
        }
    }

    public static double parseDouble(String teks, String namaField) {
        try {
            return Double.parseDouble(teks==null?"-": teks.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(namaField + " tidak valid. Harus berupa angka desimal.");
        }
    }

    public static String wajib(String teks, String namaField) {
        if (teks==null || teks.trim().isEmpty()) {
            throw new IllegalArgumentException(namaField + " tidak boleh kosong.");
        }
        return teks.trim();
    }

    public static String kosongjadinull(String teks) {
        if (teks==null || teks.trim().isEmpty()) {
            return null;
        }
        return teks.trim();
    }

}
