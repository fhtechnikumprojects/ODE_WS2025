package org.example.project_wobimich.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.project_wobimich.FunFactUtils;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WobimichUI {

    private AddressLookupService addressLookupService;
    private List<Station> initialStations = new ArrayList<>();

    private final ObservableList<String> stationNames = FXCollections.observableArrayList();
    private final ObservableList<String> favoriteStations = FXCollections.observableArrayList();

    private static final Path SEARCH_LOG = Paths.get("search_history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.json");

    // initial stations setzen
    public void setInitialStations(List<Station> stations) {
        this.initialStations = stations;
        stationNames.clear();
        for (Station s : stations) {
            stationNames.add(s.getName());
        }
    }

    public BorderPane createScene() {
        loadFavorites();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // --- Top Box ---
        VBox topBox = new VBox(10);

        VBox funFactBox = new VBox(5);
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.setStyle("-fx-border-color:black;-fx-background-color:lightgray;");

        funFactBox.getChildren().addAll(
                new Label("Hast du gewusst?"),
                new Label(FunFactUtils.getRandomFact())
        );

        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("-fx-border-color:black;");

        TextField searchField = new TextField();
        searchField.setPromptText("Standort eingeben");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Suche");
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());

        searchBar.getChildren().addAll(searchField, searchButton);
        topBox.getChildren().addAll(funFactBox, searchBar);
        root.setTop(topBox);

        // --- Center Box ---
        HBox centerBox = new HBox(10);

        // Station List mit Stern-Favoriten
        ListView<String> stationList = new ListView<>(stationNames);
        stationList.setCellFactory(list -> new ListCell<>() {
            private final Button star = new Button("\u2606");
            private final HBox box = new HBox(10);
            private final Label label = new Label();

            {
                star.setOnAction(e -> toggleFavorite(label.getText()));
                box.getChildren().addAll(label, star);
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

        VBox leftBox = new VBox(new Label("Haltestellen"), stationList);
        VBox.setVgrow(stationList, Priority.ALWAYS);
        leftBox.setPadding(new Insets(10));
        leftBox.setStyle("-fx-border-color:black;");

        // Favorites List mit Stern zum Entfernen
        ListView<String> favoritesList = new ListView<>(favoriteStations);
        favoritesList.setCellFactory(list -> new ListCell<>() {
            private final Button star = new Button("\u2605");
            private final HBox box = new HBox(10);
            private final Label label = new Label();

            {
                star.setOnAction(e -> toggleFavorite(label.getText())); // entfernt bei Klick
                box.getChildren().addAll(label, star);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    star.setText("\u2605"); // immer gefüllt in Favoriten
                    setGraphic(box);
                }
            }
        });

        VBox rightBox = new VBox(new Label("Favoriten"), favoritesList);
        VBox.setVgrow(favoritesList, Priority.ALWAYS);
        rightBox.setPadding(new Insets(10));
        rightBox.setStyle("-fx-border-color:black;");

        centerBox.getChildren().addAll(leftBox, rightBox);
        root.setCenter(centerBox);

        // --- Bottom Box: Toggle Theme ---
        Button toggleTheme = new Button("Light / Dark Mode");
        toggleTheme.setMaxWidth(Double.MAX_VALUE);

        toggleTheme.setOnAction(e -> {
            Scene scene = toggleTheme.getScene();
            if (scene == null) return;
            Parent r = scene.getRoot();

            if (!"dark".equals(scene.getUserData())) {
                r.setStyle("-fx-background-color:#2b2b2b;-fx-text-fill:white;");
                scene.setUserData("dark");
            } else {
                r.setStyle("-fx-background-color:white;-fx-text-fill:black;");
                scene.setUserData("light");
            }
        });

        root.setBottom(toggleTheme);

        // --- Search Button Action ---
        searchButton.setOnAction(e -> {
            String address = searchField.getText();
            logSearch(address);

            stationNames.clear();

            addressLookupService = new AddressLookupService(address);
            addressLookupService.setOnSucceeded(ev -> {
                List<Station> results = addressLookupService.getValue();
                if (results != null) {
                    for (Station s : results) {
                        stationNames.add(s.getName());
                    }
                }
            });
            addressLookupService.start();
        });

        // Initialstations beim Start anzeigen
        if (!initialStations.isEmpty()) {
            stationNames.clear();
            for (Station s : initialStations) {
                stationNames.add(s.getName());
            }
        }

        return root;
    }

    // --- Favorite Handling ---
    private void toggleFavorite(String station) {
        if (favoriteStations.contains(station)) {
            favoriteStations.remove(station);
        } else {
            favoriteStations.add(station);
        }
        saveFavorites();
    }

    private void saveFavorites() {
        try {
            Files.writeString(FAVORITES_FILE, favoriteStations.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFavorites() {
        try {
            if (Files.exists(FAVORITES_FILE)) {
                String content = Files.readString(FAVORITES_FILE)
                        .replace("[", "")
                        .replace("]", "")
                        .replace(", ", "\n");
                if (!content.isBlank()) {
                    favoriteStations.setAll(content.split("\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Search Logging ---
    private void logSearch(String address) {
        String entry = "{ \"address\": \"" + address + "\", \"time\": \"" + LocalDateTime.now() + "\" }\n";
        try {
            Files.writeString(SEARCH_LOG, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
