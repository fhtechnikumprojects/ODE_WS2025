package org.example.project_wobimich.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.project_wobimich.FunFactUtils;
import org.example.project_wobimich.service.OutputRightBox;
import org.example.project_wobimich.service.SelectedStationOperator;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;

import java.util.ArrayList;
import java.util.List;

public class WobimichUI {
    private AddressLookupService addressLookupService;
    private SelectedStationOperator selectedStationOperator;

    public BorderPane createScene() {
        ObservableList<String> station = FXCollections.observableArrayList();
        ListView<String> stationList = new ListView<>();
        //need an all data station list; later: maybe just 1 list?
        List<Station> fullStationList = new ArrayList<>();

        stationList.setItems(station);
//vorübergehend; löschen bei success!
//        stationList.getSelectionModel()
//                .selectedItemProperty()
//                .addListener((obs, oldVal, selectedName) -> {
//                    if (selectedName != null) {
//                        selectedStationOperator.handleSelection(selectedName);
//                    }
//                });


        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        //Top level
        VBox topVBox = new VBox(10);

        //Top level: Fun-fact
        VBox funFactBox = new VBox(5);
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                    -fx-background-color: lightgray;
                """);

        Label funFactHeader = new Label("Hast du gewusst?");
        funFactHeader.setAlignment(Pos.CENTER);

        Label funFactText = new Label(FunFactUtils.getRandomFact());
        funFactText.setAlignment(Pos.CENTER);

        funFactBox.getChildren().addAll(funFactHeader, funFactText);

        //Top level: Search-bar
        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                """);
        TextField searchField = new TextField();
        searchField.setPromptText("Standort eingeben:");
        HBox.setHgrow(searchField, Priority.ALWAYS);


        //Top level: Search-button
        Button searchButton = new Button("Suche:");
        searchBar.getChildren().addAll(searchField, searchButton);

        //Top level: Search-button - Event handler ==> use service and task
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty()); //if search bar is empty ==> button cannot be clicked!

        searchButton.setOnAction((event) -> {
            String address = searchField.getText();
            addressLookupService = new AddressLookupService(address);
            station.clear();
            fullStationList.clear();

            addressLookupService.setOnSucceeded(e -> {
                ArrayList<Station> stations = addressLookupService.getValue();
                for (Station s : stations) {
                    station.add(s.getName());
                    fullStationList.add(s);
                }
            });

            addressLookupService.start();
        });

        //Top level: add all boxes that are at top-level
        topVBox.getChildren().addAll(funFactBox, searchBar);
        root.setTop(topVBox);

        //Center level
        HBox centerBox = new HBox(10);
        centerBox.setSpacing(10);

        //Center level: left
        VBox centerLeftVBox = new VBox();
        centerLeftVBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
        centerLeftVBox.setPrefWidth(200);
        centerLeftVBox.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                """);
        HBox.setHgrow(centerLeftVBox, Priority.ALWAYS);
        VBox.setVgrow(centerLeftVBox, Priority.ALWAYS);

        //Center left: Default stations ==> show 5 stations after starting the application (part of 1. Feature)
        station.setAll(
                "Höchstädtplatz",
                "Franz-Josefs-Bahnhof",
                "Heiligenstadt",
                "Mitte-Landstraße",
                "Erdberg"
        );

        centerLeftVBox.getChildren().add(stationList);

        //Center level: right
        VBox centerRightVBox = new VBox();
        centerRightVBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
        centerRightVBox.setPrefWidth(200);
        centerRightVBox.setStyle("""
                -fx-border-color: black;
                -fx-border-width: 1;
                """);

        HBox.setHgrow(centerRightVBox, Priority.ALWAYS);
        VBox.setVgrow(centerRightVBox, Priority.ALWAYS);
        //Achtung: hier Übergabe für rechts - bitte bei Änderungen hier nicht reinpfuschen!
        OutputRightBox outputRightBox = new OutputRightBox(centerRightVBox);
        this.selectedStationOperator =
                new SelectedStationOperator(fullStationList, outputRightBox);

        stationList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, selectedName) -> {
                    if (selectedName != null) {
                        this.selectedStationOperator.handleSelection(selectedName);
                    }
                });


        centerBox.getChildren().addAll(centerLeftVBox, centerRightVBox);

        root.setCenter(centerBox);

        //Bottom level

        //Toggle Light/Dark Mode button
        HBox bottomBox = new HBox(10);
        bottomBox.setSpacing(10);
        VBox bottomVBox = new VBox();

        Button ToggleLightDarkButton = new Button("Light/Dark Mode");
        bottomBox.getChildren().addAll(ToggleLightDarkButton);
        bottomVBox.getChildren().add(bottomBox);
        ToggleLightDarkButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(ToggleLightDarkButton, Priority.ALWAYS);

        //Dropdown field shows listing of history search input

        root.setBottom(bottomBox);

        return root;
    }
}
