package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.summary.StatsService;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Objects;

/**
 * Controller for the summary screen.
 * Displays the end-game statistics and the player's avatar.
 */
public class SummaryController {

    private static final String PERFECT_LEVELS_TEXT = "Numero di livelli completati in modo perfetto: ";
    private static final String TOTAL_ATTEMPTS_TEXT = "Numero totale di tentativi eseguiti: ";

    private final StatsService statsService;
    private final DiscipleProfileService discipleProfile;
    private final ViewRouter sceneManager;

    @FXML private ImageView discipleImageView;
    @FXML private VBox statsContainer;

    /**
     * Constructs the summary controller with its required dependencies.
     *
     * @param statsService    the service providing the player's session statistics
     * @param discipleProfile the service providing the player's character data and visuals
     * @param sceneManager    the router responsible for switching views
     * @throws NullPointerException if any of the dependencies are null
     */
    public SummaryController(StatsService statsService, DiscipleProfileService discipleProfile, ViewRouter sceneManager) {
        this.statsService = Objects.requireNonNull(statsService, "The stats service must not be null.");
        this.discipleProfile = Objects.requireNonNull(discipleProfile, "The disciple profile service must not be null.");
        this.sceneManager = Objects.requireNonNull(sceneManager, "The scene manager must not be null.");
    }

    @FXML
    public void initialize() {
        initializeDiscipleImage();
        populateStatsContainer();
    }

    @FXML
    void onBackToMenuButtonClicked() {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }

    private void initializeDiscipleImage() {
        String gifPath = discipleProfile.getGifPath();
        Image gif = ImageUtils.loadImage(gifPath);
        discipleImageView.setImage(gif);
    }

    private void populateStatsContainer() {
        TextFlow perfectLevelsStat = createStatRow(
                PERFECT_LEVELS_TEXT,
                statsService.getNumberOfPerfectLevels()
        );

        TextFlow attemptsStat = createStatRow(
                TOTAL_ATTEMPTS_TEXT,
                statsService.getNumberOfAttempts()
        );

        statsContainer.getChildren().setAll(perfectLevelsStat, attemptsStat);
    }

    private TextFlow createStatRow(String description, int value) {
        Text descriptionText = new Text(description);
        descriptionText.getStyleClass().add("stat-label");

        Text valueText = new Text(" " + value);
        valueText.getStyleClass().add("stat-value");

        return new TextFlow(descriptionText, valueText);
    }
}