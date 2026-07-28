package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.asset.AssetRegistry;
import it.unicam.cs.mpgc.rpg129852.asset.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.GameStarter;
import it.unicam.cs.mpgc.rpg129852.service.NewGameRequest;
import it.unicam.cs.mpgc.rpg129852.ui.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigator;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigatorImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class DiscipleCreationController {

    private static final String ERROR_TITLE = "Errore Creazione Partita";
    private static final String INVALID_NAME_HEADER = "Nome non adatto";
    private static final String OVERWRITE_TITLE = "Salvataggio Esistente";
    private static final String OVERWRITE_HEADER = "Esiste già un salvataggio con questo nome.";
    private static final String OVERWRITE_CONTENT = "Vuoi sovrascriverlo e perdere i vecchi progressi?";

    private final GameStarter gameStarter;
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
        currentGifImage.setImage(new Image(discipleAsset.gifPath()));
    }

    private void initJobSelector() {
        jobSelector.getItems().addAll(jobs);
        jobSelector.getSelectionModel().selectFirst();
    }

    private void initNameField() {

        startGameButton.setDisable(discipleNameField.getText().isEmpty());

        discipleNameField.textProperty().addListener((observable, oldContent, newContent) ->
                startGameButton.setDisable(newContent.isEmpty())
        );
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
        NewGameRequest request = buildRequest();
        executeStart(request);
    }

    private void executeStart(NewGameRequest request) {
        try {
            gameStarter.startNewGame(request);
            sceneManager.switchScene(ViewRoute.PLAYER_MENU);

        } catch (IllegalStateException e) {
            if (AlertHelper.askConfirmation(OVERWRITE_TITLE, OVERWRITE_HEADER, OVERWRITE_CONTENT)) {
                gameStarter.overwriteAndStartNewGame(request);
                sceneManager.switchScene(ViewRoute.PLAYER_MENU);
            }
        } catch (IllegalArgumentException e) {
            AlertHelper.showError(INVALID_NAME_HEADER, e.getMessage());
        }
    }

    private NewGameRequest buildRequest() {
        return new NewGameRequest(
                discipleNameField.getText(),
                jobSelector.getValue(),
                discipleAssetNavigator.getCurrentElement().id(),
                saveNameField.getText()
        );
    }

}