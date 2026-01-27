package org.example.project_wobimich.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;
import org.example.project_wobimich.service.LineLookupService;
import org.example.project_wobimich.utils.LineStationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.project_wobimich.utils.LineStationUtils.updateCountdownMinutes;

public class WobimichController {
    // Path
    private static final Path SEARCH_LOG = Paths.get("search-history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.txt");
    private static final int MAX_REFRESH = 5;
    //View
    private final WobimichView view;
    /**
     * private ObservableList<Station> stations = FXCollections.observableArrayList();
     * private ObservableList<LineStation> lines = FXCollections.observableArrayList();
     * private ObservableList<Station> favoriteStations = FXCollections.observableArrayList();
     */
    // Data Lists (Observable for automatic UI synchronization)
    private ObservableList<Station> stations;
    private ObservableList<LineStation> lines;
    private ObservableList<Station> favoriteStations;
    //Data List for lines of a selected station
    private List<LineStation> linesForSelectedStation = new ArrayList<>();
    // Services
    private AddressLookupService addressLookupService;
    private LineLookupService lineLookupService;
    private Station currentStation;
    private boolean tramSelected;
    private boolean busSelected;
    private boolean subwaySelected;
    // Timeline
    private Timeline refreshTimeline;
    private int refreshCounter = 0;

    /**
     * Creates a new controller with the given view.
     *
     * @param view the view to be controlled by this controller
     *
     */
    public WobimichController(WobimichView view) {
        this.view = view;
        this.stations = view.getStations();
        this.lines = view.getLines();
        this.favoriteStations = view.getFavoriteStations();
    }

    /**
     * Loads a predefined set of stations for the initial UI display.
     */
    public void loadDefaultStations() {
        stations.setAll(new Station("60200569", "Höchstädtplatz", 16.3769075, 48.2392428),
                new Station("60200345", "Franz-Josefs-Bahnhof", 16.361151, 48.2259888),
                new Station("60200743", "Mitte-Landstraße", 16.3845881, 48.2060445),
                new Station("60200491", "Heiligenstadt", 16.3657773, 48.2490958),
                new Station("60201468", "Westbahnhof", 16.3376511, 48.1966562));
    }

    /**
     * Performs a search for the address entered the search bar,
     * logs the search, and updates the station list.
     */
    public void searchAddress(String address) {
        logSearch(address);
        stations.clear();

        addressLookupService = new AddressLookupService(address);
        addressLookupService.setOnSucceeded(e -> stations.setAll(addressLookupService.getValue()));
        addressLookupService.setOnFailed(e -> showError("Die eingegebene Adresse konnte nicht" +
                " verarbeitet werden!"));
        addressLookupService.start();
    }

    /**
     * Logs the search query along with timestamp to a local file.
     */
    private void logSearch(String address) {
        try {
            String logEntry = String.format("{ \"address\":\"%s\", \"time\":\"%s\" }\n", address, LocalDateTime.now());
            Files.writeString(SEARCH_LOG, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Logging fehlgeschlagen: " + e.getMessage());
        }
    }

    /**
     * Loads real-time departures for the selected station and applies the current filters.
     */
    public void loadLinesForSelectedStation(Station station, boolean tramSelected, boolean busSelected,
                                            boolean subwaySelected) {
        if (station == null) {
            return;
        }
        this.currentStation = station;
        this.tramSelected = tramSelected;
        this.busSelected = busSelected;
        this.subwaySelected = subwaySelected;

        fetchLinesOnce();
        startRefreshTimer();

    }

    /**
     * Applies the selected transportation type filters to the departure list.
     */
    public void applyFilter(boolean tramSelected, boolean busSelected, boolean subwaySelected) {
        lines.clear();
        for (LineStation line : linesForSelectedStation) {
            if (line != null) {
                String type = line.getTypeOfTransportation().toLowerCase();
                boolean matchesTram = tramSelected && type.contains("pttram");
                boolean matchesBus = busSelected && type.contains("ptbuscity");
                boolean matchesSubway = subwaySelected && type.contains("ptmetro");
                if (matchesTram || matchesBus || matchesSubway) {
                    lines.add(line);
                }
            }
        }
    }

    /**
     * Loads favorites from the text file (favorites.txt) during initialization.
     */
    public void loadFavorites() {
        try {
            if (Files.exists(FAVORITES_FILE)) {
                List<String> loadedNames = Files.readAllLines(FAVORITES_FILE);
                List<Station> loadedStation = new ArrayList<>();
                for (String name : loadedNames) {
                    for (Station s : stations) {
                        if (s.getName().equals(name)) {
                            loadedStation.add(s);
                            break;
                        }
                    }
                }
                favoriteStations.setAll(loadedStation);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Favoriten: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of favorite stations to a local text file (favorites.txt).
     */
    public void saveFavorites() {
        try {
            List<String> names = new ArrayList<>();
            for (Station s : favoriteStations) {
                names.add(s.getName());
            }
            Files.write(FAVORITES_FILE, names, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Favoriten: " + e.getMessage());
        }
    }

    /**
     * Toggles between light and dark mode for the UI.
     */
    public boolean toggleLightDarkMode(BorderPane root, boolean isDarkMode) {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            root.setStyle("-fx-base: #2b2b2b; -fx-background-color: #3c3f41; -fx-font-size: 14px;");
        } else {
            root.setStyle("-fx-font-size: 14px;-fx-background-color: #ADD8E6;");
        }
        return isDarkMode;
    }

    /**
     * Shows an error message in an alert dialog.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows the departure times for the selected station.
     */
    private void fetchLinesOnce() {
        if (currentStation == null) return;
        if (lineLookupService != null && lineLookupService.isRunning()) {
            lineLookupService.cancel();
        }
        lineLookupService = new LineLookupService(currentStation.getId());
        lineLookupService.setOnSucceeded(e -> {
            List<LineStation> result = lineLookupService.getValue();
            linesForSelectedStation = result;
            LineStationUtils.updateCountdownMinutes(linesForSelectedStation);
            applyFilter(tramSelected, busSelected, subwaySelected);
        });
        lineLookupService.setOnFailed(e -> showError("Abfahrtszeiten nicht verfügbar!"));
        lineLookupService.start();
    }

    /**
     * Starts a timer to periodically refresh the departure times
     * <p>
     * The timer operates with a fixed interval of 60 seconds.
     * <p>
     * It continues refreshing until {@code MAX_REFRESH} is reached.
     * Once the limit is exceeded, the timer stops automatically.
     */
    private void startRefreshTimer() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        refreshCounter = 0;
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(45), ev -> {
            refreshCounter++;
            fetchLinesOnce();
            if (refreshCounter > MAX_REFRESH) {
                refreshTimeline.stop();
            }
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.playFromStart();
    }

    /**
     * Stops the refresh timer if it is running.
     */
    public void stopRefreshTimer() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }
}

