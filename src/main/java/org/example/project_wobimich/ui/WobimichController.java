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

/**
 * Controller class for the Wobimich application.
 * <p>
 * Handles application logic, data coordination and communication
 * between the UI layer and backend services.
 * <p>
 * Responsibilities include:
 * <ul>
 *     <li>Address search and station lookup</li>
 *     <li>Fetching and filtering departure data</li>
 *     <li>Managing favorite stations</li>
 *     <li>Persisting search history and favorites</li>
 * </ul>
 */
public class WobimichController {
    // Data Lists (Observable for automatic UI synchronization)
    private ObservableList<Station> stations = FXCollections.observableArrayList();
    private ObservableList<LineStation> lines = FXCollections.observableArrayList();
    private ObservableList<Station> favoriteStations = FXCollections.observableArrayList();

    // Cached list of all lines for the currently selected station
    private List<LineStation> linesForSelectedStation = new ArrayList<>();

    // Service for resolving addresses into stations/for fetching departure lines for a station
    private AddressLookupService addressLookupService;
    private LineLookupService lineLookupService;

    // Path for search history/favorite stations
    private static final Path SEARCH_LOG = Paths.get("search-history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.txt");

    // Variables for updating departure time of lines every 20 seconds
    private Timeline refreshTimeline;
    private Station currentStation;
    private boolean tramSelected;
    private boolean busSelected;
    private boolean subwaySelected;

    /**
     * Creates a new controller with the given data.
     *
     * @param stations observablelist of station
     * @param lines observablelist of lines
     * @param favoriteStations observablelist of stations that are added to favorite
     */
    public WobimichController(ObservableList<Station> stations, ObservableList<LineStation> lines, ObservableList<Station> favoriteStations) {
        this.stations = stations;
        this.lines = lines;
        this.favoriteStations = favoriteStations;
    }

    /**
     * Loads a predefined set of stations for the initial UI display.
     */
    public void loadDefaultStations() {
        stations.setAll(
            new Station("60200569","Höchstädtplatz",16.3769075,48.2392428),
            new Station("60200345","Franz-Josefs-Bahnhof",16.361151,48.2259888),
            new Station("60200743","Mitte-Landstraße",16.3845881,48.2060445),
            new Station("60200491","Heiligenstadt",16.3657773,48.2490958),
            new Station("60201468","Westbahnhof",16.3376511,48.1966562)
        );
    }

    /**
     * Performs an asynchronous address search based on user input.
     * <p>
     * The search query is logged locally before the station list
     * is replaced with the search results.
     *
     * @param address the address entered by the user
     */
    public void searchAddress(String address) {
        logSearch(address);
        stations.clear();

        addressLookupService = new AddressLookupService(address);
        addressLookupService.setOnSucceeded(e -> stations.setAll(addressLookupService.getValue()));
        addressLookupService.setOnFailed(e -> showError("Die eingegebene Adresse konnte nicht verarbeitet werden!"));

        addressLookupService.start();
    }

    /**
     * Persists a searched address together with the current timestamp.
     * <p>
     * The data is appended to a local JSON-like log file.
     *
     * @param address the searched address
     */
    private void logSearch(String address) {
        try {
            String logEntry = String.format(
                    "{ \"address\":\"%s\", \"time\":\"%s\" }\n",
                    address,
                    LocalDateTime.now()
            );
            Files.writeString(
                    SEARCH_LOG,
                    logEntry,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Logging fehlgeschlagen: " + e.getMessage());
        }
    }

    /**
     * Loads real-time departures for the selected station and applies the current filters.
     */
    public void loadLinesForSelectedStation(Station station, boolean tramSelected, boolean busSelected, boolean subwaySelected) {
        if (station == null) {
            return;
        }

        this.currentStation = station;
        this.tramSelected = tramSelected;
        this.busSelected = busSelected;
        this.subwaySelected = subwaySelected;

        lineLookupService = new LineLookupService(station.getId());

        lineLookupService.setOnSucceeded(e -> {
            linesForSelectedStation = lineLookupService.getValue();
            applyFilter(
                    tramSelected,
                    busSelected,
                    subwaySelected
            );
        });

        lineLookupService.setOnFailed(e -> showError("Abfahrtszeiten nicht verfügbar!"));

        lineLookupService.start();
        startAutoRefresh();
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
     * Toggles between light and dark mode for the application UI.
     *
     * @param root the root pane whose style is updated
     * @param isDarkMode current theme state
     * @return the updated theme state after toggling
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
     * Displays an error message in a modal alert dialog.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Updates the departure times for all lines of the currently selected station.
     * <p>
     * For each line, it calculates the remaining minutes until the next departure.
     * Past departures (negative minutes) are removed. If a line has no departures
     * left, the line data is reloaded from the service.
     * <p>
     * Uses the internal fields:
     * <ul>
     *     <li>{@link #linesForSelectedStation} – list of all lines for the current station</li>
     *     <li>{@link #tramSelected}, {@link #busSelected}, {@link #subwaySelected} – current filter settings</li>
     * </ul>
     */
    private void updateDepartures() {
        boolean needReload = false;

        for (LineStation line : linesForSelectedStation) {
            List<String> departureTimesString = line.getDepartureTimes();

            if (departureTimesString == null || departureTimesString.isEmpty()) {
                needReload = true;
                break;
            }

            String nextDeparture = departureTimesString.getFirst();
            int minutesUntilDeparture = LineStationUtils.getMinutesUntilDeparture(nextDeparture);

            if (minutesUntilDeparture < 0) {
                departureTimesString.removeFirst();

                if (departureTimesString.isEmpty()) {
                    needReload = true;
                    break;
                }
            }

        }

        if (needReload) {
            reloadLineLookupService();
        } else {
            applyFilter(tramSelected, busSelected, subwaySelected);
        }

    }

    /**
     * Reloads the departure lines from the backend service for the current station.
     * <p>
     * Fetches fresh line data asynchronously, updates the cached list of lines,
     * and reapplies the current transportation type filters.
     * <p>
     * Uses the internal fields:
     * <ul>
     *     <li>{@link #currentStation} – the station whose lines are being reloaded</li>
     *     <li>{@link #linesForSelectedStation} – list to be updated after reloading</li>
     *     <li>{@link #tramSelected}, {@link #busSelected}, {@link #subwaySelected} – current filter settings</li>
     * </ul>
     */
    private void reloadLineLookupService() {
        if (currentStation == null) {
            return;
        }

        lineLookupService = new LineLookupService(currentStation.getId());

        lineLookupService.setOnSucceeded(e -> {
            linesForSelectedStation = lineLookupService.getValue();
            applyFilter(tramSelected, busSelected, subwaySelected);
        });

        lineLookupService.start();
    }

    /**
     * Starts a periodic auto-refresh of departure times every 20 seconds.
     * <p>
     * Stops any existing timeline before creating a new one. The timeline runs
     * indefinitely and calls {@link #updateDepartures()} at each cycle.
     * <p>
     * Uses the internal field:
     * <ul>
     *     <li>{@link #refreshTimeline} – stores the timeline to control its lifecycle</li>
     * </ul>
     */
    private void startAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }

        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(20), e -> updateDepartures())
        );

        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

}
