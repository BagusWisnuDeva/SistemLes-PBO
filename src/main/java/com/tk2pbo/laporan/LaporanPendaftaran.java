package com.tk2pbo.laporan;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.tk2pbo.dao.PendaftaranDao;
import com.tk2pbo.model.Kursus;
import com.tk2pbo.model.Pendaftaran;
import com.tk2pbo.util.FormatUtil;

public class LaporanPendaftaran extends Laporan {

    private final Kursus kursus; // null = semua kursus
    private final PendaftaranDao dao = new PendaftaranDao();

    public LaporanPendaftaran(Kursus kursus) {
        this.kursus = kursus;
    }

    @Override
    public String getJudul() {
        return kursus == null ? "Laporan Pendaftaran - Semua Kursus" : "Laporan Pendaftaran - " + kursus.getNamaKursus();
    }

    @Override
    public List<String> getKolom() {
        return List.of("ID", "Siswa", "Kursus", "Pengajar", "Tgl Daftar", "Pertemuan", "Total Biaya", "Status");
    }

    @Override
    protected List<List<String>> ambilData() throws SQLException {
        List<Pendaftaran> daftar = kursus == null ? dao.semua() : dao.byKursus(kursus.getIdKursus());
        return daftar.stream()
                .map(p -> List.of(
                        String.valueOf(p.getIdPendaftaran()),
                        p.getSiswa().getNama(),
                        p.getKursus().getNamaKursus(),
                        p.getPengajar().getNama(),
                        FormatUtil.formatTanggal(p.getTanggalPendaftaran()),
                        String.valueOf(p.getJumlahPertemuan()),
                        FormatUtil.rupiah(p.getTotalBiaya()),
                        p.getStatusPembayaran()))
                .collect(Collectors.toList());
    }

    @Override
    public String getRingkasan() {
        return data.size() + " pendaftaran";
    }
}
