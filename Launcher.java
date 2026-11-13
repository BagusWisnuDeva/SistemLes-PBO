package com.tk2pbo;

import javafx.application.Application;
import javafx.stage.Stage;
import com.tk2pbo.util.Navigator;

public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) {
        Navigator.init(primaryStage);
        Navigator.tampilLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}