package it.unicam.cs.mpgc.rpg129852.ui.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * A utility class that offers static methods to show error alerts or confirmation alerts.
 */
public class AlertHelper {

    private AlertHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Shows an error alert
     * @param header    the header shown in the alert
     * @param content   the content shown in the alert
     */
    public static void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Shows a confirmation alert
     * @param title     the title of the alert
     * @param header    the header of the alert
     * @param content   the content of the alert
     * @return          the choice made by the user
     */
    public static boolean askConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
