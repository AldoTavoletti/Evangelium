package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.summary.StatsService;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SummaryController {

    private final StatsService statsService;
    private final DiscipleProfileService discipleProfile;
    private final ViewRouter sceneManager;

    @FXML private ImageView discipleImageView;
    @FXML private Button returnToMenuButton;
    @FXML private VBox statsContainer;

    public SummaryController(StatsService statsService, DiscipleProfileService discipleProfile, ViewRouter sceneManager) {
        this.statsService = statsService;
        this.discipleProfile = discipleProfile;
        this.sceneManager = sceneManager;
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
                "Numero di livelli completati in modo perfetto: ",
                statsService.getNumberOfPerfectLevels()
        );

        TextFlow attemptsStat = createStatRow(
                "Numero totale di tentativi eseguiti: ",
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