package it.unicam.cs.mpgc.rpg129852.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Implementation of {@link ViewRouter} for JavaFX.
 * Manages the loading of FXML files and the transitions between scenes,
 * injecting dependencies into controllers via the provided factory.
 */
public class SceneManager implements ViewRouter {

    private final Stage stage;
    private final Callback<Class<?>, Object> controllerFactory;

    public SceneManager(Stage stage, Callback<Class<?>, Object> controllerFactory) {
        this.stage = Objects.requireNonNull(stage, "Stage must not be null.");
        this.controllerFactory = Objects.requireNonNull(controllerFactory, "Controller factory must not be null.");
    }

    @Override
    public void switchScene(ViewRoute route) {
        if (route == null) {
            throw new IllegalArgumentException("The specified route cannot be null.");
        }

        URL fxmlLocation = getClass().getResource(route.getPath());

        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML file not found at path: " + route.getPath());
        }

        Parent viewNode = loadView(fxmlLocation);
        displayOnStage(viewNode);
    }

    private Parent loadView(URL fxmlLocation) {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setControllerFactory(controllerFactory);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view for: " + fxmlLocation.getFile(), e);
        }
    }

    private void displayOnStage(Parent viewNode) {
        Scene newScene = new Scene(viewNode);
        stage.setScene(newScene);
        stage.show();
    }
}