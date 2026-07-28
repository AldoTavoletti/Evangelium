package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.asset.AssetRegistry;
import it.unicam.cs.mpgc.rpg129852.asset.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.nio.file.Path;

public class PlayerMenuController {

    private final GameSessionManager sessionManager;

    private final AssetRegistry<DiscipleAsset> discipleAssetRegistry;

    private final ViewRouter sceneManager;

    @FXML
    private VBox detailsContainer;

    @FXML
    private GridPane levelsContainer;

    @FXML
    private Label discipleNameLabel;

    @FXML
    private ImageView discipleImageView;

    @FXML
    private Label faithLabel;

    @FXML
    private Label hopeLabel;

    @FXML
    private Label loveLabel;

    @FXML
    private Button playButton;

    public PlayerMenuController(GameSessionManager sessionManager, AssetRegistry<DiscipleAsset> discipleAssetRegistry, ViewRouter sceneManager){
        this.sessionManager = sessionManager;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {

        if (!sessionManager.hasActiveGame())
            throw new IllegalStateException("No game is active while in the player menu");

        Game game = sessionManager.getCurrentGame();
        GameState gameState = game.getGameState();
        DiscipleData discipleData = gameState.getDiscipleData();

        initDiscipleInfo(discipleData);

        //todo: initialize level panes

    }

    @FXML
    void onPlayLevelAction(ActionEvent event) {

    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private void initDiscipleInfo(DiscipleData discipleData){

        discipleNameLabel.setText(discipleData.getName() + " | " + discipleData.getJob());
        faithLabel.setText("Fede: " + discipleData.getFaith());
        hopeLabel.setText("Speranza: " + discipleData.getHope());
        loveLabel.setText("Carità: " + discipleData.getLove());

        DiscipleAsset discipleAsset = discipleAssetRegistry.getAsset(discipleData.getColor());
        Image discipleGif = loadImage(discipleAsset.gifPath());
        discipleImageView.setImage(discipleGif);

    }

    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
