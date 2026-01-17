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

    public class WobimichUI {
        private AddressLookupService addressLookupService;
        private LineLookupService lineLookupService;

        private ObservableList<Station> stations = FXCollections.observableArrayList();
        private ObservableList<LineStation> lines = FXCollections.observableArrayList();
        private ListView<Station> stationList = new ListView<>(stations);
        private ListView<LineStation> lineList = new ListView<>(lines);
        private TextField searchField = new TextField();

        public BorderPane createScene() {
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));

            //modularer Aufbau
            root.setTop(createTopSection());
            root.setCenter(createCenterSection());
            root.setBottom(createBottomSection());

            //Daten initialisieren
            loadDefaultStations();

            return root;
        }

        private VBox createTopSection() {
            VBox topVBox = new VBox(10);

            // Fun Fact Bereich
            VBox funFactBox = new VBox(5);
            funFactBox.setAlignment(Pos.CENTER);
            funFactBox.setPadding(new Insets(10));
            funFactBox.getStyleClass().add("fun-fact-box"); // Nutze lieber CSS Klassen!
            funFactBox.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

            Label funFactHeader = new Label("Hast du gewusst?");
            Label funFactText = new Label(FunFactUtils.getRandomFact());
            funFactBox.getChildren().addAll(funFactHeader, funFactText);

            // Suchbereich
            HBox searchBar = new HBox(10);
            searchBar.setPadding(new Insets(10));
            searchBar.setStyle("-fx-border-color: black;");

            searchField.setPromptText("Standort eingeben:");
            HBox.setHgrow(searchField, Priority.ALWAYS);

            Button searchButton = new Button("Suche");
            searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
            searchButton.setOnAction(e -> handleSearch());

            searchBar.getChildren().addAll(searchField, searchButton);
            topVBox.getChildren().addAll(funFactBox, searchBar);

            return topVBox;
        }

        private HBox createCenterSection() {
            HBox centerBox = new HBox(10);

            // Linke Seite: Stationen
            VBox leftBox = new VBox(stationList);
            setupBoxStyle(leftBox, 200);

            // Rechte Seite: Linien
            VBox rightBox = new VBox(lineList);
            setupBoxStyle(rightBox, 300);

            // Event für Klick auf Station
            stationList.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) handleStationSelection();
            });

            centerBox.getChildren().addAll(leftBox, rightBox);
            return centerBox;
        }

        private HBox createBottomSection() {
            Button toggleButton = new Button("Light/Dark Mode");
            toggleButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(toggleButton, Priority.ALWAYS);

            return new HBox(10, toggleButton);
        }

        private void handleSearch() {
            String address = searchField.getText();
            addressLookupService = new AddressLookupService(address);
            stations.clear();

            addressLookupService.setOnSucceeded(e -> stations.setAll(addressLookupService.getValue()));
            addressLookupService.setOnFailed(e -> showError("Adresse konnte nicht verarbeitet werden!"));
            addressLookupService.start();
        }

        private void handleStationSelection() {
            Station selected = stationList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            lineLookupService = new LineLookupService(selected.getId());
            lineLookupService.setOnSucceeded(e -> lines.setAll(lineLookupService.getValue()));
            lineLookupService.setOnFailed(e -> showError("Abfahrtszeiten nicht verfügbar!"));
            lineLookupService.start();
        }

        private void loadDefaultStations() {
            stations.setAll(
                    new Station("60200569","Höchstädtplatz",16.3769075,48.2392428),
                    new Station("60200345","Franz-Josefs-Bahnhof",16.361151,48.2259888),
                    new Station("60200743","Mitte-Landstraße",16.3845881,48.2060445),
                    new Station("60200491","Heiligenstadt",16.3657773,48.2490958),
                    new Station("60201468","Westbahnhof",16.3376511,48.1966562)
            );
        }

        private void setupBoxStyle(VBox box, double prefWidth) {
            //box.setStyle("-fx-background-color: lightblue; -fx-padding: 10; -fx-border-color: black;");
            box.setPrefWidth(prefWidth);
            HBox.setHgrow(box, Priority.ALWAYS);
            VBox.setVgrow(box, Priority.ALWAYS);
        }

        private void showError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(message);
            alert.showAndWait();
        }

    }


