package com.tk2pbo.laporan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tk2pbo.dao.JadwalDao;
import com.tk2pbo.dao.PengajarDao;
import com.tk2pbo.model.Jadwal;
import com.tk2pbo.model.Pengajar;
import com.tk2pbo.util.FormatUtil;

public class LaporanJadwalPengajar extends Laporan {

    private final Pengajar pengajar;
    private final JadwalDao jadwalDao = new JadwalDao();
    private final PengajarDao pengajarDao = new PengajarDao();
    private double totalJam;
    private double totalHonor;

    public LaporanJadwalPengajar(Pengajar pengajar) {
        this.pengajar = pengajar;
    }

    @Override
    public String getJudul() {
        return pengajar == null ? "Laporan Jadwal Pengajar - Semua Pengajar"
                : "Laporan Jadwal Pengajar - " + pengajar.getNama();
    }

    @Override
    public List<String> getKolom() {
        return List.of("Pengajar", "Hari", "Jam", "Ruangan", "Kursus", "Siswa", "Durasi (jam)");
    }

    @Override
    protected List<List<String>> ambilData() throws SQLException {
        List<Pengajar> daftarPengajar = pengajar == null ? pengajarDao.semua() : List.of(pengajar);
        List<List<String>> hasil = new ArrayList<>();
        totalJam = 0;
        totalHonor = 0;
        for (Pengajar p : daftarPengajar) {
            double jamPengajar = 0;
            for (Jadwal j : jadwalDao.byPengajar(p.getId())) {
                double jam = j.getDurasiMenit() / 60.0;
                jamPengajar += jam;
                hasil.add(List.of(
                        p.getNama(),
                        j.getHari(),
                        j.getWaktuTeks(),
                        j.getRuangan(),
                        j.getPendaftaran().getKursus().getNamaKursus(),
                        j.getPendaftaran().getSiswa().getNama(),
                        String.format("%.1f", jam)));
            }
            totalJam += jamPengajar;
            totalHonor += p.hitungHonor(jamPengajar);
        }
        return hasil;
    }

    @Override
    public String getRingkasan() {
        return String.format("%d jadwal | Total %.1f jam/minggu | Estimasi honor per minggu: %s",
                data.size(), totalJam, FormatUtil.rupiah(totalHonor));
    }
}
