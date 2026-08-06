package it.unicam.cs.mpgc.rpg129852.controller.menu;

import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;
import it.unicam.cs.mpgc.rpg129852.service.save.InvalidSaveNameException;
import it.unicam.cs.mpgc.rpg129852.service.save.SaveAlreadyExistsException;
import it.unicam.cs.mpgc.rpg129852.dto.disciple.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarter;
import it.unicam.cs.mpgc.rpg129852.service.game.NewGameRequest;
import it.unicam.cs.mpgc.rpg129852.ui.common.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigator;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiscipleCreationController {

    private static final String INVALID_NAME_HEADER = "Nome non adatto";
    private static final String OVERWRITE_TITLE = "Salvataggio Esistente";
    private static final String OVERWRITE_HEADER = "Esiste già un salvataggio con questo nome.";
    private static final String OVERWRITE_CONTENT = "Vuoi sovrascriverlo e perdere i vecchi progressi?";

    private final GameStarter gameStarter;
    private final CircularListNavigator<DiscipleAsset> discipleAssetNavigator;
    private final ViewRouter sceneManager;

    @FXML
    private ImageView currentGifImage;
    @FXML
    private ChoiceBox<Job> jobSelector;
    @FXML
    private TextField discipleNameField;
    @FXML
    private TextField saveNameField;
    @FXML
    private Button startGameButton;

    public DiscipleCreationController(GameStarter gameStarter,
                                      CircularListNavigator<DiscipleAsset> discipleAssetNavigator,
                                      ViewRouter sceneManager) {
        this.gameStarter = gameStarter;
        this.discipleAssetNavigator = discipleAssetNavigator;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        initJobSelector();
        bindStartButtonToNameField();
        updateGifImage();
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
        } catch (SaveAlreadyExistsException e) {
            handleExistingSave(request);
        } catch (InvalidSaveNameException e) {
            AlertHelper.showError(INVALID_NAME_HEADER, e.getMessage());
        }
    }

    private void handleExistingSave(NewGameRequest request) {
        if (AlertHelper.askConfirmation(OVERWRITE_TITLE, OVERWRITE_HEADER, OVERWRITE_CONTENT)) {
            gameStarter.overwriteAndStartNewGame(request);
            sceneManager.switchScene(ViewRoute.PLAYER_MENU);
        }
    }

    private void updateGifImage() {
        DiscipleAsset discipleAsset = discipleAssetNavigator.getCurrentElement();
        currentGifImage.setImage(ImageUtils.loadImage(discipleAsset.gifPath()));
    }

    private void initJobSelector() {
        List<Job> selectableJobs = Arrays.stream(Job.values())
                .filter(job -> job != Job.NONE)
                .collect(Collectors.toList());

        jobSelector.getItems().addAll(selectableJobs);
        jobSelector.getSelectionModel().selectFirst();
    }

    private void bindStartButtonToNameField() {
        BooleanBinding isNameBlank = Bindings.createBooleanBinding(
                () -> discipleNameField.getText().isBlank(),
                discipleNameField.textProperty()
        );

        startGameButton.disableProperty().bind(isNameBlank);
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