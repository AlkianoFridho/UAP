module com.example.tugasbesarproglan {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tugasbesarproglan to javafx.fxml;
    exports com.example.tugasbesarproglan;
}