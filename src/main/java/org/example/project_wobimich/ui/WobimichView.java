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
    // Observable lists used for UI data binding
    private ObservableList<Station> stations = FXCollections.observableArrayList();
    private ObservableList<LineStation> lines = FXCollections.observableArrayList();
    private ObservableList<Station> favoriteStations = FXCollections.observableArrayList();

    // Input field for address search
    private TextField searchTextField = new TextField();

    // Transportation filter checkboxes
    private CheckBox tramCheckbox = new CheckBox("Straßenbahn");
    private CheckBox busCheckbox = new CheckBox("Bus");
    private CheckBox subwayCheckbox = new CheckBox("U-Bahn");

    // Current UI theme state (default light mode)
    private boolean isDarkMode = false;

    // UI Controls: List Views
    private ListView<Station> stationListView = new ListView<>(stations);
    private ListView<LineStation> lineListView = new ListView<>(lines);
    private ListView<Station> favoriteListView = new ListView<>(favoriteStations);

    // UI: Progress bar for auto-refresh countdown
    private ProgressBar refreshProgressBar = new ProgressBar(0);

    // Variable for controller
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
        this.controller = new WobimichController(stations, lines,favoriteStations, refreshProgressBar);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-font-size: 14px; -fx-background-color: #ADD8E6;");

        this.controller.loadDefaultStations();
        this.controller.loadFavorites();

        this.favoriteStations.addListener((ListChangeListener<Station>) c -> this.controller.saveFavorites());

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

        VBox funFactBox = createFunFactBox();
        HBox searchBarButton = createSearchBarButton();

        topVBox.getChildren().addAll(funFactBox, searchBarButton);
        return topVBox;
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
                        if (!favoriteStations.contains(station)) {
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
        Button darkLightButton = createDarkLightButton(root);

        return new HBox(10, darkLightButton);
    }

    /**
     * Applies consistent layout constraints to a VBox used inside the main layout.
     * <p>
     * Sets preferred width and enables horizontal and vertical growing behavior.
     *
     * @param box the VBox to configure
     * @param prefWidth the preferred width of the container
     */
    private void setupBoxStyle(VBox box, double prefWidth) {
        box.setPrefWidth(prefWidth);
        HBox.setHgrow(box, Priority.ALWAYS);
        VBox.setVgrow(box, Priority.ALWAYS);
    }

    /**
     * Creates a UI box displaying a random fun fact.
     * <p>
     * The content is generated once during creation and does not update dynamically.
     *
     * @return VBox containing the fun fact header and text
     */
    private VBox createFunFactBox() {
        VBox funFactBox = new VBox(5);
        funFactBox.setAlignment(Pos.CENTER);
        funFactBox.setPadding(new Insets(10));
        funFactBox.getStyleClass().add("fun-fact-box");
        funFactBox.setStyle("-fx-border-color: darkred; -fx-border-width: 2; -fx-background-color: lightgray;");

        Label funFactHeader = new Label("Hast du gewusst?");
        Label funFactText = new Label(FunFactUtils.getRandomFact());

        funFactBox.getChildren().addAll(funFactHeader, funFactText);
        return funFactBox;
    }

    /**
     * Creates the search bar containing the address input field
     * and the search button.
     * <p>
     * The search button is disabled while the input field is empty.
     *
     * @return HBox with search input and search button
     */
    private HBox createSearchBarButton() {
        HBox searchBarButton = new HBox(10);
        searchBarButton.setPadding(new Insets(10));
        searchBarButton.setStyle("-fx-border-color: darkred;");
        searchTextField.setPromptText("Standort eingeben:");
        HBox.setHgrow(searchTextField, Priority.ALWAYS);

        Button searchButton = new Button("Suche");
        searchButton.disableProperty().bind(searchTextField.textProperty().isEmpty());
        searchButton.setOnAction(e ->
                this.controller.searchAddress(searchTextField.getText())
        );
        searchButton.setStyle("-fx-border-color: darkred; -fx-border-width: 2;");

        searchBarButton.getChildren().addAll(searchTextField, searchButton);
        return searchBarButton;
    }

    /**
     * Creates the left column of the center layout.
     * <p>
     * Contains the station list and the favorites list.
     *
     * @return VBox representing the left center column
     */
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

    /**
     * Creates the right column of the center layout.
     * <p>
     * This section contains:
     * <ul>
     *     <li>The departure list for the selected station</li>
     *     <li>Transportation type filter controls</li>
     * </ul>
     * <p>
     * A double-click on a station in the station list triggers
     * loading of departure data for that station.
     *
     * @return VBox representing the right center column
     */
    private VBox centerRightColumn() {
        VBox rightColumn = new VBox(5);
        Label departuresLabel = new Label("Abfahrten & Filter");

        VBox combinedContainer = new VBox(10);
        combinedContainer.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-border-color: darkred; -fx-padding: 10;");

        refreshProgressBar.setPrefWidth(Double.MAX_VALUE);
        refreshProgressBar.setProgress(0);

        VBox filterContent = createFilterSection();
        filterContent.setStyle("-fx-border-color: darkred;");

        VBox.setVgrow(lineListView, Priority.ALWAYS);

        combinedContainer.getChildren().addAll(refreshProgressBar, filterContent, new Separator(), lineListView);
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
     * Creates the filter section for selecting transportation types.
     * <p>
     * Changing any checkbox immediately reapplies the filter
     * to the currently loaded departure list.
     *
     * @return VBox containing transportation filter controls
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
     * Applies the currently selected transportation filters
     * by delegating the filter state to the controller.
     */
    private void applyFilter() {
        this.controller.applyFilter(
            tramCheckbox.isSelected(),
            busCheckbox.isSelected(),
            subwayCheckbox.isSelected()
        );
    }

    /**
     * Creates the button for toggling between light and dark mode.
     * <p>
     * The actual theme switching logic is handled by the controller.
     *
     * @param root the root pane whose style is updated
     * @return configured toggle button
     */
    private Button createDarkLightButton(BorderPane root) {
        Button darkLightButton = new Button("Light/Dark Mode");
        darkLightButton.setMaxWidth(Double.MAX_VALUE);
        darkLightButton.setStyle("-fx-background-color: lightgray; -fx-border-color: darkred; -fx-border-width: 2;");
        HBox.setHgrow(darkLightButton, Priority.ALWAYS);

        darkLightButton.setOnAction(e -> {
            isDarkMode = this.controller.toggleLightDarkMode(root,isDarkMode);
        });

        return darkLightButton;
    }

}