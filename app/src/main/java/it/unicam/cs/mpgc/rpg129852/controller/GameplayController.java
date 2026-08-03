package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.*;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;
import it.unicam.cs.mpgc.rpg129852.service.GameplayService;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameplayController {

    private final GameplayService gameplayService;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;
    private final ResourceRegistry<ScriptureResource> scriptureResourceRegistry;
    private final ViewRouter sceneManager;
    private final GameSessionManager gameSessionManager;

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

    @FXML
    private Label healFeedbackLabel;

    @FXML
    private Label currentPhaseLabel;

    private Popup hoverPopup;
    private Label popupText;
    private LevelPhase currentPhase;
    private SequentialTransition healAnimation;

    public GameplayController(GameplayService gameplayService,
                              GameSessionManager gameSessionManager,
                              ResourceRegistry<DiscipleAsset> discipleAssetRegistry,
                              ResourceRegistry<ScriptureResource> scriptureResourceRegistry,
                              ViewRouter sceneManager) {
        this.gameplayService = gameplayService;
        this.gameSessionManager = gameSessionManager;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.scriptureResourceRegistry = scriptureResourceRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    void initialize() {
        initDiscipleImage();
        initNpcImage();
        initHoverPopup();

        if (healFeedbackLabel != null) {
            healFeedbackLabel.setOpacity(0.0);
            healFeedbackLabel.setVisible(false);
        }

        problemProgressBar.setProgress(gameplayService.getSessionManager().getMaxProblemValue());
        goToNextPhase();
    }

    private void initDiscipleImage() {
        DiscipleData currentDiscipleData = gameSessionManager.getCurrentDiscipleData();
        String colorId = currentDiscipleData.getColor();
        DiscipleAsset discipleImage = discipleAssetRegistry.getResource(colorId);

        Image discipleGif = ImageUtils.loadImage(discipleImage.gifPath());
        discipleImageView.setImage(discipleGif);
    }

    private void initNpcImage() {
        String npcImagePath = gameplayService.getSessionManager().getNpcImagePath();

        Image npcImage = ImageUtils.loadImage(npcImagePath);
        npcImageView.setImage(npcImage);
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

    private void goToNextPhase() {
        this.currentPhase = gameplayService.getEngine().getNextPhase();
        initDialogueTextArea(currentPhase.npcDialogue());
        initOptionButtons(currentPhase.responses());
        initPhaseLabel();
    }

    private void initPhaseLabel() {
        currentPhaseLabel.setText("Turno: " + gameplayService.getEngine().getCurrentPhaseNumber() + "/" + gameplayService.getEngine().getTotalNumberOfPhases());
    }

    private void initDialogueTextArea(String npcDialogue) {
        npcDialogueTextArea.setText(npcDialogue);
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

    private List<DiscipleResponse> getShuffledResponses(DiscipleResponse[] responses) {
        List<DiscipleResponse> shuffledResponses = new ArrayList<>(List.of(responses));
        Collections.shuffle(shuffledResponses);
        return shuffledResponses;
    }

    @FXML
    void onOptionButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        DiscipleResponse discipleResponse = (DiscipleResponse) button.getUserData();
        double healValue = discipleResponse.healValue();

        updateProgressBar(healValue);

        if (problemProgressBar.getProgress() <= 0.0) {
            setOptionsDisable(true);
            gameplayService.completeLevel(problemProgressBar.getProgress());
            playFeedbackAnimation("Hai vinto!", () -> sceneManager.switchScene(ViewRoute.PLAYER_MENU));

        } else if (gameplayService.getEngine().hasNextPhase()) {
            goToNextPhase();
            int displayValue = (int) (healValue * 100);
            playFeedbackAnimation("-" + displayValue, () -> {});

        } else {
            setOptionsDisable(true);
            gameplayService.completeLevel(problemProgressBar.getProgress());
            playFeedbackAnimation("Hai perso!", () -> sceneManager.switchScene(ViewRoute.PLAYER_MENU));
        }
    }

    private void updateProgressBar(double healValue) {
        Double currentProgress = problemProgressBar.getProgress();
        problemProgressBar.setProgress(currentProgress - healValue);
    }

    private void setOptionsDisable(boolean disable) {
        firstOptionButton.setDisable(disable);
        secondOptionButton.setDisable(disable);
        thirdOptionButton.setDisable(disable);
    }

    private void playFeedbackAnimation(String message, Runnable onFinished) {
        if (healFeedbackLabel == null) {
            if (onFinished != null) onFinished.run();
            return;
        }

        if (healAnimation != null && healAnimation.getStatus() == SequentialTransition.Status.RUNNING) {
            healAnimation.stop();
        }

        healFeedbackLabel.setText(message);
        healFeedbackLabel.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), healFeedbackLabel);
        fadeIn.setFromValue(healFeedbackLabel.getOpacity());
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), healFeedbackLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        healAnimation = new SequentialTransition(fadeIn, pause, fadeOut);
        healAnimation.setOnFinished(e -> {
            healFeedbackLabel.setVisible(false);
            if (onFinished != null) {
                onFinished.run();
            }
        });
        healAnimation.play();
    }

    @FXML
    void onOptionButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        DiscipleResponse discipleResponse = (DiscipleResponse) button.getUserData();
        String scriptureID = discipleResponse.scriptureId();
        String content = scriptureResourceRegistry.getResource(scriptureID).text();

        showPopup(button, content);
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

    @FXML
    void onOptionButtonExit(MouseEvent event) {
        hoverPopup.hide();
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }
}