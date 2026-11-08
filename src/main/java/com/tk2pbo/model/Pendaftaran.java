package com.tk2pbo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tk2pbo.util.FormatUtil;

public class Pendaftaran implements Pembayaranable {
    
    public static final String Belum_bayar = "Belum Bayar";
    public static final String Lunas = "Lunas";

    private int idPendaftaran;
    private Siswa siswa;
    private Kursus kursus;
    private Pengajar pengajar;
    private LocalDate tanggalPendaftaran;
    private int jumlahPertemuan;
    private double totalBiaya;
    private String statusPembayaran;
    private MetodePembayaran metodePembayaran;
    private LocalDateTime tanggalPembayaran;

    public Pendaftaran(Siswa siswa, Kursus kursus, Pengajar pengajar, LocalDate tanggalPendaftaran,
        int jumlahPertemuan) {
        if (siswa == null || kursus == null || pengajar == null) {
            throw new IllegalArgumentException("Siswa, kursus dan pengajar wajib dipilih");
        }

        if (tanggalPendaftaran == null) {
            throw new IllegalArgumentException("Tanggal pendaftaran tidak boleh kosong");
        }
        this.siswa = siswa;
        this.kursus = kursus;
        this.pengajar = pengajar;
        this.tanggalPendaftaran = tanggalPendaftaran;
        setJumlahPertemuan(jumlahPertemuan);
        this.statusPembayaran = Belum_bayar;
    }

    public Pendaftaran(int idPendaftaran, Siswa siswa, Kursus kursus, Pengajar pengajar,
        LocalDate tanggalPendaftaran, int jumlahPertemuan,double totalBiaya, String statusPembayaran,
        MetodePembayaran metodePembayaran, LocalDateTime tanggalPembayaran) {
        this.idPendaftaran = idPendaftaran;
        this.siswa = siswa;
        this.kursus = kursus;
        this.pengajar = pengajar;
        this.tanggalPendaftaran = tanggalPendaftaran;
        this.jumlahPertemuan = jumlahPertemuan;
        this.totalBiaya = totalBiaya;
        this.statusPembayaran = statusPembayaran;
        this.metodePembayaran = metodePembayaran;
        this.tanggalPembayaran = tanggalPembayaran;
    }

    public int getIdPendaftaran() {
        return idPendaftaran;
    }

    public void setIdPendaftaran(int idPendaftaran) {
        this.idPendaftaran = idPendaftaran;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public Kursus getKursus() {
        return kursus;
    }

    public Pengajar getPengajar() {
        return pengajar;
    }

    public LocalDate getTanggalPendaftaran() {
        return tanggalPendaftaran;
    }

    public int getJumlahPertemuan() {
        return jumlahPertemuan;
    }

    public final void setJumlahPertemuan(int jumlahPertemuan) {
        if (jumlahPertemuan <= 0) {
            throw new IllegalArgumentException("Jumlah pertemuan harus lebih besar dari 0.");
        }
        this.jumlahPertemuan = jumlahPertemuan;
        this.totalBiaya = kursus.getBiayaPerPertemuan() * jumlahPertemuan;
    }

    public double getTotalBiaya() {
        return totalBiaya;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public boolean isLunas() {
        return Lunas.equals(statusPembayaran);
    }

    public MetodePembayaran getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(MetodePembayaran metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public LocalDateTime getTanggalPembayaran() {
        return tanggalPembayaran;
    }

    public double hitungBiayaAdmin() {
        return metodePembayaran == null ? 0 : metodePembayaran.hitungBiayaAdmin(totalBiaya);
    }

    @Override
    public double hitungTotalBayar() {
        return totalBiaya + hitungBiayaAdmin();
    }

    @Override
    public void prosesPembayaran() {
        if (isLunas ()) {
            throw new IllegalStateException("Pembayaran sudah lunas.");
        }

        if (metodePembayaran == null) {
            throw new IllegalStateException("Metode pembayaran belum dipilih.");
        }

        this.statusPembayaran = Lunas;
        this.tanggalPembayaran = LocalDateTime.now();
    }

    @Override 
    public String cetakStruk() {
        if (!isLunas()) {
            throw new IllegalStateException("Pembayaran belum lunas.");
        }

        String garis = "=".repeat (44);
        StringBuilder struk = new StringBuilder();
        struk.append(garis).append("\n");
        struk.append("STRUK PEMBAYARAN LES").append("\n");
        struk.append(garis).append("\n");
        struk.append(String.format("No. Pendaftaran : %d\n", idPendaftaran));
        struk.append(String.format("Tanggal Bayar    : %s\n", FormatUtil.formatTanggalJam(tanggalPembayaran)));
        struk.append(String.format("Nama Siswa       : %s\n", siswa.getNama()));
        struk.append(String.format("Kursus           : %s\n", kursus.getNamaKursus()));
        struk.append(String.format("Pengajar         : %s\n", pengajar.getNama()));
        struk.append(String.format("Pertemuan        : %d x %s%n", jumlahPertemuan,
        FormatUtil.rupiah(kursus.getBiayaPerPertemuan())));
        struk.append(String.format("Tagihan Pokok    : %s%n", FormatUtil.rupiah(totalBiaya)));
        struk.append(String.format("Metode           : %s%n", metodePembayaran.getNamaMetode()));
        struk.append(String.format("Biaya Admin      : %s%n", FormatUtil.rupiah(hitungBiayaAdmin())));
        struk.append(String.format("Total Bayar      : %s%n", FormatUtil.rupiah(hitungTotalBayar())));
        struk.append(String.format("Status           : %s%n", statusPembayaran.toUpperCase()));
        struk.append(garis).append("\n");
        struk.append("                        Terima kasih telah melakukan pembayaran!").append("\n");
        return struk.toString();
    }

    public String tampilkanInfo() {
        return "#" + idPendaftaran + " | " + siswa.getNama() + " | " + kursus.getNamaKursus()
                + " | " + pengajar.getNama() + " | " + jumlahPertemuan + " pertemuan | " + statusPembayaran;
    }

    @Override
    public String toString() {
        return "#" + idPendaftaran + " - " + siswa.getNama() + " - " + kursus.getNamaKursus();
    }
}
