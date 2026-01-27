package org.example.project_wobimich;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.project_wobimich.ui.WobimichController;
import org.example.project_wobimich.ui.WobimichView;

public class WobimichApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        WobimichView view = new WobimichView();
        WobimichController controller = new WobimichController(view);
        view.setController(controller);

        Scene wobimichScene = new Scene(view.createScene());
        stage.setScene(wobimichScene);
        stage.setTitle("Wobimich?!");
        stage.show();
    }
}

