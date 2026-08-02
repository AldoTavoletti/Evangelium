package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.dto.DiscipleResponse;
import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.ScriptureResource;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;
import it.unicam.cs.mpgc.rpg129852.service.LevelEngine;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameplayController {

    private final LevelEngine levelEngine;
    private final GameSessionManager sessionManager;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;

    @FXML
    private ImageView discipleImageView;

    @FXML
    private TextArea npcDialogueTextArea;

    @FXML
    private ImageView npcImageView;

    @FXML
    private Button firstOptionButton;

    @FXML
    private Button secondOptionButton;

    @FXML
    private Button thirdOptionButton;

    @FXML
    private ProgressBar problemProgressBar;

    private Popup hoverPopup;
    private Label popupText;
    private LevelPhase currentPhase;
    private ResourceRegistry<ScriptureResource> scriptureResourceRegistry;
    private ViewRouter sceneManager;

    public GameplayController(LevelEngine levelEngine,
                              GameSessionManager sessionManager,
                              ResourceRegistry<DiscipleAsset> discipleAssetRegistry,
                              ResourceRegistry<ScriptureResource> scriptureResourceRegistry,
                              ViewRouter sceneManager) {
        this.levelEngine = levelEngine;
        this.sessionManager = sessionManager;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.scriptureResourceRegistry = scriptureResourceRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    void initialize() {
        initDiscipleImage();
        initNpcImage();

        problemProgressBar.setProgress(levelEngine.getMaxProblemValue());

        goToNextPhase();
        initHoverPopup();
    }

    @FXML
    void onOptionButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();

        DiscipleResponse discipleResponse = (DiscipleResponse) button.getUserData();
        double healValue = discipleResponse.healValue();

        updateProgressBar(healValue);

        if (problemProgressBar.getProgress() <= 0.0)
            sceneManager.switchScene(ViewRoute.PLAYER_MENU);


        if (levelEngine.hasNextPhase())
            goToNextPhase();
        else
            sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }

    @FXML
    void onOptionButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();

        DiscipleResponse discipleResponse = (DiscipleResponse) button.getUserData();
        String scriptureID = discipleResponse.scriptureId();
        String content = scriptureResourceRegistry.getResource(scriptureID).text();

        showPopup(button, content);
    }

    @FXML
    void onOptionButtonExit(MouseEvent event) {
        hoverPopup.hide();
    }

    private void initOptionButtons(DiscipleResponse[] responses) {

        List<DiscipleResponse> shuffledResponses = getShuffledResponses(responses);

        List<Button> optionButtons = List.of(firstOptionButton, secondOptionButton, thirdOptionButton);

        for (int i = 0; i < 3; i++) {
            DiscipleResponse response = shuffledResponses.get(i);
            Button btn = optionButtons.get(i);

            btn.setText(response.displayReference());
            btn.setUserData(response);
        }

    }

    private void initDiscipleImage() {
        DiscipleData currentDiscipleData = sessionManager.getCurrentDiscipleData();
        String colorId = currentDiscipleData.getColor();
        DiscipleAsset discipleImage = discipleAssetRegistry.getResource(colorId);

        Image discipleGif = ImageUtils.loadImage(discipleImage.gifPath());
        discipleImageView.setImage(discipleGif);
    }

    private void initNpcImage() {
        String npcImagePath = this.levelEngine.getNpcImagePath();

        Image npcImage = ImageUtils.loadImage(npcImagePath);
        npcImageView.setImage(npcImage);
    }

    private void initDialogueTextArea(String npcDialogue) {
        npcDialogueTextArea.setText(npcDialogue);
    }

    private void initHoverPopup() {
        hoverPopup = new Popup();

        popupText = new Label();
        popupText.setWrapText(true);
        popupText.setPrefWidth(350);
        popupText.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-line-spacing: 5px;");

        VBox pane = new VBox(popupText);
        pane.setStyle(
                "-fx-background-color: rgba(30, 30, 30, 0.95);" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #555555;" +
                        "-fx-border-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);"
        );

        hoverPopup.getContent().add(pane);

        hoverPopup.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
    }

    private void showPopup(Button button, String content) {
        popupText.setText(content);

        Bounds boundsInScreen = button.localToScreen(button.getBoundsInLocal());

        hoverPopup.show(
                button,
                boundsInScreen.getMinX(),
                boundsInScreen.getMinY() - 5
        );
    }

    private void goToNextPhase() {
        this.currentPhase = levelEngine.getNextPhase();
        initDialogueTextArea(currentPhase.npcDialogue());
        initOptionButtons(currentPhase.responses());
    }

    private List<DiscipleResponse> getShuffledResponses(DiscipleResponse[] responses) {
        List<DiscipleResponse> shuffledResponses = new ArrayList<>(List.of(responses));
        Collections.shuffle(shuffledResponses);

        return shuffledResponses;
    }

    private void updateProgressBar(double healValue) {
        Double currentProgress = problemProgressBar.getProgress();
        problemProgressBar.setProgress(currentProgress - healValue);
    }

}
