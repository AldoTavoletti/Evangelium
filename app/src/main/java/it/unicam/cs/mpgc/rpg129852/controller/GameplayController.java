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
import it.unicam.cs.mpgc.rpg129852.service.DiscipleProfileService;
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

    private static final String MSG_VICTORY = "Hai vinto!";
    private static final String MSG_DEFEAT = "Hai perso!";

    private final GameplayService gameplayService;
    private final DiscipleProfileService discipleProfile;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;
    private final ResourceRegistry<ScriptureResource> scriptureResourceRegistry;
    private final ViewRouter sceneManager;

    @FXML
    private Label problemLabel;
    @FXML
    private ImageView discipleImageView;
    @FXML
    private ImageView npcImageView;
    @FXML
    private TextArea npcDialogueTextArea;
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
    private Label popupSource;
    private SequentialTransition healAnimation;

    public GameplayController(GameplayService gameplayService,
                              DiscipleProfileService discipleProfile,
                              ResourceRegistry<DiscipleAsset> discipleAssetRegistry,
                              ResourceRegistry<ScriptureResource> scriptureResourceRegistry,
                              ViewRouter sceneManager) {
        this.gameplayService = gameplayService;
        this.discipleProfile = discipleProfile;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.scriptureResourceRegistry = scriptureResourceRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    void initialize() {
        setupUIComponents();
        loadNextPhase();
    }

    private void setupUIComponents() {
        initDiscipleImage();
        initNpcImage();
        initHoverPopup();
        resetHealFeedbackLabel();
        problemProgressBar.setProgress(gameplayService.getMaxProblemValue());
    }

    private void resetHealFeedbackLabel() {
        if (healFeedbackLabel != null) {
            healFeedbackLabel.setOpacity(0.0);
            healFeedbackLabel.setVisible(false);
        }
    }

    private void initDiscipleImage() {
        DiscipleData data = discipleProfile.getCurrentData();
        String gifPath = discipleProfile.getAvatarGifPath();
        Image gif = ImageUtils.loadImage(gifPath);

        discipleImageView.setImage(gif);
    }

    private void initNpcImage() {
        npcImageView.setImage(ImageUtils.loadImage(gameplayService.getNpcImagePath()));
    }

    private void initHoverPopup() {
        hoverPopup = new Popup();
        hoverPopup.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);

        popupSource = new Label();
        popupSource.setWrapText(true);
        popupSource.setPrefWidth(350);
        popupSource.getStyleClass().add("popup-source");

        popupText = new Label();
        popupText.setWrapText(true);
        popupText.setPrefWidth(350);
        popupText.getStyleClass().add("popup-text");

        VBox pane = new VBox(popupSource, popupText);
        pane.setSpacing(10.0);
        pane.getStyleClass().add("popup-pane");

        String cssPath = getClass().getResource("/css/Gameplay.css").toExternalForm();
        pane.getStylesheets().add(cssPath);

        hoverPopup.getContent().add(pane);
    }

    private void loadNextPhase() {
        LevelPhase currentPhase = gameplayService.getNextPhase();
        npcDialogueTextArea.setText(currentPhase.npcDialogue());
        updateOptionButtons(currentPhase.responses());
        updatePhaseLabel();
        updateProblemLabel();
    }

    private void updateProblemLabel() {
        problemLabel.setText(gameplayService.getProblemType().getDisplayValue() + ": " + problemProgressBar.getProgress()*100);
    }

    private void updatePhaseLabel() {
        String phaseText = String.format("Turno: %d/%d",
                gameplayService.getCurrentPhaseNumber(),
                gameplayService.getTotalNumberOfPhases());
        currentPhaseLabel.setText(phaseText);
    }

    private void updateOptionButtons(DiscipleResponse[] responses) {
        List<DiscipleResponse> shuffledResponses = new ArrayList<>(List.of(responses));
        Collections.shuffle(shuffledResponses);

        List<Button> buttons = List.of(firstOptionButton, secondOptionButton, thirdOptionButton);
        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);
            DiscipleResponse response = shuffledResponses.get(i);

            btn.setText(response.displayReference());
            btn.setUserData(response);
        }
    }

    @FXML
    void onOptionButtonAction(ActionEvent event) {
        DiscipleResponse response = extractResponseFromEvent(event);
        gameplayService.saveAnswer(response.answerValue());
        applyHealing(response.answerValue().getHealValue());
        evaluateGameState(response.answerValue().getHealValue());
    }

    private DiscipleResponse extractResponseFromEvent(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        return (DiscipleResponse) clickedButton.getUserData();
    }

    private void applyHealing(double healValue) {
        double currentProgress = problemProgressBar.getProgress();
        double newProgress = currentProgress - healValue;

        newProgress = Math.round(newProgress * 100.0) / 100.0;

        problemProgressBar.setProgress(newProgress);
    }

    private void evaluateGameState(double healValue) {
        if (isLevelWon()) {
            endLevel(MSG_VICTORY);
        } else if (gameplayService.hasNextPhase()) {
            continueLevel(healValue);
        } else {
            endLevel(MSG_DEFEAT);
        }
    }

    private boolean isLevelWon() {
        return problemProgressBar.getProgress() <= 0.0;
    }

    private void endLevel(String finalMessage) {
        setOptionsDisable(true);
        updateProblemLabel();
        gameplayService.completeLevel(problemProgressBar.getProgress());
        playFeedbackAnimation(finalMessage, this::returnToMenu);
    }

    private void continueLevel(double healValue) {
        loadNextPhase();
        int displayValue = (int) (healValue * 100);
        playFeedbackAnimation("-" + displayValue, () -> {
        });
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

        stopRunningAnimation();

        healFeedbackLabel.setText(message);
        healFeedbackLabel.setVisible(true);

        healAnimation = buildAnimationSequence();
        healAnimation.setOnFinished(e -> {
            healFeedbackLabel.setVisible(false);
            if (onFinished != null) onFinished.run();
        });
        healAnimation.play();
    }

    private void stopRunningAnimation() {
        if (healAnimation != null && healAnimation.getStatus() == SequentialTransition.Status.RUNNING) {
            healAnimation.stop();
        }
    }

    private SequentialTransition buildAnimationSequence() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), healFeedbackLabel);
        fadeIn.setFromValue(healFeedbackLabel.getOpacity());
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), healFeedbackLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        return new SequentialTransition(fadeIn, pause, fadeOut);
    }

    @FXML
    void onOptionButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        DiscipleResponse response = (DiscipleResponse) button.getUserData();
        String content = scriptureResourceRegistry.getResource(response.scriptureId()).text();
        String source = response.displayReference();

        showPopup(button, content, source);
    }

    private void showPopup(Button button, String content, String source) {
        popupSource.setText(source);
        popupText.setText(content);
        Bounds bounds = button.localToScreen(button.getBoundsInLocal());
        hoverPopup.show(button, bounds.getMinX(), bounds.getMinY() - 5);
    }

    @FXML
    void onOptionButtonExit(MouseEvent event) {
        hoverPopup.hide();
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        returnToMenu();
    }

    private void returnToMenu() {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }
}