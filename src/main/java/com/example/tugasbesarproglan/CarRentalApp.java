package com.example.tugasbesarproglan;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.lang.String;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CarRentalApp extends Application {
    private ObservableList<Car> availableCars = FXCollections.observableArrayList();
    private ObservableList<Rental> rentedCars = FXCollections.observableArrayList();


    private ListView<Car> carListView = new ListView<>();
    private ListView<Rental> rentalListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Bab 9: GUI
        primaryStage.setTitle("Aplikasi Penyewaan Mobil");

        Button rentButton = new Button("Sewa Mobil");
        Button payButton = new Button("Bayar Sewa");
        carListView.setItems(availableCars);
        rentalListView.setItems(rentedCars);

        // Menambahkan beberapa mobil yang tersedia
        availableCars.addAll(
                new Car("Toyota", "Camry", 500000),
                new Car("Honda", "Accord", 550000),
                new Car("Nissan", "Altima", 600000),
                new Car("Ford", "Mustang", 550000),
                new Car("Mercedes-Benz", "E-Class", 800000),
                new Car("BMW", "Z4", 1000000)
        );

        rentButton.setOnAction(e -> rentCar());
        payButton.setOnAction(e -> payRent());

        // Menggunakan HBox untuk menyusun tombol sejajar
        HBox buttonBox = new HBox(10); // Spasi antar elemen: 10 piksel
        buttonBox.getChildren().addAll(rentButton, payButton);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(buttonBox, carListView, new Label("Daftar Mobil dan Harga"), new Separator(),
                rentalListView);

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void rentCar() {
        Car selectedCar = carListView.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Penyewaan Mobil");
            dialog.setHeaderText(null);
            dialog.setContentText("Masukkan Nama Anda:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                try {
                    // Bab 5: Defensive Programming
                    if (availableCars.isEmpty()) {
                        throw new IllegalStateException("Tidak ada mobil yang tersedia saat ini.");
                    }

                    // Lakukan proses penyewaan mobil di sini...

                    // Simulasikan penyewaan dengan menghapus mobil dari daftar tersedia
                    availableCars.remove(selectedCar);

                    // Harga sewa dihitung berdasarkan informasi mobil yang dipilih
                    int rentalPrice = selectedCar.getRentalPrice();

                    // Menambahkan informasi penyewa ke daftar penyewaan
                    Rental rental = new Rental(selectedCar, name, rentalPrice);
                    rentedCars.add(rental);

                    // Menyimpan informasi penyewa ke dalam map
                    Map<String, String> renters = new HashMap<>();
                    renters.put(name, selectedCar.toString());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Mobil berhasil disewa. Terima kasih, " + name + "!");
                    alert.showAndWait();
                } catch (Exception e) {
                    // Bab 3: Error Correctness
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Terjadi kesalahan: " + e.getMessage());
                    alert.showAndWait();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih mobil yang ingin disewa dari daftar.");
            alert.showAndWait();
        }
    }

    private void payRent() {
        Rental selectedRental = rentalListView.getSelectionModel().getSelectedItem();
        if (selectedRental != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Pembayaran Sewa");
            dialog.setHeaderText(null);
            dialog.setContentText("Masukkan jumlah pembayaran:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(amount -> {
                try {
                    int paymentAmount = Integer.parseInt(amount);
                    int remainingBalance = selectedRental.payRent(paymentAmount);

                    if (remainingBalance <= 0) {
                        rentedCars.remove(selectedRental);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Pembayaran Sukses");
                        alert.setHeaderText(null);
                        alert.setContentText("Pembayaran berhasil. Terima kasih!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Pembayaran Sebagian");
                        alert.setHeaderText(null);
                        alert.setContentText("Pembayaran berhasil. Sisa pembayaran: $" + remainingBalance);
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    // Bab 3: Error Correctness
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Masukkan jumlah pembayaran yang valid.");
                    alert.showAndWait();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih sewaan yang ingin dibayar dari daftar.");
            alert.showAndWait();
        }

    }

    private class Car {
        private String brand;
        private String model;
        private int rentalPrice;

        public Car(String brand, String model, int rentalPrice) {
            this.brand = brand;
            this.model = model;
            this.rentalPrice = rentalPrice;
        }

        public int getRentalPrice() {
            return rentalPrice;
        }

        @Override
        public String toString() {
            return brand + " " + model + " - Harga: Rp" + rentalPrice;
        }
    }

    private class Rental {
        private Car car;
        private String renter;
        private int rentalPrice;
        private int remainingBalance;
        private String nik;


        public Rental(Car car, String renter, int rentalPrice) {
            this.car = car;
            this.renter = renter;
            this.nik = nik;
            this.rentalPrice = rentalPrice;
            this.remainingBalance = rentalPrice;
        }

        public int payRent(int amount) {
            remainingBalance -= amount;
            return remainingBalance;
        }

        @Override
        public String toString() {
            return "Penyewa: " + renter + ", Mobil: " + car + ", Harga Sewa: Rp" + rentalPrice +
                    ", Sisa Pembayaran: Rp" + remainingBalance;
        }
    }
}