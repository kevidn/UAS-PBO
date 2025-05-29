# 🧾 UAS Pemrograman Berorientasi Objek (PBO)

Repositori ini merupakan hasil pengerjaan **Ujian Akhir Semester (UAS)** mata kuliah **Pemrograman Berorientasi Objek**.

## 👥 Contributors

| Foto | Username |
|------|----------|
| <img src="https://github.com/kevidn.png" width="80" height="80" /> | [kevidn](https://github.com/kevidn) |
| <img src="https://github.com/atangorp.png" width="80" height="80" /> | [atangorp](https://github.com/atangorp) |
| <img src="https://github.com/adalahruna.png" width="80" height="80" /> | [adalahruna](https://github.com/adalahruna) |
| <img src="https://github.com/torarizal.png" width="80" height="80" /> | [torarizal](https://github.com/torarizal) |
| <img src="https://github.com/Yusa137D.png" width="80" height="80" /> | [Yusa137D](https://github.com/Yusa137D) |
| <img src="https://github.com/ZafaaFA12.png" width="80" height="80" /> | [ZafaaFA12](https://github.com/ZafaaFA12) |

---

## 🖼️ Tampilan Antarmuka Aplikasi

### 📊 Dashboard

<img src="https://raw.githubusercontent.com/kevidn/UAS-PBO/refs/heads/master/1.PNG" width="600"/>

> Halaman **Dashboard** merupakan tampilan utama setelah login.  
> Fitur:
> - Ringkasan data (jumlah transaksi, pembelian, dll)
> - Navigasi ke fitur lain via sidebar
> - Desain simple dan informatif

---

### 📱 Manajemen Data Phones

<img src="https://raw.githubusercontent.com/kevidn/UAS-PBO/refs/heads/master/2.PNG" width="600"/>

> Halaman untuk mengelola daftar handphone.  
> Fitur:
> - Menampilkan data produk: nama, merk, harga, stok
> - Tombol **Tambah**, **Edit**, **Hapus** data handphone

---

### 💸 Histori Transaksi

<img src="https://raw.githubusercontent.com/kevidn/UAS-PBO/refs/heads/master/3.PNG" width="600"/>

> Menampilkan semua riwayat transaksi pembelian.  
> Fitur:
> - Daftar transaksi dengan informasi lengkap
> - Memudahkan pelacakan dan dokumentasi pembelian

---

## ⚙️ Konfigurasi & Cara Menjalankan

### 📌 Persiapan

1. Clone repositori:

```bash
git clone https://github.com/kevidn/UAS-PBO.git
cd UAS-PBO
```

2. Buka project di IDE favorit (IntelliJ, NetBeans, Eclipse)

3. Jalankan server Apache dan MySQL di XAMPP
   
4. Buat database MySQL

```sql
CREATE DATABASE jual_hp;
```

5. Jalankan Aplikasi

```java
mvn spring-boot:run
```
