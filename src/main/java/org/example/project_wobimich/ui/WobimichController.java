package org.example.project_wobimich.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.service.AddressLookupService;
import org.example.project_wobimich.service.LineLookupService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WobimichController {
    // Data Lists (Observable for automatic UI synchronization)
    private ObservableList<Station> stations = FXCollections.observableArrayList();
    private ObservableList<LineStation> lines = FXCollections.observableArrayList();
    private ObservableList<Station> favoriteStations = FXCollections.observableArrayList();

    //Data List for lines of a selected station
    private List<LineStation> linesForSelectedStation = new ArrayList<>();

    // Services
    private AddressLookupService addressLookupService;
    private LineLookupService lineLookupService;

    // Path
    private static final Path SEARCH_LOG = Paths.get("search-history.json");
    private static final Path FAVORITES_FILE = Paths.get("favorites.txt");

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
     * Performs a search for the address entered the search bar,
     * logs the search, and updates the station list.
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
     * Logs the search query along with timestamp to a local file.
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
     * */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
