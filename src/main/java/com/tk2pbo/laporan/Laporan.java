package com.tk2pbo.laporan;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Laporan {

    protected List<List<String>> data = List.of();

    public abstract String getJudul();

    public abstract List<String> getKolom();

    protected abstract List<List<String>> ambilData() throws SQLException;

    public String getRingkasan() {
        return data.size() + " baris data";
    }

    public final List<List<String>> muat() throws SQLException {
        data = ambilData();
        return data;
    }

    public final String keCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(getKolom().stream().map(Laporan::escape).collect(Collectors.joining(","))).append('\n');
        for (List<String> baris : data) {
            sb.append(baris.stream().map(Laporan::escape).collect(Collectors.joining(","))).append('\n');
        }
        return sb.toString();
    }

    private static String escape(String teks) {
        String t = teks == null ? "" : teks;
        if (t.contains(",") || t.contains("\"") || t.contains("\n")) {
            return "\"" + t.replace("\"", "\"\"") + "\"";
        }
        return t;
    }
}
