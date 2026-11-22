==============================================================
 SISTEM PENDAFTARAN KURSUS / LES PRIVAT
 Tugas Kelompok 2 - Pemrograman Berorientasi Objek (PBO)
==============================================================
 
DESKRIPSI
---------
Aplikasi desktop berbasis Java (JavaFX) untuk mengelola pendaftaran
kursus / les privat: data siswa, pengajar, kursus, pendaftaran,
penjadwalan, pembayaran, dan laporan. Data disimpan di database MySQL
(db_kursus).
 
Aplikasi memiliki dua peran:
  - Admin : mengelola seluruh data dan transaksi.
  - Siswa : melihat jadwal dan riwayat pendaftarannya sendiri.
 
FITUR UTAMA
-----------
Admin
  1. Beranda           - ringkasan data
  2. Data Siswa        - tambah, ubah, hapus, cari
  3. Data Pengajar     - tambah, ubah, hapus, cari
  4. Data Kursus       - tambah, ubah, hapus, cari
  5. Pendaftaran       - mendaftarkan siswa ke kursus + pengajar, total biaya dihitung otomatis
  6. Jadwal            - atur hari, jam, dan ruangan; sistem mendeteksi jadwal bentrok
  7. Pembayaran        - metode Tunai, Transfer, QRIS, Kartu Kredit, lengkap dengan struk pembayaran
  8. Laporan           - laporan pendaftaran, jadwal pengajar, dan pendapatan (ada filter, bisa diekspor CSV)
 
Siswa
  1. Jadwal Saya
  2. Riwayat Pendaftaran
 
Penerapan konsep PBO
  - Pewarisan      : Pengguna -> Admin, Siswa, Pengajar
  - Interface      : Pembayaranable (Pendaftaran), CrudDao<T>
  - Polimorfisme   : MetodePembayaran -> PembayaranTunai, PembayaranTransfer, PembayaranQris, PembayaranKartuKredit
  - Abstraksi      : Laporan -> LaporanPendaftaran, LaporanJadwalPengajar, LaporanPendapatan
  - Enkapsulasi    : atribut private + getter/setter pada paket model
  - Exception      : ValidasiException, JadwalBentrokException
  - Pola MVC + DAO : model / view (FXML) / controller / dao
 
TEKNOLOGI
---------
  - Java 11 atau lebih baru (JDK)
  - JavaFX 13 (javafx-controls, javafx-fxml)
  - MySQL / MariaDB (dump dibuat dengan phpMyAdmin / XAMPP)
  - MySQL Connector/J 8.4.0
  - Apache Maven 3.10.0
         
STRUKTUR FOLDER
---------------
  SistemLes-PBO/
  |-- pom.xml                  konfigurasi Maven
  |-- db_kursus.sql            dump database (struktur + data contoh)
  |-- db.properties.example    contoh konfigurasi koneksi database
  |-- src/main/java/com/tk2pbo/
  |     |-- App.java, Launcher.java   titik masuk aplikasi
  |     |-- model/                    kelas entitas & pembayaran
  |     |-- dao/                      akses database (CRUD)
  |     |-- controller/               pengendali tampilan FXML
  |     |-- laporan/                  kelas laporan
  |     |-- exception/                exception khusus
  |     |-- util/                     koneksi DB, sesi, hash password,
  |                                   navigasi, format, notifikasi
  |-- src/main/resources/
        |-- view/                     file tampilan (.fxml)
        |-- css/style.css             gaya tampilan
 
 
CARA MENJALANKAN
----------------
1. Siapkan prasyarat
   - Pasang JDK 11+ dan Maven, lalu pastikan "java -version" dan
     "mvn -version" bisa dijalankan.
   - Jalankan MySQL/MariaDB (misalnya lewat XAMPP: start Apache
     dan MySQL).
 
2. Impor database
   - Buka phpMyAdmin (http://localhost/phpmyadmin).
   - Buat database baru bernama:  db_kursus
     (collation disarankan utf8mb4_unicode_ci).
   - Pilih database tersebut, buka tab Import, pilih file
     db_kursus.sql, lalu klik Go.
   - Catatan: file SQL tidak berisi perintah CREATE DATABASE,
     jadi database harus dibuat dulu secara manual.
 
3. Atur koneksi database
   - Salin db.properties.example menjadi db.properties
     (letakkan di folder utama proyek, sejajar dengan pom.xml).
   - Sesuaikan isinya dengan MySQL Anda:
 
       db.url=jdbc:mysql://localhost:3306/db_kursus?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Makassar
       db.user=root
       db.password=
 
   - Jika db.properties tidak ada, aplikasi memakai nilai bawaan:
     localhost:3306, user "root", password kosong.
   - Jalankan aplikasi dari folder utama proyek, karena
     db.properties dibaca dari folder kerja saat ini.
 
4. Jalankan aplikasi
   Lewat terminal, dari folder SistemLes-PBO:
 
       mvn clean javafx:run
 
   Atau lewat IDE (NetBeans / IntelliJ / VS Code):
   buka proyek sebagai proyek Maven, lalu jalankan kelas
   com.tk2pbo.App (atau com.tk2pbo.Launcher jika JavaFX tidak
   terdeteksi saat dijalankan dari IDE).
 
5. (Opsional) Uji koneksi database
   Jalankan kelas com.tk2pbo.TesKoneksi untuk memastikan aplikasi
   bisa terhubung ke database.
 
AKUN LOGIN
----------
Pada halaman login, pilih peran (Admin / Siswa), lalu isi username
dan password.
 
  Admin
    Username : admin
    Password : admin123
 
  Siswa (data contoh)
    Username : surya_001 / wisnu_005 / angga_010 / luthfi_148
    Password : mahasiswa123
 
Password disimpan dalam bentuk hash SHA-256, bukan teks biasa.
Siswa baru dibuat oleh admin lewat menu Data Siswa.
 
CATATAN
-------
  - Jika muncul error koneksi, cek apakah MySQL sudah berjalan,
    nama database sudah db_kursus, dan user/password di
    db.properties sudah benar.
  - Folder target/ adalah hasil kompilasi dan dapat dibuat ulang
    dengan "mvn clean compile". File db.properties sebaiknya tidak
    ikut dikumpulkan/di-upload karena berisi password database.
 
ANGGOTA KELOMPOK
----------------
  1. Pande Putu Agus Diva Surya Dinata. L. - 2508561001
  2. I Made Bagus Wisnu Deva Manik - 2508561005
  3. I Gusti Kadek Bagus Angga Saputra - 2508561010
  4. Luthfi Hanafi - 2508561148
 
Dosen Pengampu : I Made Satria Bimantara, S.Kom., M.Kom.
 