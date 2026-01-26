package org.example.project_wobimich.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.utils.FunFactUtils;
import org.example.project_wobimich.model.Station;


/**
 * Main UI class for the "Wobimich" application.
 * <p>
 * Handles the graphical user interface, user interactions, and bridges the UI
 * with backend services for station lookup, address search, and real-time departure data.
 * <p>
 * Provides functionality for:
 * <ul>
 *     <li>Searching stations by address</li>
 *     <li>Viewing station departures with filters</li>
 *     <li>Maintaining favorite stations</li>
 *     <li>Displaying fun facts</li>
 *     <li>Light/Dark mode toggle</li>
 * </ul>
 */
public class WobimichView {
    // Data Lists (Observable for automatic UI synchronization)
    private ObservableList<Station> stations = FXCollections.observableArrayList();
    private ObservableList<LineStation> lines = FXCollections.observableArrayList();
    private ObservableList<Station> favoriteStations = FXCollections.observableArrayList();

    //User input in search bar
    private TextField searchTextField = new TextField();

    //Checkbox transportations
    private CheckBox tramCheckbox = new CheckBox("Straßenbahn");
    private CheckBox busCheckbox = new CheckBox("Bus");
    private CheckBox subwayCheckbox = new CheckBox("U-Bahn");

    //State darkmode
    private boolean isDarkMode = false;

    // UI Controls: List Views
    private ListView<Station> stationListView = new ListView<>(stations);
    private ListView<LineStation> lineListView = new ListView<>(lines);
    private ListView<Station> favoriteListView = new ListView<>(favoriteStations);

    private WobimichController controller;

    /**
     * Initializes and constructs the main application scene.
     * <p>
     * Sets up the layout structure, loads default stations, favorites,
     * and configures event listeners for user interaction.
     *
     * @return A configured BorderPane containing the full UI layout.
     */
    public BorderPane createScene() {
        controller = new WobimichController(stations, lines,favoriteStations);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-font-size: 14px; -fx-background-color: #ADD8E6;");

        this.controller.loadDefaultStations();
        this.controller.loadFavorites();

        favoriteStations.addListener((ListChangeListener<Station>) c -> controller.saveFavorites());

        root.setTop(createTopSection());
        root.setCenter(createCenterSection());
        root.setBottom(createBottomSection(root));

        return root;
    }

    /**
     * Creates the top section including:
     * <ul>
     *     <li>Fun Fact display box</li>
     *     <li>Search bar for entering a location</li>
     * </ul>
     *
     * @return VBox containing the top UI section.
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
        searchTextField.setPromptText("Standort eingeben:");
        HBox.setHgrow(searchTextField, Priority.ALWAYS);

        Button searchButton = new Button("Suche");
        searchButton.disableProperty().bind(searchTextField.textProperty().isEmpty());
        searchButton.setOnAction(e ->
            this.controller.searchAddress(searchTextField.getText())
        );
        searchButton.setStyle("-fx-border-color: darkred; -fx-border-width: 2;");

        searchBar.getChildren().addAll(searchTextField, searchButton);
        topVBox.getChildren().addAll(funFactBox, searchBar);

        return topVBox;
    }

    private VBox centerLeftColumn() {
        VBox leftColumn = new VBox(15);
        VBox stationArea = new VBox(5, new Label("Haltestellen"), stationListView);
        stationListView.setPrefHeight(200);
        stationArea.setStyle("-fx-border-color: darkred;");

        VBox favoriteArea = new VBox(5, new Label("Favoriten"), favoriteListView);
        VBox.setVgrow(favoriteListView, Priority.ALWAYS);
        VBox.setVgrow(favoriteArea, Priority.ALWAYS);
        favoriteArea.setStyle("-fx-border-color: darkred;");

        leftColumn.getChildren().addAll(stationArea, favoriteArea);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        setupBoxStyle(leftColumn, 280);

        return leftColumn;
    }

    private VBox centerRightColumn() {
        VBox rightColumn = new VBox(5);
        Label departuresLabel = new Label("Abfahrten & Filter");

        VBox combinedContainer = new VBox(10);
        combinedContainer.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-border-color: darkred; -fx-padding: 10;");

        VBox filterContent = createFilterSection();
        filterContent.setStyle("-fx-border-color: darkred;");

        VBox.setVgrow(lineListView, Priority.ALWAYS);

        combinedContainer.getChildren().addAll(filterContent, new Separator(), lineListView);
        VBox.setVgrow(combinedContainer, Priority.ALWAYS);

        rightColumn.getChildren().addAll(departuresLabel, combinedContainer);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);
        setupBoxStyle(rightColumn, 350);

        stationListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Station selectedStation = stationListView.getSelectionModel().getSelectedItem();
                this.controller.loadLinesForSelectedStation(
                        selectedStation,
                        tramCheckbox.isSelected(),
                        busCheckbox.isSelected(),
                        subwayCheckbox.isSelected()
                );
            }
        });

        return rightColumn;
    }

    /**
     * Creates the center section containing:
     * <ul>
     *     <li>Station list</li>
     *     <li>Favorites list</li>
     *     <li>Filters for transportation types</li>
     *     <li>Departure times list</li>
     * </ul>
     *
     * @return HBox containing the center UI section.
     */
    private HBox createCenterSection() {
        HBox centerBox = new HBox(15);
        centerBox.setPadding(new Insets(10, 0, 10, 0));

        VBox leftColumn = centerLeftColumn();
        VBox rightColumn = centerRightColumn();
        centerBox.getChildren().addAll(leftColumn, rightColumn);

        stationListView.setCellFactory(lv -> new ListCell<Station>() {
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
                            favoriteStations.add(station);
                        }
                    });

                    HBox.setHgrow(name, Priority.ALWAYS);
                    cell.getChildren().addAll(name, starButton);
                    setGraphic(cell);
                }
            }
        });

        favoriteListView.setCellFactory(lv -> new ListCell<Station>() {
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
                    Button removeButton = new Button("★");

                    removeButton.setOnAction(e -> favoriteStations.remove(station));

                    HBox.setHgrow(name, Priority.ALWAYS);
                    cell.getChildren().addAll(name, removeButton);
                    setGraphic(cell);
                }
            }
        });

        favoriteListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Station selectedFav = favoriteListView.getSelectionModel().getSelectedItem();
                if (selectedFav != null) {
                    this.controller.loadLinesForSelectedStation(
                        selectedFav,
                        tramCheckbox.isSelected(),
                        busCheckbox.isSelected(),
                        subwayCheckbox.isSelected());
                }
            }
        });

        return centerBox;
    }

    /**
     * Creates the bottom section containing a light/dark mode toggle button.
     *
     * @param root The main BorderPane root of the scene
     * @return HBox containing the toggle button
     */
    private HBox createBottomSection(BorderPane root) {
        Button toggleButton = new Button("Light/Dark Mode");
        toggleButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(toggleButton, Priority.ALWAYS);

        toggleButton.setOnAction(e -> {
            isDarkMode = this.controller.toggleLightDarkMode(root,isDarkMode);
        });

        toggleButton.setStyle("-fx-background-color: lightgray; -fx-border-color: darkred; -fx-border-width: 2;");

        return new HBox(10, toggleButton);
    }

    /**
     * Applies preferred width and height settings to a VBox container.
     */
    private void setupBoxStyle(VBox box, double prefWidth) {
        box.setPrefWidth(prefWidth);
        HBox.setHgrow(box, Priority.ALWAYS);
        VBox.setVgrow(box, Priority.ALWAYS);
    }

    /**
     * Creates the filter section UI for transportation types.
     */
    private VBox createFilterSection() {
        VBox filterContainer = new VBox(5, new Label("Verkehrsmittel filtern:"));
        filterContainer.setPadding(new Insets(5));
        filterContainer.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");

        tramCheckbox.setSelected(true);
        busCheckbox.setSelected(true);
        subwayCheckbox.setSelected(true);

        tramCheckbox.setOnAction(e -> applyFilter());
        busCheckbox.setOnAction(e -> applyFilter());
        subwayCheckbox.setOnAction(e -> applyFilter());

        HBox checks = new HBox(10, tramCheckbox, busCheckbox, subwayCheckbox);
        filterContainer.getChildren().add(checks);
        return filterContainer;
    }

    /**
     *help function to improve readability of code
     */
    private void applyFilter() {
        this.controller.applyFilter(
            tramCheckbox.isSelected(),
            busCheckbox.isSelected(),
            subwayCheckbox.isSelected()
        );
    }

}