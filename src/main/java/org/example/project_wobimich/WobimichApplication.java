package org.example.project_wobimich;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.project_wobimich.ui.WobimichUI;

public class WobimichApplication extends Application {
    @Override
    public void start(Stage stage) {
        WobimichUI wobimichUI = new WobimichUI();
        Scene wobimichScene = new Scene(wobimichUI.createScene());
        stage.setScene(wobimichScene);
        stage.setTitle("Wobimich?!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
