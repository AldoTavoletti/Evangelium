package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.asset.*;
import it.unicam.cs.mpgc.rpg129852.service.GameStarter;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigator;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigatorImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Optional;

public class DiscipleCreationController {

    private GameStarter gameStarter;

    private final CircularListNavigator<DiscipleAsset> discipleAssetNavigator;

    private final List<String> jobs;

    private final ViewRouter sceneManager;

    @FXML
    private ImageView currentGifImage;

    @FXML
    private ChoiceBox<String> jobSelector;

    @FXML
    private TextField discipleNameField;

    @FXML
    private TextField saveNameField;

    @FXML
    private Button nextGifButton;

    @FXML
    private Button previousGifButton;

    @FXML
    private Button startGameButton;

    public DiscipleCreationController(GameStarter gameStarter,
                                      AssetRegistry<DiscipleAsset> discipleAssetRegistry,
                                      List<String> jobs,
                                      ViewRouter sceneManager) {
        this.gameStarter = gameStarter;
        this.discipleAssetNavigator = new CircularListNavigatorImpl<>(discipleAssetRegistry.getAllAssets());
        this.jobs = jobs;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        updateGifImage();
        initJobSelector();
        initNameField();
    }

    private void updateGifImage() {
        DiscipleAsset discipleAsset = discipleAssetNavigator.getCurrentElement();
        String currentGifPath = discipleAsset.gifPath();

        currentGifImage.setImage(new Image(currentGifPath));
    }

    private void initJobSelector() {
        jobSelector.getItems().addAll(jobs);
        jobSelector.getSelectionModel().selectFirst();
    }

    private void initNameField() {
        discipleNameField.textProperty().addListener((observable, oldContent, newContent) -> {
            startGameButton.setDisable(newContent.isEmpty());
        });
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    @FXML
    void onNextGifAction(ActionEvent event) {
        discipleAssetNavigator.moveToNext();
        updateGifImage();
    }

    @FXML
    void onPreviousGifAction(ActionEvent event) {
        discipleAssetNavigator.moveToPrevious();
        updateGifImage();
    }

    @FXML
    void onStartGameAction(ActionEvent event) {

        attemptStartGame(false);

    }

    private void attemptStartGame(boolean forceOverwrite) {
        String name = discipleNameField.getText();
        String job = jobSelector.getValue();
        String saveName = saveNameField.getText();
        String color = discipleAssetNavigator.getCurrentElement().id();

        try {
            gameStarter.startNewGame(name, job, color, saveName, forceOverwrite);
            // todo: should switch scene

        } catch (IllegalStateException e) {
            if (askOverwriteConfirmation()) {
                attemptStartGame(true);
            }

        } catch (IllegalArgumentException e) {
            showErrorAlert("Nome non adatto", e.getMessage());
        }
    }

    private void showErrorAlert(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore Creazione Partita");
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private boolean askOverwriteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salvataggio Esistente");
        alert.setHeaderText("Esiste già un salvataggio con questo nome.");
        alert.setContentText("Vuoi sovrascriverlo e perdere i vecchi progressi?");

        // Aspettiamo la risposta dell'utente
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
