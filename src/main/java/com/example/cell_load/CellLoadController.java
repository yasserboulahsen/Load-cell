package com.example.cell_load;

import Esp.CellLoadData;
import Esp.EspData;
import Esp.EspSerialPort;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CellLoadController {
    @FXML
    private Button stop;
    @FXML
    private MenuButton menu;
    @FXML
    private Button start;
    @FXML
    private Label liable4;
    @FXML
    private Label liable3;
    @FXML
    private Label liable2;
    @FXML
    private Label liable1;



    private CellLoadData cellLoadData;

    public void initialize(){
        menu.setDisable(true);
        stop.setDisable(true);

    }


    public void startReding(ActionEvent actionEvent) throws IOException, InterruptedException {

//        EspData.comPortFinalData(EspData.comPort());
        stop.setDisable(false);
    try {
        cellLoadData = new CellLoadData(liable1, liable2, liable3, liable4);
        cellLoadData.cellData(500);
    }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Connection error");
        alert.setContentText("Make sure the arduino is connected !");
        alert.showAndWait();
    }

    }

    public void stop(ActionEvent actionEvent) throws IOException, InterruptedException {

        cellLoadData.shutDownService();
        menu.setDisable(false);

    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("Exit the programe");

        Platform.exit();
        System.exit(1);
    }

    public void saveToTxt(ActionEvent event) {


        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if(file!=null){

                saveTextToFile(cellLoadData.gatData(),file);


        }
    }

    private void saveTextToFile(List<String> content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            System.out.println("enable to save file");
        }
    }
}