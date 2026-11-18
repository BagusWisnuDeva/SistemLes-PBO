package com.tk2pbo;

import com.tk2pbo.util.Navigator;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Navigator.init(stage);
        Navigator.tampilLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}