package com.tk2pbo.laporan;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.util.FormatUtil;

public class LaporanPendapatan extends Laporan {

    private final LocalDate dari;
    private final LocalDate sampai;
    private final PendaftaranDao dao = new PendaftaranDao();
    private double totalPendapatan;

    public LaporanPendapatan(LocalDate dari, LocalDate sampai) {
        if (dari == null || sampai == null) {
            throw new IllegalArgumentException("Tanggal awal dan akhir wajib diisi");
        }
        if (sampai.isBefore(dari)) {
            throw new IllegalArgumentException("Tanggal akhir tidak boleh sebelum tanggal awal");
        }
        this.dari = dari;
        this.sampai = sampai;
    }

    @Override
    public String getJudul() {
        return "Laporan Pendapatan " + FormatUtil.formatTanggal(dari) + " s.d. " + FormatUtil.formatTanggal(sampai);
    }

    @Override
    public List<String> getKolom() {
        return List.of("Tgl Bayar", "ID", "Siswa", "Kursus", "Metode", "Pendapatan");
    }

    @Override
    protected List<List<String>> ambilData() throws SQLException {
        List<Pendaftaran> lunas = dao.lunasAntara(dari, sampai);
        totalPendapatan = lunas.stream().mapToDouble(Pendaftaran::getTotalBiaya).sum();
        return lunas.stream()
                .map(p -> List.of(
                        FormatUtil.formatTanggalJam(p.getTanggalPembayaran()),
                        String.valueOf(p.getIdPendaftaran()),
                        p.getSiswa().getNama(),
                        p.getKursus().getNamaKursus(),
                        p.getMetodePembayaran() == null ? "-" : p.getMetodePembayaran().getNamaMetode(),
                        FormatUtil.rupiah(p.getTotalBiaya())))
                .collect(Collectors.toList());
    }

    @Override
    public String getRingkasan() {
        return "Total pendapatan: " + FormatUtil.rupiah(totalPendapatan) + " dari " + data.size() + " transaksi";
    }
}
