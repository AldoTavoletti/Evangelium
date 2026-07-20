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

    private static final String[] DISCIPLE_COLORS = {"red", "blue", "green", "yellow"};
    private static final String[] AVAILABLE_JOBS = {
            "Pescatore", "Falegname", "Esattore delle imposte",
            "Fabbricante di tende", "Contadino", "Fabbro", "Medico"
    };

    @FXML
    private ImageView currentGifImage;

    @FXML
    private ChoiceBox<String> jobSelector;

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
        initDiscipleGifs();
        initJobSelector();
        initNameField();
    }

    private void initNameField() {

        nameField.textProperty().addListener((observable, oldContent, newContent) -> {
            if (!newContent.isEmpty())
                startGameButton.setDisable(false);
            else
                startGameButton.setDisable(true);
        });

    }

    private void initDiscipleGifs() {
        List<Image> gifs = DiscipleGifLoader.initializeGifs(DISCIPLE_COLORS);

        discipleGifNavigator = new CircularImageNavigator(gifs);

        currentGifImage.setImage(discipleGifNavigator.getCurrentImage());
    }

    private void initJobSelector() {
        jobSelector.getItems().addAll(AVAILABLE_JOBS);
        jobSelector.getSelectionModel().selectFirst();
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
    void onNameFieldAction(ActionEvent event) {
        CharSequence nameFieldContent = nameField.getCharacters();

        if (!nameFieldContent.isEmpty())
            startGameButton.setDisable(false);
        else
            startGameButton.setDisable(true);
    }

    @FXML
    void onStartGameAction(ActionEvent event) {
            System.out.println("Starting game...");
    }


}
