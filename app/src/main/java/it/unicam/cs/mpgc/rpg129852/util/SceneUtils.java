package it.unicam.cs.mpgc.rpg129852.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneUtils {

    // private constructor to prevent instantiation
    private SceneUtils() {
    }

    public static void switchScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(SceneUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
