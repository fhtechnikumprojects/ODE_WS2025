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
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WobimichUI {

    private AddressLookupService addressLookupService;
    //private List<Station> initialStations = new ArrayList<>();

    private final ObservableList<String> stationNames = FXCollections.observableArrayList();
    private final ObservableList<String> favoriteStations = FXCollections.observableArrayList();
    private final ObservableList<String> filteredLines = FXCollections.observableArrayList();

    private static final Path SEARCH_LOG = Paths.get("search_history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.json");

   /* public void setInitialStations(List<Station> stations) {
        this.initialStations = stations;
        stationNames.clear();
        for (Station s : stations) {
            stationNames.add(s.getName());
        }
    }*/

    public BorderPane createScene() {
        loadFavorites();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #87CEFA;");

        VBox topBox = new VBox(10);

        VBox funFactBox = new VBox(5,
                new Label("Hast du gewusst?"),
                new Label(FunFactUtils.getRandomFact()));
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.setStyle("-fx-border-color:black;-fx-background-color:lightgray;");

        TextField searchField = new TextField();
        searchField.setPromptText("Standort eingeben");

        Button searchButton = new Button("Suche");
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());

        HBox searchBar = new HBox(10, searchField, searchButton);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("-fx-border-color:black;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        topBox.getChildren().addAll(funFactBox, searchBar);
        root.setTop(topBox);

        ListView<String> stationList = new ListView<>(stationNames);
        ListView<String> favoritesList = new ListView<>(favoriteStations);
        ListView<String> linesList = new ListView<>(filteredLines);

        stationList.setCellFactory(list -> new ListCell<>() {
            private final Button star = new Button("\u2606");
            private final HBox box = new HBox(10);
            private final Label label = new Label();

            {
                star.setOnAction(e -> toggleFavorite(label.getText()));
                box.getChildren().addAll(label, star);
            }

            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    label.setText(item);
                    star.setText(favoriteStations.contains(item) ? "\u2605" : "\u2606");
                    setGraphic(box);
                }
            }
        });

        favoritesList.setCellFactory(list -> new ListCell<>() {
            private final Button star = new Button("\u2605");
            private final HBox box = new HBox(10);
            private final Label label = new Label();

            {
                star.setOnAction(e -> toggleFavorite(label.getText()));
                box.getChildren().addAll(label, star);
            }

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
        filterBox.setStyle("-fx-border-color:black;");

        VBox leftBox = new VBox(10, new Label("Haltestellen"), stationList);
        VBox rightBox = new VBox(10,
                new Label("Favoriten"), favoritesList,
                new Label("Verkehrsmittel"), filterBox, linesList);

        VBox.setVgrow(stationList, Priority.ALWAYS);
        VBox.setVgrow(favoritesList, Priority.ALWAYS);
        VBox.setVgrow(linesList, Priority.ALWAYS);

        root.setCenter(new HBox(10, leftBox, rightBox));

        Runnable applyFilter = () -> {
            filteredLines.clear();
            String selected = stationList.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Station station = initialStations.stream()
                    .filter(s -> s.getName().equals(selected))
                    .findFirst()
                    .orElse(null);

            if (station == null || station.getLines() == null) return;

            for (LineStation l : station.getLines()) {
                String t = l.getTypeOfTransportation().toLowerCase();
                if ((tram.isSelected() && t.contains("tram")) ||
                        (bus.isSelected() && t.contains("bus")) ||
                        (subway.isSelected() && t.contains("subway")) ||
                        (suburban.isSelected() && t.contains("suburban"))) {
                    filteredLines.add(l.getName() + " → " + l.getDirection());
                }
            }
        };

        stationList.getSelectionModel().selectedItemProperty().addListener((a,b,c)->applyFilter.run());
        tram.setOnAction(e->applyFilter.run());
        bus.setOnAction(e->applyFilter.run());
        subway.setOnAction(e->applyFilter.run());
        suburban.setOnAction(e->applyFilter.run());

        searchButton.setOnAction(e -> {
            String address = searchField.getText();
            logSearch(address);
            stationNames.clear();
            filteredLines.clear();

            addressLookupService = new AddressLookupService(address);
            addressLookupService.setOnSucceeded(ev -> {
                initialStations = addressLookupService.getValue();
                if (initialStations != null)
                    initialStations.forEach(s -> stationNames.add(s.getName()));
            });
            addressLookupService.start();
        });

        Button toggleTheme = new Button("Light / Dark Mode");
        toggleTheme.setOnAction(e -> {
            Scene scene = toggleTheme.getScene();
            Parent r = scene.getRoot();
            if (!"dark".equals(scene.getUserData())) {
                r.setStyle("-fx-background-color:#2b2b2b;-fx-text-fill:white;");
                scene.setUserData("dark");
            } else {
                r.setStyle("-fx-background-color:#87CEFA;-fx-text-fill:black;");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFavorites() {
        try {
            if (Files.exists(FAVORITES_FILE)) {
                String c = Files.readString(FAVORITES_FILE)
                        .replace("[","").replace("]","");
                if (!c.isBlank()) favoriteStations.setAll(c.split(", "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logSearch(String address) {
        try {
            Files.writeString(SEARCH_LOG,
                    "{ \"address\":\""+address+"\",\"time\":\""+LocalDateTime.now()+"\" }\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
