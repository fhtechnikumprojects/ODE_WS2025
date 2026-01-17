    package org.example.project_wobimich.ui;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.control.*;
    import javafx.scene.layout.*;
    import org.example.project_wobimich.model.LineStation;
    import org.example.project_wobimich.service.LineLookupService;
    import org.example.project_wobimich.utils.FunFactUtils;
    import org.example.project_wobimich.model.Station;
    import org.example.project_wobimich.service.AddressLookupService;

    import java.util.ArrayList;
    import java.util.List;

    public class WobimichUI {
        private AddressLookupService addressLookupService;
        private LineLookupService lineLookupService;

        public BorderPane createScene() {
            ObservableList<Station> station = FXCollections.observableArrayList();
            ListView<Station> stationList = new ListView<>();
            stationList.setItems(station);

            ObservableList<LineStation> line = FXCollections.observableArrayList();
            ListView<LineStation> lineList = new ListView<>();
            lineList.setItems(line);

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
            Button searchButton = new Button("Suche");
            searchBar.getChildren().addAll(searchField, searchButton);

            //Top level: Search-button - Event handler ==> use service and task
            searchButton.disableProperty().bind(searchField.textProperty().isEmpty()); //if search bar is empty ==> button cannot be clicked!

            searchButton.setOnAction((event) -> {
                String address = searchField.getText();
                addressLookupService = new AddressLookupService(address);
                station.clear();

                addressLookupService.setOnSucceeded(e -> {
                    ArrayList<Station> stations = addressLookupService.getValue();
                    station.setAll(stations);
                });

                //Exception-Handling für User, eine Info-Meldung in der GUI
                addressLookupService.setOnFailed(e -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ohje...da ist etwas schief gelaufen!");
                    alert.setHeaderText(null);
                    alert.setContentText("Die eingegebene Adresse konnte nicht verarbeitet werden!");
                    alert.showAndWait();
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
                new Station("60200569","Höchstädtplatz",16.3769075,48.2392428),
                new Station("60200345","Franz-Josefs-Bahnhof",16.361151,48.2259888),
                new Station("60200491","Heiligenstadt",16.3657773,48.2490958),
                new Station("60200743","Mitte-Landstraße",16.3845881,48.2060445),
                new Station("60200289","Erdberg",16.4139989,48.1915243)
            );

            centerLeftVBox.getChildren().add(stationList);

            //Center level: right
            VBox centerRightVBox = new VBox();
            centerRightVBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
            centerRightVBox.setPrefWidth(300);
            centerRightVBox.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                    """);

            HBox.setHgrow(centerRightVBox, Priority.ALWAYS);
            VBox.setVgrow(centerRightVBox, Priority.ALWAYS);

            //Clicking on a station will show the lines with its (real time) information
            stationList.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Station selectedStation = stationList.getSelectionModel().getSelectedItem();
                    if (selectedStation != null) {
                        lineLookupService = new LineLookupService(selectedStation.getId());

                        lineLookupService.setOnSucceeded( e -> {
                            List<LineStation> result = lineLookupService.getValue();
                            line.setAll(result); //Update list of the right box
                        });

                        //Exception-Handling für User, eine Info-Meldung in der GUI
                        lineLookupService.setOnFailed(e -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ohje...da ist etwas schief gelaufen!");
                            alert.setHeaderText(null);
                            alert.setContentText("Die Linien und ihre Abfahrtszeiten können derzeit nicht angezeigt werden!");
                            alert.showAndWait();
                        });
                        lineLookupService.start();
                    }
                }
            });

            centerRightVBox.getChildren().addAll(lineList);
            centerBox.getChildren().addAll(centerLeftVBox,centerRightVBox);
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
