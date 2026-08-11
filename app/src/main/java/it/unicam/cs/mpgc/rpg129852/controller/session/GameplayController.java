package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.dto.level.DiscipleResponse;
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
import java.util.Objects;

/**
 * Controller for the core gameplay screen.
 * Manages the dialogue interaction between the disciple and the NPC,
 * updates the visual state of the level, and evaluates the outcomes of player choices.
 */
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
    @FXML private Label discipleNameLabel;
    @FXML private ImageView npcImageView;
    @FXML private Label npcNameLabel;
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

    /**
     * Constructs the gameplay controller with its required dependencies.
     *
     * @param gameplayService  the service managing the core gameplay loop and state
     * @param discipleProfile  the service providing the player's character data
     * @param scriptureCatalog the catalog to retrieve scripture texts for tooltips
     * @param sceneManager     the router responsible for switching views
     * @throws NullPointerException if any of the dependencies are null
     */
    public GameplayController(GameplayService gameplayService,
                              DiscipleProfileService discipleProfile,
                              ScriptureCatalog scriptureCatalog,
                              ViewRouter sceneManager) {
        this.gameplayService = Objects.requireNonNull(gameplayService, "The gameplay service must not be null.");
        this.discipleProfile = Objects.requireNonNull(discipleProfile, "The disciple profile service must not be null.");
        this.scriptureCatalog = Objects.requireNonNull(scriptureCatalog, "The scripture catalog must not be null.");
        this.sceneManager = Objects.requireNonNull(sceneManager, "The scene manager must not be null.");
    }

    @FXML
    public void initialize() {
        this.responseButtons = List.of(firstResponseButton, secondResponseButton, thirdResponseButton);
        initializeVisualElements();
        loadNextPhase();
    }

    /**
     * Handles the hover event over a response button to display the scripture popup.
     *
     * @param event the mouse event triggered by hovering
     */
    @FXML
    void onResponseButtonHover(MouseEvent event) {
        displayScriptureReference((Node) event.getSource());
    }

    /**
     * Handles the exit event from a response button to hide the scripture popup.
     */
    @FXML
    void onResponseButtonExit() {
        scripturePopup.hide();
    }

    /**
     * Handles the click event on a response button to submit the chosen answer.
     *
     * @param event the action event triggered by clicking
     */
    @FXML
    void onResponseButtonClicked(ActionEvent event) {
        DiscipleResponse selectedResponse = getResponseFromEvent(event);

        gameplayService.submitAnswer(selectedResponse.answerValue());
        refreshProgressBar();
        resolveTurnOutcome(selectedResponse.answerValue().getHealValue());
    }

    /**
     * Handles the click event to abandon the level and return to the player menu.
     */
    @FXML
    void onReturnToMenuClicked() {
        returnToPlayerMenu();
    }

    private void initializeVisualElements() {
        loadDiscipleAvatar();
        loadNpcAvatar();

        scripturePopup = new ScripturePopup();
        feedbackAnimator = new FeedbackAnimator(feedbackLabel);

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
        String gifPath = discipleProfile.getGifPath();
        discipleImageView.setImage(ImageUtils.loadImage(gifPath));
        discipleNameLabel.setText(discipleProfile.getCurrentData().getName());
    }

    private void loadNpcAvatar() {
        npcImageView.setImage(ImageUtils.loadImage(gameplayService.getNpcImagePath()));
        npcNameLabel.setText(gameplayService.getNpcName());
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
        int currentHealth = (int) (gameplayService.getCurrentProblemValue() * PERCENTAGE_MULTIPLIER);
        problemSeverityLabel.setText(problemName + ": " + currentHealth);
    }

    private void updateTurnCounterText() {
        String phaseText = String.format("Turn: %d/%d",
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