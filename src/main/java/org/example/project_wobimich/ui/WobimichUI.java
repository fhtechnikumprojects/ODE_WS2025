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
import java.util.*;

public class WobimichUI {

    private AddressLookupService addressLookupService;

    private final ObservableList<String> stationNames = FXCollections.observableArrayList();
    private final ObservableList<String> favoriteStations = FXCollections.observableArrayList();

    private static final Path SEARCH_LOG = Paths.get("search_history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.json");

    private List<Station> initialStations = new ArrayList<>();

    public void setInitialStations(List<Station> stations) {
        this.initialStations = stations;
    }

    public BorderPane createScene() {

        loadFavorites();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox topBox = new VBox(10);

        VBox funFactBox = new VBox(5);
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.setStyle("-fx-border-color:black;-fx-background-color:lightgray;");

        funFactBox.getChildren().addAll(
                new Label("Hast du gewusst?"),
                new Label(FunFactUtils.getRandomFact())
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Standort eingeben");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Suche");
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());

        HBox searchBar = new HBox(10, searchField, searchButton);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("-fx-border-color:black;");

        topBox.getChildren().addAll(funFactBox, searchBar);
        root.setTop(topBox);


        ListView<String> stationList = new ListView<>(stationNames);

        stationList.setCellFactory(list -> new ListCell<>() {
            private final Button star = new Button("☆");
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
                    star.setText(favoriteStations.contains(item) ? "★" : "☆");
                    setGraphic(box);
                }
            }
        });

        VBox leftBox = new VBox(new Label("Haltestellen"), stationList);
        VBox.setVgrow(stationList, Priority.ALWAYS);
        leftBox.setPadding(new Insets(10));
        leftBox.setStyle("-fx-border-color:black;");

        ListView<String> favoritesList = new ListView<>(favoriteStations);

        VBox rightBox = new VBox(new Label("Favoriten"), favoritesList);
        VBox.setVgrow(favoritesList, Priority.ALWAYS);
        rightBox.setPadding(new Insets(10));
        rightBox.setStyle("-fx-border-color:black;");

        HBox centerBox = new HBox(10, leftBox, rightBox);
        root.setCenter(centerBox);



        Button toggleTheme = new Button("Light / Dark Mode");

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

        HBox bottomBox = new HBox(toggleTheme);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);


        searchButton.setOnAction(e -> {
            String address = searchField.getText();
            logSearch(address);

            stationNames.clear();

            addressLookupService = new AddressLookupService(address);
            addressLookupService.setOnSucceeded(ev -> {
                for (Station s : addressLookupService.getValue()) {
                    stationNames.add(s.getName());
                }
            });
            addressLookupService.start();
        });

        return root;
    }

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
            Files.writeString(FAVORITES_FILE, favoriteStations.toString());
        } catch (IOException ignored) {}
    }

    private void loadFavorites() {
        try {
            if (Files.exists(FAVORITES_FILE)) {
                String content = Files.readString(FAVORITES_FILE)
                        .replace("[", "")
                        .replace("]", "");
                if (!content.isBlank()) {
                    favoriteStations.setAll(content.split(", "));
                }
            }
        } catch (IOException ignored) {}
    }

    private void logSearch(String address) {
        String entry = "{ \"address\": \"" + address +
                "\", \"time\": \"" + LocalDateTime.now() + "\" }\n";

        try {
            Files.writeString(
                    SEARCH_LOG,
                    entry,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ignored) {}
    }

    public List<Station> getInitialStations() {
        return initialStations;
    }
}
