module org.example.project_wobimich {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.project_wobimich to javafx.fxml;
    exports org.example.project_wobimich;
}