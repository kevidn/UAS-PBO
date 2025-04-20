import java.util.ArrayList;

public class DeskripsiElektronik {
    private String nama;
    private String merk;
    protected Integer harga;

    public DeskripsiElektronik(String nama, String merk, Integer harga) {
        this.nama = nama;
        this.merk = merk;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    protected Integer hitungDiskon() {
        if (this.harga >= 5000000) {
            return (int)(this.harga * 0.1);
        }
        return 0;
    }

    public void tampilkanInfo() {
        System.out.println("");
        System.out.println("Nama: " + getNama());
        System.out.println("Merk: " + getMerk());
        Integer diskon = hitungDiskon();
        if (diskon > 0) {
            System.out.println("Harga dengan diskon 10%: " + (harga - diskon));
        } else {
            System.out.println("Harga normal: " + this.harga);
        }
    }

    // Method untuk mencari barang
    public static void cariBarang(ArrayList<DeskripsiElektronik> list, String nama) {
        boolean ditemukan = false;
        for (DeskripsiElektronik item : list) {
            if (item.getNama().toLowerCase().contains(nama.toLowerCase())) {
                item.tampilkanInfo();
                ditemukan = true;
            }
        }
        if (!ditemukan) {
            System.out.println("Barang tidak ditemukan!");
        }
    }

    // Bubble Sort
    public static void bubbleSort(ArrayList<DeskripsiElektronik> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getHarga() > list.get(j + 1).getHarga()) {
                    // Swap
                    DeskripsiElektronik temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    // Quick Sort
    public static void quickSort(ArrayList<DeskripsiElektronik> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    // Partition untuk Quick Sort
    private static int partition(ArrayList<DeskripsiElektronik> list, int low, int high) {
        int pivot = list.get(high).getHarga();
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (list.get(j).getHarga() <= pivot) {
                i++;
                DeskripsiElektronik temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        DeskripsiElektronik temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }
}