package org.example.project_wobimich;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.project_wobimich.ui.LoginUI;
import org.example.project_wobimich.ui.WobimichUI;

public class WobimichApplication extends Application {
    @Override
    public void start(Stage stage) {
        WobimichUI wobimichUI = new WobimichUI();
        Scene loginScene = new Scene(WobimichUI.(stage),400,200);
        stage.setScene();
        stage.setTitle("Wobimich?!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
