package org.example.project_wobimich;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.ui.WobimichUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WobimichApplication extends Application {
    @Override
    public void start(Stage stage) {

        WobimichUI wobimichUI = new WobimichUI();

        Scene wobimichScene = new Scene(wobimichUI.createScene(), 800, 600);
        stage.setScene(wobimichScene);
        stage.setTitle("Wobimich Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

