import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

public class Elektronik {
    private ArrayList<DeskripsiElektronik> listElektronik;
    private HashMap<String, Integer> jumlahBarang;

    public Elektronik() {
        this.listElektronik = new ArrayList<>();
        this.jumlahBarang = new HashMap<>();
    }

    public void tambahElektronik(DeskripsiElektronik elektronik) {
        listElektronik.add(elektronik);
        simpanKeFile(); // Auto save saat menambah barang
    }

    public void tampilkanSemuaElektronik() {
        for (DeskripsiElektronik elektronik : listElektronik) {
            elektronik.tampilkanInfo();
            System.out.println("--------------------------------");
        }
    }

    public void tambahJumlahBarang(String nama, int jumlah) {
        jumlahBarang.put(nama, jumlah);
    }

    public void tampilkanJumlahBarang() {
        System.out.println("\nJumlah Stok Barang:");
        for (String nama : jumlahBarang.keySet()) {
            System.out.println(nama + ": " + jumlahBarang.get(nama) + " unit");
        }
    }

    public boolean beliBarang(String nama, int jumlah) {
        if (!jumlahBarang.containsKey(nama)) {
            System.out.println("Barang tidak tersedia!");
            return false;
        }
        
        int stokTersedia = jumlahBarang.get(nama);
        if (stokTersedia < jumlah) {
            System.out.println("Stok tidak mencukupi! Stok tersedia: " + stokTersedia);
            return false;
        }
        
        jumlahBarang.put(nama, stokTersedia - jumlah);
        simpanKeFile(); // Auto save saat pembelian
        return true;
    }

    private boolean isBarangValid(String nama) {
        for (DeskripsiElektronik elektronik : listElektronik) {
            if (elektronik.getNama().equalsIgnoreCase(nama)) {
                return true;
            }
        }
        return false;
    }

    public void prosesBeliBarang(Scanner scanner) {
        while (true) {
            System.out.print("\nMasukkan nama barang yang ingin dibeli: ");
            String namaBeli = scanner.nextLine();
            
            // Jika input kosong, kembali ke menu utama
            if (namaBeli.trim().isEmpty()) {
                System.out.println("Pembelian dibatalkan.");
                return;
            }

            // Tampilkan hasil pencarian
            System.out.println("\nHasil pencarian:");
            DeskripsiElektronik.cariBarang(listElektronik, namaBeli);

            if (!isBarangValid(namaBeli)) {
                System.out.println("Nama barang tidak sesuai dengan katalog.");
                System.out.println("Silakan masukkan nama barang yang sesuai atau tekan Enter untuk membatalkan.");
                continue;
            }

            // Tampilkan stok tersedia
            int stokTersedia = jumlahBarang.get(namaBeli);
            System.out.println("\nStok " + namaBeli + " tersedia: " + stokTersedia + " unit");

            // Input jumlah pembelian
            System.out.print("Masukkan jumlah yang ingin dibeli (0 untuk batal): ");
            int jumlahBeli;
            try {
                jumlahBeli = scanner.nextInt();
                scanner.nextLine(); // clear buffer
            } catch (Exception e) {
                System.out.println("Input tidak valid!");
                scanner.nextLine(); // clear buffer
                continue;
            }

            if (jumlahBeli == 0) {
                System.out.println("Pembelian dibatalkan.");
                return;
            }

            // Proses pembelian
            if (beliBarang(namaBeli, jumlahBeli)) {
                // Cari harga barang dan hitung diskon
                int harga = 0;
                int diskon = 0;
                for (DeskripsiElektronik elektronik : listElektronik) {
                    if (elektronik.getNama().equals(namaBeli)) {
                        harga = elektronik.getHarga();
                        diskon = elektronik.hitungDiskon();
                        break;
                    }
                }
                
                int hargaSetelahDiskon = harga - diskon;
                int totalHarga = hargaSetelahDiskon * jumlahBeli;
                
                System.out.println("\n=== Detail Pembelian ===");
                System.out.println("Barang: " + namaBeli);
                System.out.println("Jumlah: " + jumlahBeli + " unit");
                System.out.println("Harga per unit: Rp " + harga);
                if (diskon > 0) {
                    System.out.println("Diskon per unit: Rp " + diskon + " (10%)");
                    System.out.println("Harga setelah diskon: Rp " + hargaSetelahDiskon);
                }
                System.out.println("Total harga: Rp " + totalHarga);
                System.out.println("Sisa stok: " + jumlahBarang.get(namaBeli) + " unit");
                System.out.println("Pembelian berhasil!");
            }
            return;
        }
    }

    public void simpanKeFile() {
        try (FileWriter writer = new FileWriter("data_elektronik.txt")) {
            for (DeskripsiElektronik elektronik : listElektronik) {
                String nama = elektronik.getNama();
                Integer stok = jumlahBarang.getOrDefault(nama, 0);
                writer.write(String.format("%s,%s,%d,%d\n", 
                    nama,
                    elektronik.getMerk(),
                    elektronik.getHarga(),
                    stok
                ));
            }
        } catch (IOException e) {
            // Silent error handling to avoid console spam
        }
    }

    public void bacaDariFile() {
        File file = new File("data_elektronik.txt");
        
        if (!file.exists()) {
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            listElektronik.clear();
            jumlahBarang.clear();
                
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    try {
                        String nama = data[0];
                        String merk = data[1];
                        Integer harga = Integer.parseInt(data[2]);
                        Integer stok = Integer.parseInt(data[3]);
                        
                        DeskripsiElektronik elektronik = new DeskripsiElektronik(nama, merk, harga);
                        listElektronik.add(elektronik);
                        jumlahBarang.put(nama, stok);
                    } catch (NumberFormatException e) {
                        // Silent error handling to avoid console spam
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            // Silent error handling to avoid console spam
        }
    }

    public static void main(String[] args) {
        Elektronik toko = new Elektronik();
        Scanner scanner = new Scanner(System.in);

        // Load data saat program dimulai
        toko.bacaDariFile();

        // Jika file kosong atau belum ada, inisialisasi dengan data default
        if (toko.listElektronik.isEmpty()) {
            // Inisialisasi data dengan 10 elektronik
            DeskripsiElektronik laptop = new DeskripsiElektronik("Laptop", "Asus", 8000000);
            DeskripsiElektronik smartphone = new DeskripsiElektronik("Smartphone", "Samsung", 5000000);
            DeskripsiElektronik smartwatch = new DeskripsiElektronik("Smartwatch", "Apple", 3000000);
            DeskripsiElektronik tablet = new DeskripsiElektronik("Tablet", "iPad", 7000000);
            DeskripsiElektronik headphone = new DeskripsiElektronik("Headphone", "Sony", 2000000);
            DeskripsiElektronik speaker = new DeskripsiElektronik("Speaker", "JBL", 1500000);
            DeskripsiElektronik camera = new DeskripsiElektronik("Camera", "Canon", 6000000);
            DeskripsiElektronik printer = new DeskripsiElektronik("Printer", "Epson", 2500000);
            DeskripsiElektronik monitor = new DeskripsiElektronik("Monitor", "LG", 3500000);
            DeskripsiElektronik keyboard = new DeskripsiElektronik("Keyboard", "Logitech", 1000000);

            toko.tambahElektronik(laptop);
            toko.tambahElektronik(smartphone);
            toko.tambahElektronik(smartwatch);
            toko.tambahElektronik(tablet);
            toko.tambahElektronik(headphone);
            toko.tambahElektronik(speaker);
            toko.tambahElektronik(camera);
            toko.tambahElektronik(printer);
            toko.tambahElektronik(monitor);
            toko.tambahElektronik(keyboard);

            // Inisialisasi jumlah barang
            toko.tambahJumlahBarang("Laptop", 5);
            toko.tambahJumlahBarang("Smartphone", 10);
            toko.tambahJumlahBarang("Smartwatch", 8);
            toko.tambahJumlahBarang("Tablet", 6);
            toko.tambahJumlahBarang("Headphone", 15);
            toko.tambahJumlahBarang("Speaker", 12);
            toko.tambahJumlahBarang("Camera", 7);
            toko.tambahJumlahBarang("Printer", 4);
            toko.tambahJumlahBarang("Monitor", 9);
            toko.tambahJumlahBarang("Keyboard", 20);
        }

        while (true) {
            System.out.println("\n=== Toko Elektronik ===");
            System.out.println("1. Lihat Barang Elektronik");
            System.out.println("2. Cari Nama Elektronik");
            System.out.println("3. Sorting Berdasarkan Harga (Bubble Sort)");
            System.out.println("4. Sorting Berdasarkan Harga (Quick Sort)");
            System.out.println("5. Tampilkan Jumlah Barang");
            System.out.println("6. Hitung Harga dengan Diskon");
            System.out.println("7. Beli Barang");
            System.out.println("8. Keluar");
            System.out.print("Pilih (1-8): ");

            int pilihan = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (pilihan) {
                case 1:
                    toko.tampilkanSemuaElektronik();
                    break;
                case 2:
                    System.out.print("Masukkan nama barang yang dicari: ");
                    String namaCari = scanner.nextLine();
                    DeskripsiElektronik.cariBarang(toko.listElektronik, namaCari);
                    break;
                case 3:
                    DeskripsiElektronik.bubbleSort(toko.listElektronik);
                    System.out.println("\nHasil Sorting (Bubble Sort):");
                    toko.tampilkanSemuaElektronik();
                    break;
                case 4:
                    DeskripsiElektronik.quickSort(toko.listElektronik, 0, toko.listElektronik.size() - 1);
                    System.out.println("\nHasil Sorting (Quick Sort):");
                    toko.tampilkanSemuaElektronik();
                    break;
                case 5:
                    toko.tampilkanJumlahBarang();
                    break;
                case 6:
                    System.out.println("\nHarga dengan Diskon:");
                    toko.tampilkanSemuaElektronik();
                    break;
                case 7:
                    toko.prosesBeliBarang(scanner);
                    break;
                case 8:
                    System.out.println("Terima kasih telah menggunakan aplikasi!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Input tidak valid! Silakan masukkan angka 1-8.");
            }
        }
    }
}