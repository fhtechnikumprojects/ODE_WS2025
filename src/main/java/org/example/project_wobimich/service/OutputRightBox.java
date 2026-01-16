package org.example.project_wobimich.service;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.project_wobimich.model.LineStation;

import java.util.List;

public class OutputRightBox {
    private final VBox targetBox;

    public OutputRightBox(VBox targetBox) {
        this.targetBox = targetBox;
    }


    public void showLines(String stationName, List<LineStation> lines) {
        targetBox.getChildren().clear();

        if (lines == null) {
            showMessage("Keine gültige Linie verfügbar");
            return;
        } else if (lines.isEmpty()) {
            showMessage("Linien nicht verfügbar");
            return;
        }

        Label header = new Label(stationName);
        header.setStyle("-fx-font-weight: bold");
        targetBox.getChildren().add(header);

        for (LineStation line : lines) {

            targetBox.getChildren().add(new Label(formatLine(line)));
        }
    }


    private String formatLine(LineStation line) {
        Integer next = firstNonNegative(line.getCountdownMinutes());
        String nextText = String.valueOf(next);

        return "Linie " + line.getName() + " nach " + line.getDirection() + " in " + nextText + " min";
    }

    private Integer firstNonNegative(List<Integer> mins) {
        if (mins == null) return null;
        for (Integer m : mins) {
            if (m != null && m >= 0) return m;
        }
        return null;
    }

    public void showMessage(String message) {
        targetBox.getChildren().clear();
        targetBox.getChildren().add(new Label(message));
    }
}
