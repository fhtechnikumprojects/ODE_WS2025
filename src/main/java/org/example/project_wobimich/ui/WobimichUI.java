package org.example.project_wobimich.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Main UI class for the "Wobimich" application.
 * This class handles the graphical user interface, user interactions,
 * and bridges the UI with backend services for station and departure lookups.
 */
public class WobimichUI {
    // Services
    private AddressLookupService addressLookupService;
    private LineLookupService lineLookupService;

    // Data Lists (Observable for automatic UI synchronization)
    private ObservableList<Station> stations = FXCollections.observableArrayList();
    private ObservableList<LineStation> lines = FXCollections.observableArrayList();
    private ObservableList<String> favoriteStations = FXCollections.observableArrayList(); // Vorbereitet
    private List<LineStation> linesOfSelectedStation = new ArrayList<>(); // Zwischenspeicher für Filter

    // UI Controls: Filters & Settings
    private CheckBox tram = new CheckBox("Straßenbahn");
    private CheckBox bus = new CheckBox("Bus");
    private CheckBox subway = new CheckBox("U-Bahn");
    private TextField searchField = new TextField();
    private boolean isDarkMode = false;

    // UI Controls: List Views
    private ListView<Station> stationList = new ListView<>(stations);
    private ListView<LineStation> lineList = new ListView<>(lines);
    private ListView<String> favoriteListView = new ListView<>(favoriteStations); // Die neue Box

    // Path
    private static final Path SEARCH_LOG = Paths.get("search-history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.txt");

    /**
     * Initializes and constructs the main application scene.
     * @return A configured BorderPane containing the full UI layout.
     */
    public BorderPane createScene() {
        loadFavorites();

        // Automatically save favorites to file whenever the list changes
        favoriteStations.addListener((ListChangeListener<String>) c -> saveFavorites());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-font-size: 14px; -fx-background-color: #ADD8E6;");

        // Set up the high-level layout structure
        root.setTop(createTopSection());
        root.setCenter(createCenterSection());
        root.setBottom(createBottomSection(root));

        loadDefaultStations();

        return root;
    }

    /**
     * Creates the top section including the fun fact box and the search bar.
     */
    private VBox createTopSection() {
        VBox topVBox = new VBox(10);
        topVBox.setPadding(new Insets(10));
        topVBox.setStyle("-fx-background-color: #ADD8E6;");

        // Fun Fact display area
        VBox funFactBox = new VBox(5);
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.getStyleClass().add("fun-fact-box");
        funFactBox.setStyle("-fx-border-color: darkred; -fx-border-width: 2; -fx-background-color: lightgray;");

        Label funFactHeader = new Label("Hast du gewusst?");
        Label funFactText = new Label(FunFactUtils.getRandomFact());
        funFactBox.getChildren().addAll(funFactHeader, funFactText);

        // Search bar setup
        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("-fx-border-color: darkred;");
        searchField.setPromptText("Standort eingeben:");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Suche");

        // Disable button if search field is empty
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
        searchButton.setOnAction(e -> handleSearch());
        searchButton.setStyle("-fx-border-color: darkred; -fx-border-width: 2;");

        searchBar.getChildren().addAll(searchField, searchButton);
        topVBox.getChildren().addAll(funFactBox, searchBar);

        return topVBox;
    }

    /**
     * Creates the center section containing station lists, favorites, and departures.
     */
    private HBox createCenterSection() {
        HBox centerBox = new HBox(15);
        centerBox.setPadding(new Insets(10, 0, 10, 0));

        // Left Column: Stations & Favorites
        VBox leftColumn = new VBox(15);
        VBox stationArea = new VBox(5, new Label("Haltestellen"), stationList);
        stationList.setPrefHeight(200);
        stationArea.setStyle("-fx-border-color: darkred;");

        VBox favoriteArea = new VBox(5, new Label("Favoriten"), favoriteListView);
        VBox.setVgrow(favoriteListView, Priority.ALWAYS);
        VBox.setVgrow(favoriteArea, Priority.ALWAYS);
        favoriteArea.setStyle("-fx-border-color: darkred;");

        leftColumn.getChildren().addAll(stationArea, favoriteArea);
        setupBoxStyle(leftColumn, 280);

        // Right Column: Filters & Departure Display
        VBox rightColumn = new VBox(5);
        Label departuresLabel = new Label("Abfahrten & Filter");

        VBox combinedContainer = new VBox(10);
        combinedContainer.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-border-color: darkred; -fx-padding: 10;");

        VBox filterContent = createFilterSection();
        filterContent.setStyle("-fx-border-color: darkred;");

        VBox.setVgrow(lineList, Priority.ALWAYS);

        combinedContainer.getChildren().addAll(filterContent, new Separator(), lineList);
        VBox.setVgrow(combinedContainer, Priority.ALWAYS);

        rightColumn.getChildren().addAll(departuresLabel, combinedContainer);
        setupBoxStyle(rightColumn, 350);

        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        // Handle station selection on double click
        stationList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) handleStationSelection();
        });

        centerBox.getChildren().addAll(leftColumn, rightColumn);

        stationList.setCellFactory(lv -> new ListCell<Station>() {
            @Override
            protected void updateItem(Station station, boolean empty) {
                super.updateItem(station, empty);
                if (empty || station == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox cell = new HBox(10);
                    cell.setAlignment(Pos.CENTER_LEFT);
                    Label name = new Label(station.getName());
                    Button starButton = new Button("☆");

                    starButton.setOnAction(e -> {
                        if (!favoriteStations.contains(station.getName())) {
                            favoriteStations.add(station.getName());
                        }
                    });

                    HBox.setHgrow(name, Priority.ALWAYS);
                    cell.getChildren().addAll(name, starButton);
                    setGraphic(cell);
                }
            }
        });

        favoriteListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String stationName, boolean empty) {
                super.updateItem(stationName, empty);
                if (empty || stationName == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox cell = new HBox(10);
                    cell.setAlignment(Pos.CENTER_LEFT);
                    Label name = new Label(stationName);
                    Button removeButton = new Button("★");

                    removeButton.setOnAction(e -> favoriteStations.remove(stationName));

                    HBox.setHgrow(name, Priority.ALWAYS);
                    cell.getChildren().addAll(name, removeButton);
                    setGraphic(cell);
                }
            }
        });

        favoriteListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String selectedFav = favoriteListView.getSelectionModel().getSelectedItem();
                if (selectedFav != null) {
                    addressLookupService = new AddressLookupService(selectedFav);

                    addressLookupService.setOnSucceeded(event -> {
                        stations.setAll(addressLookupService.getValue());

                        for (Station s : stations) {
                            if (s.getName().equalsIgnoreCase(selectedFav)) {
                                stationList.getSelectionModel().select(s);
                                handleStationSelection();
                                break;
                            }
                        }
                    });

                    addressLookupService.setOnFailed(event -> showError("Verbindung fehlgeschlagen!"));
                    addressLookupService.start();
                }
            }
        });

        return centerBox;
    }

    /**
     * Creates the bottom section containing the toggle button for dark/light mode.
     */
    private HBox createBottomSection(BorderPane root) {
        Button toggleButton = new Button("Light/Dark Mode");
        toggleButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(toggleButton, Priority.ALWAYS);

        toggleButton.setOnAction(e -> toggleTheme(root));
        toggleButton.setStyle("-fx-background-color: lightgray; -fx-border-color: darkred; -fx-border-width: 2;");

        return new HBox(10, toggleButton);
    }

    /**
     * Executes the search for a specific address and updates the UI.
     */
    private void handleSearch() {
        String address = searchField.getText();
        logSearch(address);

        addressLookupService = new AddressLookupService(address);
        stations.clear();

        addressLookupService.setOnSucceeded(e -> stations.setAll(addressLookupService.getValue()));
        addressLookupService.setOnFailed(e -> showError("Adresse konnte nicht verarbeitet werden!"));
        addressLookupService.start();
    }

    /**
     * Retrieves departure data for the selected station and applies current filters.
     */
    private void handleStationSelection() {
        Station selected = stationList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        lineLookupService = new LineLookupService(selected.getId());
        lineLookupService.setOnSucceeded(e -> {
            linesOfSelectedStation = lineLookupService.getValue();
            applyFilter();
        });
        lineLookupService.setOnFailed(e -> showError("Abfahrtszeiten nicht verfügbar!"));
        lineLookupService.start();
    }

    // Default stations shown on startup
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
        box.setPrefWidth(prefWidth);
        HBox.setHgrow(box, Priority.ALWAYS);
        VBox.setVgrow(box, Priority.ALWAYS);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Logs the search query and timestamp to a local JSON file.
     */
    private void logSearch(String address) {
        try {
            String logEntry = String.format("{ \"address\":\"%s\", \"time\":\"%s\" }\n", address, LocalDateTime.now());
            Files.writeString(SEARCH_LOG, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Logging fehlgeschlagen: " + e.getMessage());
        }
    }

    // Filter UI
    private VBox createFilterSection() {
        VBox filterContainer = new VBox(5, new Label("Verkehrsmittel filtern:"));
        filterContainer.setPadding(new Insets(5));
        filterContainer.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");

        tram.setSelected(true);
        bus.setSelected(true);
        subway.setSelected(true);

        tram.setOnAction(e -> applyFilter());
        bus.setOnAction(e -> applyFilter());
        subway.setOnAction(e -> applyFilter());

        HBox checks = new HBox(10, tram, bus, subway);
        filterContainer.getChildren().add(checks);
        return filterContainer;
    }

    /**
     * Filters the departure list based on selected transportation types (Tram, Bus, Subway).
     */
    private void applyFilter() {
        lines.clear();

        for (LineStation line : linesOfSelectedStation) {
            String type = line.getTypeOfTransportation().toLowerCase();

            boolean matchesTram = tram.isSelected() && type.contains("pttram");
            boolean matchesBus = bus.isSelected() && type.contains("ptbuscity");
            boolean matchesSubway = subway.isSelected() && type.contains("ptmetro");

            if (matchesTram || matchesBus || matchesSubway) {
                lines.add(line);
            }
        }
    }

    /**
     * Saves the current list of favorite stations to a local text file.
     */
    private void saveFavorites() {
        try {
            Files.write(FAVORITES_FILE, favoriteStations, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Favoriten: " + e.getMessage());
        }
    }

    /**
     * Loads favorites from the text file during initialization.
     */
    private void loadFavorites() {
        try {
            if (Files.exists(FAVORITES_FILE)) {
                List<String> loaded = Files.readAllLines(FAVORITES_FILE);
                favoriteStations.setAll(loaded);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Favoriten: " + e.getMessage());
        }
    }

    /**
     * Toggles between Light Mode and Dark Mode for the entire scene.
     */
    private void toggleTheme(BorderPane root) {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            root.setStyle("-fx-base: #2b2b2b; -fx-background-color: #3c3f41; -fx-font-size: 14px;");
        } else {
            root.setStyle("-fx-font-size: 14px;-fx-background-color: #ADD8E6;");
        }
    }

    // Helper Methods for Layout and Interaction


}