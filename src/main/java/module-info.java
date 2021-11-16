module com.example.cell_load {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens com.example.cell_load to javafx.fxml;
    exports com.example.cell_load;

}