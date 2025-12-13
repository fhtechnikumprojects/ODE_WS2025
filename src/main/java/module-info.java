module org.example.project_wobimich {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens org.example.project_wobimich to javafx.fxml;
    exports org.example.project_wobimich;
    exports org.example.project_wobimich.dto;
    opens org.example.project_wobimich.dto to javafx.fxml;
    exports org.example.project_wobimich.api;
    opens org.example.project_wobimich.api to javafx.fxml;
    exports org.example.project_wobimich.model;
    opens org.example.project_wobimich.model to com.fasterxml.jackson.databind, javafx.fxml;
}