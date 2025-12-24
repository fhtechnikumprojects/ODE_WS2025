package org.example.project_wobimich;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.project_wobimich.ui.LoginUI;

public class WobimichApplication extends Application {
    @Override
    public void start(Stage stage) {
        LoginUI loginUI = new LoginUI();
        Scene loginScene = new Scene(loginUI.createLoginScene(stage),400,200);
        stage.setScene(loginScene);
        stage.setTitle("Wobimich?!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
