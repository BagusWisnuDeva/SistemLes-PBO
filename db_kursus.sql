-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 19 Nov 2026 pada 11.01
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_kursus`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id_admin`, `nama`, `email`, `no_telepon`, `username`, `password`) VALUES
(1, 'Administrator Utama', 'admin@kursus.local', '081234567890', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal`
--

CREATE TABLE `jadwal` (
  `id_jadwal` int(11) NOT NULL,
  `id_pendaftaran` int(11) NOT NULL,
  `hari` varchar(20) NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `ruangan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `jadwal`
--

INSERT INTO `jadwal` (`id_jadwal`, `id_pendaftaran`, `hari`, `jam_mulai`, `jam_selesai`, `ruangan`) VALUES
(9, 3, 'Senin', '16:00:00', '17:30:00', 'Ruang A'),
(10, 3, 'Rabu', '16:00:00', '17:30:00', 'Ruang A'),
(11, 4, 'Sabtu', '09:00:00', '11:00:00', 'Lab Komputer'),
(12, 5, 'Selasa', '15:00:00', '16:30:00', 'Ruang B'),
(13, 6, 'Kamis', '16:00:00', '17:30:00', 'Ruang A');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kursus`
--

CREATE TABLE `kursus` (
  `id_kursus` int(11) NOT NULL,
  `nama_kursus` varchar(100) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `biaya_per_pertemuan` decimal(12,2) NOT NULL DEFAULT 0.00,
  `durasi_menit` int(11) NOT NULL DEFAULT 60
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `kursus`
--

INSERT INTO `kursus` (`id_kursus`, `nama_kursus`, `deskripsi`, `biaya_per_pertemuan`, `durasi_menit`) VALUES
(1, 'Matematika Dasar', 'Les matematika untuk SMP', 60000.00, 90),
(2, 'English Conversation', 'Kelas percakapan bahasa Inggris', 65000.00, 90),
(3, 'Java Programming', 'Belajar OOP dengan Java', 75000.00, 120);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pendaftaran`
--

CREATE TABLE `pendaftaran` (
  `id_pendaftaran` int(11) NOT NULL,
  `id_siswa` int(11) NOT NULL,
  `id_kursus` int(11) NOT NULL,
  `id_pengajar` int(11) NOT NULL,
  `tanggal_daftar` date NOT NULL,
  `jumlah_pertemuan` int(11) NOT NULL DEFAULT 1,
  `total_biaya` decimal(12,2) NOT NULL DEFAULT 0.00,
  `status_pembayaran` varchar(50) DEFAULT 'Belum Bayar',
  `metode_pembayaran` varchar(50) DEFAULT NULL,
  `tanggal_bayar` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `pendaftaran`
--

INSERT INTO `pendaftaran` (`id_pendaftaran`, `id_siswa`, `id_kursus`, `id_pengajar`, `tanggal_daftar`, `jumlah_pertemuan`, `total_biaya`, `status_pembayaran`, `metode_pembayaran`, `tanggal_bayar`) VALUES
(3, 3, 1, 1, '2026-11-10', 8, 480000.00, 'Lunas', 'Transfer', '2026-11-11 10:00:00'),
(4, 4, 3, 3, '2026-11-15', 12, 900000.00, 'Belum Bayar', NULL, NULL),
(5, 5, 2, 2, '2026-11-12', 10, 650000.00, 'Lunas', 'Tunai', '2026-11-12 14:30:00'),
(6, 6, 1, 1, '2026-11-14', 8, 480000.00, 'Lunas', 'QRIS', '2026-11-14 09:15:00'),
(7, 3, 1, 1, '2026-11-10', 8, 480000.00, 'Lunas', 'Transfer', '2026-11-11 10:00:00'),
(8, 4, 3, 3, '2026-11-15', 12, 900000.00, 'Belum Bayar', NULL, NULL),
(9, 5, 2, 2, '2026-11-12', 10, 650000.00, 'Lunas', 'Tunai', '2026-11-12 14:30:00'),
(10, 6, 1, 1, '2026-11-14', 8, 480000.00, 'Lunas', 'QRIS', '2026-11-14 09:15:00');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengajar`
--

CREATE TABLE `pengajar` (
  `id_pengajar` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `spesialisasi` varchar(100) DEFAULT NULL,
  `tarif_per_jam` decimal(12,2) DEFAULT 0.00,
  `pengalaman` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `pengajar`
--

INSERT INTO `pengajar` (`id_pengajar`, `nama`, `email`, `no_telepon`, `spesialisasi`, `tarif_per_jam`, `pengalaman`) VALUES
(1, 'I Made Surya', 'surya@gmail.com', '081255556666', 'Matematika', 75000.00, 5),
(2, 'Ni Putu Dewi', 'dewi@gmail.com', '081277778888', 'Bahasa Inggris', 80000.00, 3),
(3, 'I Wayan Agus', 'agus@gmail.com', '081299990000', 'Pemrograman Java', 100000.00, 8);

-- --------------------------------------------------------

--
-- Struktur dari tabel `siswa`
--

CREATE TABLE `siswa` (
  `id_siswa` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `kelas` varchar(50) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `siswa`
--

INSERT INTO `siswa` (`id_siswa`, `nama`, `email`, `no_telepon`, `alamat`, `tanggal_lahir`, `kelas`, `username`, `password`) VALUES
(3, 'Surya', 'surya@gmail.com', '081200000001', 'Denpasar, Bali', '2005-01-01', 'Kelas A', 'surya_001', '9464fb01de62761d5691f5eb89e80aa1e40a84bea6ee5d2ab5ca90efd9776b47'),
(4, 'Wisnu', 'wisnu@gmail.com', '081200000002', 'Denpasar, Bali', '2005-01-01', 'Kelas A', 'wisnu_005', '9464fb01de62761d5691f5eb89e80aa1e40a84bea6ee5d2ab5ca90efd9776b47'),
(5, 'Angga', 'angga@gmail.com', '081200000003', 'Denpasar, Bali', '2005-01-01', 'Kelas B', 'angga_010', '9464fb01de62761d5691f5eb89e80aa1e40a84bea6ee5d2ab5ca90efd9776b47'),
(6, 'Luthfi', 'luthfi@gmail.com', '081200000004', 'Denpasar, Bali', '2005-01-01', 'Kelas B', 'luthfi_148', '9464fb01de62761d5691f5eb89e80aa1e40a84bea6ee5d2ab5ca90efd9776b47');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indeks untuk tabel `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `fk_jadwal_pendaftaran` (`id_pendaftaran`);

--
-- Indeks untuk tabel `kursus`
--
ALTER TABLE `kursus`
  ADD PRIMARY KEY (`id_kursus`);

--
-- Indeks untuk tabel `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD PRIMARY KEY (`id_pendaftaran`),
  ADD KEY `fk_daftar_siswa` (`id_siswa`),
  ADD KEY `fk_daftar_kursus` (`id_kursus`),
  ADD KEY `fk_daftar_pengajar` (`id_pengajar`);

--
-- Indeks untuk tabel `pengajar`
--
ALTER TABLE `pengajar`
  ADD PRIMARY KEY (`id_pengajar`);

--
-- Indeks untuk tabel `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id_siswa`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `jadwal`
--
ALTER TABLE `jadwal`
  MODIFY `id_jadwal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT untuk tabel `kursus`
--
ALTER TABLE `kursus`
  MODIFY `id_kursus` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `pendaftaran`
--
ALTER TABLE `pendaftaran`
  MODIFY `id_pendaftaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `pengajar`
--
ALTER TABLE `pengajar`
  MODIFY `id_pengajar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `siswa`
--
ALTER TABLE `siswa`
  MODIFY `id_siswa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `fk_jadwal_pendaftaran` FOREIGN KEY (`id_pendaftaran`) REFERENCES `pendaftaran` (`id_pendaftaran`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD CONSTRAINT `fk_daftar_kursus` FOREIGN KEY (`id_kursus`) REFERENCES `kursus` (`id_kursus`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_daftar_pengajar` FOREIGN KEY (`id_pengajar`) REFERENCES `pengajar` (`id_pengajar`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_daftar_siswa` FOREIGN KEY (`id_siswa`) REFERENCES `siswa` (`id_siswa`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
