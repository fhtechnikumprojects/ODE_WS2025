package org.example.project_wobimich.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;

import java.util.ArrayList;
import java.util.Objects;

public class LoginUI {

    public VBox createLoginScene(Stage stage) {
        VBox loginVBox = new VBox(12);
        loginVBox.setPadding(new Insets(25));
        loginVBox.setStyle("-fx-background-color: #87CEFA;");
        loginVBox.setAlignment(Pos.CENTER);


        Label label = new Label("Bitte einloggen:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Benutzername");


        TextField locationField = new TextField();
        locationField.setPromptText("Standort eingeben");


        Button loginButton = new Button("Login");
        loginButton.setDisable(true);



        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            loginButton.setDisable(newVal.trim().isEmpty());
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String location = locationField.getText().trim();

            if (!location.isEmpty()) {

                AddressLookupService addressLookupService = new AddressLookupService(location);

                loginButton.setDisable(true); // Disable Button während Suche

                addressLookupService.setOnSucceeded(ev -> {
                    ArrayList<Station> stations = addressLookupService.getValue();

                    Platform.runLater(() -> {
                        openMainUI(stage, username, stations);
                    });
                });

                addressLookupService.setOnFailed(ev -> {
                    Platform.runLater(() -> {
                        openMainUI(stage, username, new ArrayList<>());
                    });
                });

                addressLookupService.start();

            } else {

                openMainUI(stage, username, new ArrayList<>());
            }
        });

        loginVBox.getChildren().addAll(label, usernameField, locationField, loginButton);
        return loginVBox;
    }

    private void openMainUI(Stage stage, String username, ArrayList<Station> stations) {
        WobimichUI wobimichUI = new WobimichUI();
        wobimichUI.setInitialStations(stations);
        BorderPane mainRoot = wobimichUI.createScene();
        Scene mainScene = new Scene(mainRoot, 800, 600);

        stage.setScene(mainScene);
        stage.setTitle("Wobimich - Willkommen " + username);
    }
}

