package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.model.CircularImageNavigator;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleGifLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

import static it.unicam.cs.mpgc.rpg129852.util.SceneUtils.switchScene;

public class DiscipleCreationController {

    @FXML
    private ImageView currentGifImage;

    @FXML
    private ChoiceBox<?> jobSelector;

    @FXML
    private TextField nameField;

    @FXML
    private Button nextGifButton;

    @FXML
    private Button previousGifButton;

    @FXML
    private Button returnToMenuButton;

    @FXML
    private Button startGameButton;

    private CircularImageNavigator discipleGifNavigator;

    @FXML
    public void initialize() {

        String[] colors = {"red", "blue", "green", "yellow"};
        List<Image> gifs = DiscipleGifLoader.initializeGifs(colors);

        discipleGifNavigator = new CircularImageNavigator(gifs);

        currentGifImage.setImage(discipleGifNavigator.getCurrentImage());
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        switchScene("/view/MainMenu.fxml", event);
    }

    @FXML
    void onNextGifAction(ActionEvent event) {
        discipleGifNavigator.moveToNext();
        currentGifImage.setImage(discipleGifNavigator.getCurrentImage());
    }

    @FXML
    void onPreviousGifAction(ActionEvent event) {
        discipleGifNavigator.moveToPrevious();
        currentGifImage.setImage(discipleGifNavigator.getCurrentImage());
    }

    @FXML
    void onStartGameAction(ActionEvent event) {
        System.out.println("Starting game...");
    }


}
