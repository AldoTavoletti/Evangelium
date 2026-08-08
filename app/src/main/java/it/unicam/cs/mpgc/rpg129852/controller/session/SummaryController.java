package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.StatsService;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SummaryController {

    private final DiscipleProfileService discipleProfile;
    private final ViewRouter sceneManager;
    private final StatsService statsService;

    @FXML
    private ImageView discipleImageView;

    @FXML
    private Button returnToMenuButton;

    @FXML
    private VBox statsContainer;

    public SummaryController(StatsService statsService, DiscipleProfileService discipleProfile, ViewRouter sceneManager) {
        System.out.println("constructor");
        this.discipleProfile = discipleProfile;
        this.statsService = statsService;
        this.sceneManager = sceneManager;
    }

    @FXML
    void initialize() {
        System.out.println("init");
        initDiscipleImage();
        populateStatsContainer();
    }

    private void populateStatsContainer() {

        Label numPerfectLevelsLabel = new Label("Numero di livelli completati in modo perfetto: " + statsService.getNumberOfPerfectLevels());
        Label numAttemptsLabel = new Label("Numero totale di tentativi eseguiti: " + statsService.getNumberOfPerfectLevels());

        statsContainer.getChildren().addAll(numPerfectLevelsLabel, numAttemptsLabel);

    }

    private void initDiscipleImage() {
        String gifPath = discipleProfile.getAvatarGifPath();
        Image gif = ImageUtils.loadImage(gifPath);
        discipleImageView.setImage(gif);
    }

    @FXML
    void onReturnToMenuClicked(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }

}

