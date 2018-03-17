package com.tolgaduran.notdefteri;

import com.tolgaduran.notdefteri.datamodel.NotDosya;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        NotDosya.getInstance().dosyadanNotlariGetir();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainApp.fxml"));
        primaryStage.setTitle("ToDo List");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        NotDosya.getInstance().notlariDosyayaYaz();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
