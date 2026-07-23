package it.unicam.cs.mpgc.rpg129852.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class SceneManager implements ViewRouter {
    private final Stage stage;
    private final Callback<Class<?>, Object> controllerFactory;

    public SceneManager(Stage stage,  Callback<Class<?>, Object> controllerFactory) {
        this.stage = stage;
        this.controllerFactory = controllerFactory;
    }

    public void switchScene(ViewRoute route) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(route.getPath()));
            loader.setControllerFactory(controllerFactory);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
