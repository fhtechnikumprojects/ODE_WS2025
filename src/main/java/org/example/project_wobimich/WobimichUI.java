    package org.example.project_wobimich;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.geometry.Insets;
    import javafx.scene.control.*;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.Priority;
    import javafx.scene.layout.VBox;

    public class WobimichUI {

        public BorderPane createScene() {

            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));

            //Top level
            VBox topVBox = new VBox(10);

            //Info-Bar (not editable)
            Label funFactBar = new Label("Fun-Fact WL");
            funFactBar.setPadding(new Insets(10));
            funFactBar.setMaxWidth(Double.MAX_VALUE);
            funFactBar.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                    -fx-background-color: lightgray;
                    """);

            //Search-bar
            HBox searchBar = new HBox(10);
            searchBar.setPadding(new Insets(10));
            searchBar.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                    """);
            TextField searchField = new TextField();
            searchField.setPromptText("Search station");
            HBox.setHgrow(searchField, Priority.ALWAYS);

            //Search-button
            Button searchButton = new Button("Search");
            searchBar.getChildren().addAll(searchField, searchButton);
            topVBox.getChildren().addAll(funFactBar, searchBar);

            root.setTop(topVBox);

            //Center level
            HBox centerBox = new HBox(10);
            centerBox.setSpacing(10);

            //Center left
            VBox centerLeftVBox = new VBox();
            centerLeftVBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
            centerLeftVBox.setPrefWidth(200);
            centerLeftVBox.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                    """);
            HBox.setHgrow(centerLeftVBox, Priority.ALWAYS);
            VBox.setVgrow(centerLeftVBox, Priority.ALWAYS);

            ObservableList<String> station = FXCollections.observableArrayList();
            ListView<String> stationList = new ListView<>();
            stationList.setItems(station);

            //Default stations
            station.setAll(
                "Höchstädtplatz",
                "Franz-Josefs-Bahnhof",
                "Heiligenstadt",
                "Mitte-Landstraße",
                "Erdberg"
            );

            centerLeftVBox.getChildren().add(stationList);

            //Center right
            VBox centerRightVBox = new VBox();
            centerRightVBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
            centerRightVBox.setPrefWidth(200);
            centerRightVBox.setStyle("""
                    -fx-border-color: black;
                    -fx-border-width: 1;
                    """);

            HBox.setHgrow(centerRightVBox, Priority.ALWAYS);
            VBox.setVgrow(centerRightVBox, Priority.ALWAYS);

            //centerRightVBox.getChildren().add();

            centerBox.getChildren().addAll(centerLeftVBox,centerRightVBox);

            root.setCenter(centerBox);

            //Bottom level

            //Toggle Light/Dark Mode button
            HBox bottomBox = new HBox(10);
            bottomBox.setSpacing(10);
            VBox bottomVBox = new VBox();

            Button ToggleLightDarkButton = new Button("Light/Dark Mode");
            bottomBox.getChildren().addAll(ToggleLightDarkButton);
            bottomVBox.getChildren().add(bottomBox);
            ToggleLightDarkButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(ToggleLightDarkButton, Priority.ALWAYS);

            //Dropdown field shows listing of history search input


            root.setBottom(bottomBox);

            return root;
        }


    }
