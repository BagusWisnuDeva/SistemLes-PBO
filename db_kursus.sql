
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS db_kursus
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_kursus;

DROP TABLE IF EXISTS jadwal;
DROP TABLE IF EXISTS pendaftaran;
DROP TABLE IF EXISTS kursus;
DROP TABLE IF EXISTS pengajar;
DROP TABLE IF EXISTS siswa;
DROP TABLE IF EXISTS admin;

CREATE TABLE `admin` (
  `id_admin` int NOT NULL,
  `nama` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `no_telepon` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `admin` (`id_admin`, `nama`, `email`, `no_telepon`, `username`, `password`) VALUES
(1, 'Administrator Utama', 'adminkursus@gmail.com', '081234567890', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

CREATE TABLE `jadwal` (
  `id_jadwal` int NOT NULL,
  `id_pendaftaran` int NOT NULL,
  `hari` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `ruangan` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `jadwal` (`id_jadwal`, `id_pendaftaran`, `hari`, `jam_mulai`, `jam_selesai`, `ruangan`) VALUES
(1, 1, 'Senin', '16:00:00', '17:30:00', 'Ruang 1.1'),
(2, 1, 'Rabu', '16:00:00', '17:30:00', 'Ruang 1.2'),
(3, 2, 'Sabtu', '09:00:00', '11:00:00', 'Lab Komputer 1');

CREATE TABLE `kursus` (
  `id_kursus` int NOT NULL,
  `nama_kursus` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `deskripsi` text COLLATE utf8mb4_unicode_ci,
  `biaya_per_pertemuan` decimal(12,2) NOT NULL DEFAULT '0.00',
  `durasi_menit` int NOT NULL DEFAULT '60'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `kursus` (`id_kursus`, `nama_kursus`, `deskripsi`, `biaya_per_pertemuan`, `durasi_menit`) VALUES
(1, 'Matematika Dasar', 'Les matematika untuk SMP', 60000.00, 90),
(2, 'English Conversation', 'Kelas percakapan bahasa Inggris', 65000.00, 90),
(3, 'Java Programming', 'Belajar OOP dengan Java', 75000.00, 120);

CREATE TABLE `pendaftaran` (
  `id_pendaftaran` int NOT NULL,
  `id_siswa` int NOT NULL,
  `id_kursus` int NOT NULL,
  `id_pengajar` int NOT NULL,
  `tanggal_daftar` date NOT NULL,
  `jumlah_pertemuan` int NOT NULL DEFAULT '1',
  `total_biaya` decimal(12,2) NOT NULL DEFAULT '0.00',
  `status_pembayaran` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'Belum Bayar',
  `metode_pembayaran` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tanggal_bayar` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `pendaftaran` (`id_pendaftaran`, `id_siswa`, `id_kursus`, `id_pengajar`, `tanggal_daftar`, `jumlah_pertemuan`, `total_biaya`, `status_pembayaran`, `metode_pembayaran`, `tanggal_bayar`) VALUES
(1, 1, 1, 1, '2026-11-13', 8, 480000.00, 'Lunas', 'Transfer', '2026-11-13 19:17:45'),
(2, 2, 3, 3, '2026-11-13', 12, 900000.00, 'Belum Bayar', NULL, NULL);

CREATE TABLE `pengajar` (
  `id_pengajar` int NOT NULL,
  `nama` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `no_telepon` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `spesialisasi` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tarif_per_jam` decimal(12,2) DEFAULT '0.00',
  `pengalaman` int DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `pengajar` (`id_pengajar`, `nama`, `email`, `no_telepon`, `spesialisasi`, `tarif_per_jam`, `pengalaman`) VALUES
(1, 'Bagus Wisnu', 'baguswisnu@gmail.com', '081255556666', 'Matematika', 90000.00, 5),
(2, 'Surya Dinata', 'suryadinata@gmail.com', '081277778888', 'Bahasa Inggris', 80000.00, 3),
(3, 'Bagus Angga', 'bagusangga@gmail.com', '081299990000', 'Pemrograman Java', 100000.00, 8),
(4, 'Lutfi Hanafi', 'lutfihanafi@gmail.com', '08128888000', 'Ekonomi', 75000.00, 4);

CREATE TABLE `siswa` (
  `id_siswa` int NOT NULL,
  `nama` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `no_telepon` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `alamat` text COLLATE utf8mb4_unicode_ci,
  `tanggal_lahir` date DEFAULT NULL,
  `kelas` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `siswa` (`id_siswa`, `nama`, `email`, `no_telepon`, `alamat`, `tanggal_lahir`, `kelas`, `username`, `password`) VALUES
(1, 'Surya Wisnu', 'suryawisnu@gmail.com', '081211112222', 'Denpasar, Bali', '2005-03-14', 'XI IPA 2', 'surya_wisnu', 'b824b3bc8ffb125b377cfa44ab5274df0ebdf9fa21d1b934f1dfc014595f00bc'),
(2, 'Bagus Lutfi', 'baguslutfi@gmail.com', '081233334444', 'Badung, Bali', '2004-08-21', 'XII IPS 1', 'bagus_lutfi', '743b3c9e6b4a11e78c1df30f7a120b71e5cb5dbbf4c5a1a7f46024f02b3a69c4');

ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`),
  ADD UNIQUE KEY `username` (`username`);

ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `fk_jadwal_pendaftaran` (`id_pendaftaran`);

ALTER TABLE `kursus`
  ADD PRIMARY KEY (`id_kursus`);

ALTER TABLE `pendaftaran`
  ADD PRIMARY KEY (`id_pendaftaran`),
  ADD KEY `fk_daftar_siswa` (`id_siswa`),
  ADD KEY `fk_daftar_kursus` (`id_kursus`),
  ADD KEY `fk_daftar_pengajar` (`id_pengajar`);

ALTER TABLE `pengajar`
  ADD PRIMARY KEY (`id_pengajar`);

ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id_siswa`),
  ADD UNIQUE KEY `username` (`username`);

ALTER TABLE `admin`
  MODIFY `id_admin` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

ALTER TABLE `jadwal`
  MODIFY `id_jadwal` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `kursus`
  MODIFY `id_kursus` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `pendaftaran`
  MODIFY `id_pendaftaran` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

ALTER TABLE `pengajar`
  MODIFY `id_pengajar` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `siswa`
  MODIFY `id_siswa` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

ALTER TABLE `jadwal`
  ADD CONSTRAINT `fk_jadwal_pendaftaran` FOREIGN KEY (`id_pendaftaran`) REFERENCES `pendaftaran` (`id_pendaftaran`) ON DELETE CASCADE;

ALTER TABLE `pendaftaran`
  ADD CONSTRAINT `fk_daftar_kursus` FOREIGN KEY (`id_kursus`) REFERENCES `kursus` (`id_kursus`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_daftar_pengajar` FOREIGN KEY (`id_pengajar`) REFERENCES `pengajar` (`id_pengajar`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_daftar_siswa` FOREIGN KEY (`id_siswa`) REFERENCES `siswa` (`id_siswa`) ON DELETE CASCADE;
COMMIT;
