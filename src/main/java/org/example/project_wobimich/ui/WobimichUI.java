package org.example.project_wobimich.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.project_wobimich.FunFactUtils;
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class WobimichUI {

    private AddressLookupService addressLookupService;
    private List<Station> initialStations;

    private final ObservableList<String> stationNames = FXCollections.observableArrayList();
    private final ObservableList<String> favoriteStations = FXCollections.observableArrayList();
    private final ObservableList<String> filteredLines = FXCollections.observableArrayList();

    private static final Path SEARCH_LOG = Paths.get("search_history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.json");

    public void setInitialStations(List<Station> stations) {
        this.initialStations = stations;
        stationNames.clear();
        if (stations != null) {
            stations.forEach(s -> stationNames.add(s.getName()));
        }
    }

    public BorderPane createScene() {

        loadFavorites();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color:#87CEFA;");

        ImageView logo = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/org/example/project_wobimich/WobimichLogo.png")
                ))
        );
        logo.setFitWidth(160);
        logo.setPreserveRatio(true);

        VBox funFactBox = new VBox(
                new Label("Hast du gewusst?"),
                new Label(FunFactUtils.getRandomFact())
        );
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.setStyle("-fx-border-color:red;-fx-border-width:2;-fx-background-color:white;");

        TextField searchField = new TextField();
        searchField.setPromptText("Standort eingeben");

        Button searchButton = new Button("Suche");
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
        searchButton.setStyle("-fx-border-color:red; -fx-border-width:2;");

        HBox searchBar = new HBox(10, searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox topBox = new VBox(10, logo, funFactBox, searchBar);
        topBox.setAlignment(Pos.CENTER);

        root.setTop(topBox);

        ListView<String> stationList = new ListView<>(stationNames);
        ListView<String> favoritesList = new ListView<>(favoriteStations);
        ListView<String> linesList = new ListView<>(filteredLines);

        stationList.setCellFactory(lv -> new ListCell<>() {
            private final Button star = new Button();
            private final Label label = new Label();
            private final HBox box = new HBox(10, label, star);

            {
                star.setOnAction(e -> toggleFavorite(label.getText()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    star.setText(favoriteStations.contains(item) ? "\u2605" : "\u2606");
                    setGraphic(box);
                }
            }
        });

        favoritesList.setCellFactory(lv -> new ListCell<>() {
            private final Button star = new Button("\u2605");
            private final Label label = new Label();
            private final HBox box = new HBox(10, label, star);


            {
                star.setOnAction(e -> toggleFavorite(label.getText()));
                star.setStyle("-fx-border-color:yellow; -fx-border-width:2;");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    label.setText(item);
                    setGraphic(box);
                }
            }
        });

        CheckBox tram = new CheckBox("Straßenbahn");
        CheckBox bus = new CheckBox("Bus");
        CheckBox subway = new CheckBox("U-Bahn");
        CheckBox suburban = new CheckBox("S-Bahn");

        tram.setSelected(true);
        bus.setSelected(true);
        subway.setSelected(true);
        suburban.setSelected(true);

        VBox filterBox = new VBox(5, tram, bus, subway, suburban);
        filterBox.setPadding(new Insets(5));
        filterBox.setStyle("-fx-border-color:red; -fx-border-width:2;");

        VBox left = new VBox(10, new Label("Haltestellen"), stationList);
        VBox right = new VBox(
                10,
                new Label("Favoriten"),
                favoritesList,
                new Label("Verkehrsmittel"),
                filterBox,
                linesList
        );

        VBox.setVgrow(stationList, Priority.ALWAYS);
        VBox.setVgrow(favoritesList, Priority.ALWAYS);
        VBox.setVgrow(linesList, Priority.ALWAYS);

        HBox center = new HBox(10, left, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        root.setCenter(center);

        Runnable applyFilter = () -> {
            filteredLines.clear();
            String selected = stationList.getSelectionModel().getSelectedItem();
            if (selected == null || initialStations == null) return;

            initialStations.stream()
                    .filter(s -> s.getName().equals(selected))
                    .findFirst()
                    .ifPresent(station -> {
                        for (LineStation l : station.getLines()) {
                            String t = l.getTypeOfTransportation().toLowerCase();
                            if ((tram.isSelected() && t.contains("tram")) ||
                                    (bus.isSelected() && t.contains("bus")) ||
                                    (subway.isSelected() && t.contains("subway")) ||
                                    (suburban.isSelected() && t.contains("suburban"))) {
                                filteredLines.add(l.getName() + " → " + l.getDirection());
                            }
                        }
                    });
        };

        stationList.getSelectionModel().selectedItemProperty()
                .addListener((a, b, c) -> applyFilter.run());
        tram.setOnAction(e -> applyFilter.run());
        bus.setOnAction(e -> applyFilter.run());
        subway.setOnAction(e -> applyFilter.run());
        suburban.setOnAction(e -> applyFilter.run());

        /* ---------- SEARCH ACTION --------- */
        searchButton.setOnAction(e -> {
            String address = searchField.getText();
            logSearch(address);

            stationNames.clear();
            filteredLines.clear();

            addressLookupService = new AddressLookupService(address);
            addressLookupService.setOnSucceeded(ev -> {
                initialStations = addressLookupService.getValue();
                if (initialStations != null) {
                    initialStations.forEach(s -> stationNames.add(s.getName()));
                }
            });
            addressLookupService.start();
        });

        Button toggleTheme = new Button("Light / Dark Mode");
        toggleTheme.setStyle("-fx-border-color: red; -fx-border-width: 2;");

        toggleTheme.setOnAction(e -> {
            Scene scene = toggleTheme.getScene();
            Parent r = scene.getRoot();
            boolean dark = !"dark".equals(scene.getUserData());

            if (dark) {
                r.setStyle("-fx-background-color:#2b2b2b;-fx-text-fill:white;");
                funFactBox.setStyle("-fx-border-color:red;-fx-background-color:#3a3a3a;");
                filterBox.getChildren().forEach(n -> ((CheckBox) n).setTextFill(javafx.scene.paint.Color.WHITE));
                scene.setUserData("dark");
            } else {
                r.setStyle("-fx-background-color:#87CEFA;-fx-text-fill:black;");
                funFactBox.setStyle("-fx-border-color:red;-fx-background-color:white;");
                filterBox.getChildren().forEach(n -> ((CheckBox) n).setTextFill(javafx.scene.paint.Color.BLACK));
                scene.setUserData("light");
            }
        });

        root.setBottom(toggleTheme);
        return root;
    }

    private void toggleFavorite(String station) {
        if (favoriteStations.contains(station)) favoriteStations.remove(station);
        else favoriteStations.add(station);
        saveFavorites();
    }

    private void saveFavorites() {
        try {
            Files.writeString(FAVORITES_FILE, favoriteStations.toString(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) {}
    }

    private void loadFavorites() {
        try {
            if (Files.exists(FAVORITES_FILE)) {
                String c = Files.readString(FAVORITES_FILE).replace("[","").replace("]","");
                if (!c.isBlank()) favoriteStations.setAll(c.split(", "));
            }
        } catch (IOException ignored) {}
    }

    private void logSearch(String address) {
        try {
            Files.writeString(SEARCH_LOG,
                    "{ \"address\":\"" + address + "\",\"time\":\"" + LocalDateTime.now() + "\" }\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {}
    }
}
