package com.example.tugasbesarproglan;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class AplikasiRentalMobil extends Application {

    private final ObservableList<PelangganMobil> daftarPelangganMobil = FXCollections.observableArrayList();
    private ComboBox<String> mobilComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UAP");

        Label judulLabel = new Label("Rental Mobil");
        judulLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;-fx-text-fill: #4682B4;");

        GridPane grid = buatForm();

        Button sewaMButton = new Button("Sewa Mobil");
        sewaMButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;-fx-padding: 10;");

        sewaMButton.setOnAction(e -> {
            PelangganMobil pelangganMobil = buatPelangganMobilDariForm(grid);
            if (pelangganMobil != null) {
                daftarPelangganMobil.add(pelangganMobil);
                hapusForm(grid);
            }
        });

        Button cetakStrukButton = new Button("Cetak Struk");
        cetakStrukButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black;-fx-padding: 10;");
        cetakStrukButton.setOnAction(e -> cetakStruk());
        HBox buttonBox = new HBox(sewaMButton, cetakStrukButton);
        buttonBox.setSpacing(10);

        VBox vBox = new VBox(judulLabel, buatTabelPelangganMobil(), grid, buttonBox);
        vBox.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 10;");
        VBox.setMargin(sewaMButton, new Insets(0, 0, 0, 15));
        Scene scene = new Scene(vBox, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<PelangganMobil> buatTabelPelangganMobil() {
        TableView<PelangganMobil> tabelPelangganMobil = new TableView<>();
        TableColumn<PelangganMobil, String> namaColumn = new TableColumn<>("Nama");
        TableColumn<PelangganMobil, String> nomorKtpColumn = new TableColumn<>("Nomor KTP");
        TableColumn<PelangganMobil, String> alamatColumn = new TableColumn<>("Alamat");
        TableColumn<PelangganMobil, Integer> lamaSewaColumn = new TableColumn<>("Lama Sewa (hari)");
        TableColumn<PelangganMobil, String> mobilPilihanColumn = new TableColumn<>("Mobil Pilihan");
        TableColumn<PelangganMobil, Double> hargaColumn = new TableColumn<>("Harga");

        namaColumn.setStyle("-fx-text-fill: #191970;");
        nomorKtpColumn.setStyle("-fx-text-fill: #FF2400;");

        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        nomorKtpColumn.setCellValueFactory(new PropertyValueFactory<>("nomorKtp"));
        alamatColumn.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        lamaSewaColumn.setCellValueFactory(new PropertyValueFactory<>("lamaSewa"));
        mobilPilihanColumn.setCellValueFactory(new PropertyValueFactory<>("mobilPilihan"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("harga"));

        tabelPelangganMobil.getColumns().addAll(namaColumn, nomorKtpColumn, alamatColumn, lamaSewaColumn, mobilPilihanColumn,hargaColumn);
        tabelPelangganMobil.setItems(daftarPelangganMobil);

        return tabelPelangganMobil;
    }

    private GridPane buatForm() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        Label namaLabel = new Label("Nama:");
        TextField namaField = new TextField();

        Label nomorKtpLabel = new Label("Nomor KTP:");
        TextField nomorKtpField = new TextField();

        Label alamatLabel = new Label("Alamat:");
        TextField alamatField = new TextField();

        Label lamaSewaLabel = new Label("Lama Sewa (hari):");
        TextField lamaSewaField = new TextField();

        Label mobilLabel = new Label("Mobil Pilihan (klik):");
        mobilComboBox = new ComboBox<>();
        mobilComboBox.getItems().addAll("Avanza", "Innova", "Fortuner", "Xenia", "Ertiga", "Civic", "CR-V", "Alphard");
        mobilComboBox.setStyle(
                "-fx-text-fill: #7DF9FF; " +
                        "-fx-border-color: #3498DB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-padding: 2px 4px;"
        );

        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #1F51FF; -fx-text-fill: white;");
        updateButton.setOnAction(e -> {
            String namaToUpdate = ((TextField) grid.getChildren().get(1)).getText();

            Optional<PelangganMobil> pelangganToUpdate = daftarPelangganMobil.stream()
                    .filter(pelanggan -> pelanggan.getNama().equals(namaToUpdate))
                    .findFirst();

            if (pelangganToUpdate.isPresent()) {
                PelangganMobil updatedPelanggan = buatPelangganMobilDariForm(grid);

                if (updatedPelanggan != null) {
                    pelangganToUpdate.get().setNomorKtp(updatedPelanggan.getNomorKtp());
                    pelangganToUpdate.get().setAlamat(updatedPelanggan.getAlamat());
                    pelangganToUpdate.get().setLamaSewa(updatedPelanggan.getLamaSewa());
                    pelangganToUpdate.get().setMobilPilihan(updatedPelanggan.getMobilPilihan());

                    buatTabelPelangganMobil().setItems(daftarPelangganMobil);

                    System.out.println("Data berhasil diupdate!");
                }
            } else {
                tampilkanPeringatan("Data tidak ditemukan untuk nama: " + namaToUpdate);
            }
        });

        Button clearButton = new Button("Bersihkan");
        clearButton.setStyle("-fx-background-color: #FF5733; -fx-text-fill: white;");
        clearButton.setOnAction(e -> hapusForm(grid));

        grid.add(namaLabel, 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(nomorKtpLabel, 0, 1);
        grid.add(nomorKtpField, 1, 1);
        grid.add(alamatLabel, 0, 2);
        grid.add(alamatField, 1, 2);
        grid.add(lamaSewaLabel, 0, 3);
        grid.add(lamaSewaField, 1, 3);
        grid.add(mobilLabel, 0, 4);
        grid.add(mobilComboBox, 1, 4);
        grid.add(updateButton, 0, 6);
        grid.add(clearButton, 0, 5, 2, 1);

        return grid;
    }

    private PelangganMobil buatPelangganMobilDariForm(GridPane grid) {
        String nama = ((TextField) grid.getChildren().get(1)).getText();
        String nomorKtp = ((TextField) grid.getChildren().get(3)).getText();
        String alamat = ((TextField) grid.getChildren().get(5)).getText();
        String lamaSewaText = ((TextField) grid.getChildren().get(7)).getText();
        String mobilPilihan = mobilComboBox.getSelectionModel().getSelectedItem();
        double harga = hitungHargaMobil(mobilPilihan); // Fungsi untuk menghitung harga berdasarkan mobil pilihan

        if (nama.isEmpty() || nomorKtp.isEmpty() || alamat.isEmpty() || lamaSewaText.isEmpty()) {
            tampilkanPeringatan("Semua kolom harus diisi");
            return null;
        }

        try {
            if (!nomorKtp.matches("\\d+")) {
                throw new NumberFormatException();
            }

            int lamaSewa = Integer.parseInt(lamaSewaText);
            if (lamaSewa <= 0) {
                throw new IllegalArgumentException("Lama Sewa harus lebih dari 0.");
            }
            return new PelangganMobil(nama, nomorKtp, alamat, lamaSewa, mobilPilihan,harga);

        } catch (NumberFormatException e) {
            tampilkanPeringatan("Format Nomor KTP tidak valid. Harap masukkan 16 digit angka.");
            return null;
        } catch (IllegalArgumentException e) {
            tampilkanPeringatan(e.getMessage());
            return null;
        }
    }


    private void hapusForm(GridPane grid) {
        ((TextField) grid.getChildren().get(1)).clear();
        ((TextField) grid.getChildren().get(3)).clear();
        ((TextField) grid.getChildren().get(5)).clear();
        ((TextField) grid.getChildren().get(7)).clear();
    }

    private void cetakStruk() {
        PelangganMobil pelangganTerakhir = daftarPelangganMobil.get(daftarPelangganMobil.size() - 1);

        String struk = "===== Struk Rental Mobil =====\n" +
                "Nama: " + pelangganTerakhir.getNama() + "\n" +
                "Nomor KTP: " + pelangganTerakhir.getNomorKtp() + "\n" +
                "Alamat: " + pelangganTerakhir.getAlamat() + "\n" +
                "Lama Sewa (hari): " + pelangganTerakhir.getLamaSewa() + "\n" +
                "Mobil Pilihan: " + pelangganTerakhir.getMobilPilihan() + "\n" +
                "Harga: "+ pelangganTerakhir.getHarga()+"\n"+
                "==============================";

        tampilkanStrukDialog(struk);
    }

    private void tampilkanStrukDialog(String struk) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Struk Rental Mobil");
        alert.setHeaderText(null);
        alert.setContentText(struk);
        alert.showAndWait();
    }

    private void tampilkanPeringatan(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class PelangganMobil {

        private String nama;
        private String nomorKtp;
        private String alamat;
        private int lamaSewa;
        private String mobilPilihan;
        private double harga;

        public PelangganMobil(String nama, String nomorKtp, String alamat, int lamaSewa, String mobilPilihan, double harga) {
            this.nama = nama;
            this.nomorKtp = nomorKtp;
            this.alamat = alamat;
            this.lamaSewa = lamaSewa;
            this.mobilPilihan = mobilPilihan;
            this.harga = harga;
        }

        public String getNama() {
            return nama;
        }

        public String getNomorKtp() {
            return nomorKtp;
        }

        public String getAlamat() {
            return alamat;
        }

        public int getLamaSewa() {
            return lamaSewa;
        }

        public String getMobilPilihan() {
            return mobilPilihan;
        }

        public double getHarga(){
            return harga;
        }

        public void setNomorKtp(String nomorKtp) {
            this.nomorKtp = nomorKtp;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public void setLamaSewa(int lamaSewa) {
            this.lamaSewa = lamaSewa;
        }

        public void setMobilPilihan(String mobilPilihan) {
            this.mobilPilihan = mobilPilihan;
        }
        public void setHarga(double harga){
            this.harga = harga;
        }

    }
    public double hitungHargaMobil(String mobilPilihan) {
        switch (mobilPilihan) {
            case "Avanza":
                return 200000.0;
            case "Innova":
                return 300000.0;
            case "Fortuner":
                return 400000.0;
            case "Xenia":
                return 150000.0;
            case "Ertiga":
                return 250000.0;
            case "Civic":
                return 350000.0;
            case "CR-V":
                return 450000.0;
            case "Alphard":
                return 500000.0;
            // Tambahkan harga untuk mobil-mobil lainnya sesuai kebutuhan
            default:
                return 0.0;
        }
    }

}