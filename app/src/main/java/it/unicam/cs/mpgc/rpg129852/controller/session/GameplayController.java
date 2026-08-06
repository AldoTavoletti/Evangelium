package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.dto.disciple.DiscipleResponse;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.GameplayService;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.ScriptureCatalog;
import it.unicam.cs.mpgc.rpg129852.ui.common.FeedbackAnimator;
import it.unicam.cs.mpgc.rpg129852.ui.level.ScripturePopup;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameplayController {

    private static final String VICTORY_MESSAGE = "Hai vinto!";
    private static final String DEFEAT_MESSAGE = "Hai perso!";
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final GameplayService gameplayService;
    private final DiscipleProfileService discipleProfile;
    private final ScriptureCatalog scriptureCatalog;
    private final ViewRouter sceneManager;

    @FXML private Label problemSeverityLabel;
    @FXML private ImageView discipleImageView;
    @FXML private ImageView npcImageView;
    @FXML private TextArea dialogueTextArea;
    @FXML private Button firstResponseButton;
    @FXML private Button secondResponseButton;
    @FXML private Button thirdResponseButton;
    @FXML private ProgressBar severityProgressBar;
    @FXML private Label feedbackLabel;
    @FXML private Label phaseCounterLabel;

    private ScripturePopup scripturePopup;
    private FeedbackAnimator feedbackAnimator;
    private List<Button> responseButtons;

    public GameplayController(GameplayService gameplayService,
                              DiscipleProfileService discipleProfile,
                              ScriptureCatalog scriptureCatalog,
                              ViewRouter sceneManager) {
        this.gameplayService = gameplayService;
        this.discipleProfile = discipleProfile;
        this.scriptureCatalog = scriptureCatalog;
        this.sceneManager = sceneManager;
    }

    @FXML
    void initialize() {
        this.responseButtons = List.of(firstResponseButton, secondResponseButton, thirdResponseButton);
        initializeVisualElements();
        loadNextPhase();
    }

    @FXML
    void onResponseButtonHover(MouseEvent event) {
        displayScriptureReference((Node) event.getSource());
    }

    @FXML
    void onResponseButtonExit(MouseEvent event) {
        scripturePopup.hide();
    }

    @FXML
    void onResponseButtonClicked(ActionEvent event) {
        DiscipleResponse selectedResponse = getResponseFromEvent(event);

        gameplayService.submitAnswer(selectedResponse.answerValue());
        refreshProgressBar();
        resolveTurnOutcome(selectedResponse.answerValue().getHealValue());
    }

    @FXML
    void onReturnToMenuClicked(ActionEvent event) {
        returnToPlayerMenu();
    }

    private void initializeVisualElements() {
        loadDiscipleAvatar();
        loadNpcAvatar();

        scripturePopup = new ScripturePopup();
        feedbackAnimator = new FeedbackAnimator(feedbackLabel); // Inizializzazione animatore

        hideImpactFeedback();
        severityProgressBar.setProgress(gameplayService.getCurrentProblemValue());
    }

    private void hideImpactFeedback() {
        if (feedbackLabel != null) {
            feedbackLabel.setOpacity(0.0);
            feedbackLabel.setVisible(false);
        }
    }

    private void loadDiscipleAvatar() {
        String gifPath = discipleProfile.getAvatarGifPath();
        discipleImageView.setImage(ImageUtils.loadImage(gifPath));
    }

    private void loadNpcAvatar() {
        npcImageView.setImage(ImageUtils.loadImage(gameplayService.getNpcImagePath()));
    }

    private void loadNextPhase() {
        LevelPhase currentPhase = gameplayService.getNextPhase();
        dialogueTextArea.setText(currentPhase.npcDialogue());

        bindResponsesToButtons(currentPhase.responses());
        updateTurnCounterText();
        updateProblemSeverityText();
    }

    private void updateProblemSeverityText() {
        String problemName = gameplayService.getProblemType().getDisplayValue();
        double currentHealth = gameplayService.getCurrentProblemValue();
        problemSeverityLabel.setText(problemName + ": " + (currentHealth * PERCENTAGE_MULTIPLIER));
    }

    private void updateTurnCounterText() {
        String phaseText = String.format("Turno: %d/%d",
                gameplayService.getCurrentPhaseNumber(),
                gameplayService.getTotalNumberOfPhases());
        phaseCounterLabel.setText(phaseText);
    }

    private void bindResponsesToButtons(DiscipleResponse[] responses) {
        List<DiscipleResponse> shuffledResponses = new ArrayList<>(List.of(responses));
        Collections.shuffle(shuffledResponses);

        for (int i = 0; i < responseButtons.size(); i++) {
            Button button = responseButtons.get(i);
            DiscipleResponse response = shuffledResponses.get(i);
            button.setText(response.displayReference());
            button.setUserData(response);
        }
    }

    private DiscipleResponse getResponseFromEvent(ActionEvent event) {
        return (DiscipleResponse) ((Button) event.getSource()).getUserData();
    }

    private void refreshProgressBar() {
        severityProgressBar.setProgress(gameplayService.getCurrentProblemValue());
    }

    private void resolveTurnOutcome(double impactValue) {
        if (gameplayService.isLevelWon()) {
            handleLevelCompletion(VICTORY_MESSAGE);
        } else if (gameplayService.hasNextPhase()) {
            transitionToNextTurn(impactValue);
        } else {
            handleLevelCompletion(DEFEAT_MESSAGE);
        }
    }

    private void handleLevelCompletion(String finalMessage) {
        responseButtons.forEach(button -> button.setDisable(true));
        updateProblemSeverityText();

        gameplayService.completeLevel();

        feedbackAnimator.playFeedback(finalMessage, this::returnToPlayerMenu);
    }

    private void transitionToNextTurn(double impactValue) {
        loadNextPhase();
        int displayValue = (int) (impactValue * PERCENTAGE_MULTIPLIER);

        feedbackAnimator.playFeedback("-" + displayValue, null);
    }

    private void displayScriptureReference(Node anchorNode) {
        DiscipleResponse discipleResponse = (DiscipleResponse) anchorNode.getUserData();
        String content = scriptureCatalog.getScriptureText(discipleResponse.scriptureId());
        String title = discipleResponse.displayReference();

        scripturePopup.showAboveNode(anchorNode, content, title);
    }

    private void returnToPlayerMenu() {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }
}