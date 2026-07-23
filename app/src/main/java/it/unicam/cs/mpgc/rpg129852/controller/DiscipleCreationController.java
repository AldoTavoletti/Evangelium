package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.asset.*;
import it.unicam.cs.mpgc.rpg129852.service.GameStarter;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigator;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigatorImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

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
    private TextField nameField;

    @FXML
    private TextField pathField;

    @FXML
    private Button nextGifButton;

    @FXML
    private Button previousGifButton;

    @FXML
    private Button returnToMenuButton;

    @FXML
    private Button saveAsButton;

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
        nameField.textProperty().addListener((observable, oldContent, newContent) -> {
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
    void onSaveAsAction(ActionEvent event) {

        FileChooser jsonChooser = createJsonChooser();
        File selectedFile = getFileFrom(jsonChooser);

        if (selectedFile != null) {
            String rawPath = selectedFile.getAbsolutePath();
            String finalPath = formatJsonPath(rawPath);

            pathField.setText(finalPath);
        }

    }
    private FileChooser createJsonChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Scegli percorso e nome file");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser;
    }

    private File getFileFrom(FileChooser fileChooser) {
        Stage stage = (Stage) pathField.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        return selectedFile;
    }

    private String formatJsonPath(String rawPath) {
        boolean hasExtension = rawPath.toLowerCase().endsWith(".json");
        return hasExtension ? rawPath : rawPath + ".json";
    }

    @FXML
    void onStartGameAction(ActionEvent event) {
        String name = nameField.getText();
        String job = jobSelector.getValue();
        String color = discipleAssetNavigator.getCurrentElement().id();
        String savePath = pathField.getText();

        gameStarter.startNewGame(name, job, color, savePath);

    }

}
