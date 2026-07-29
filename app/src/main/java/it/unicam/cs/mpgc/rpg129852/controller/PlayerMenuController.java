package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.asset.*;
import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class PlayerMenuController {

    private static final int MAX_GRID_COLUMNS = 3;
    private static final int REQ_THEOLOGICAL_DEBATE = 50;
    private static final int REQ_BIBLE_STUDY = 120;

    private final GameSessionManager sessionManager;

    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;

    private final ResourceRegistry<LevelMetadata> levelMetadataRegistry;

    private final ViewRouter sceneManager;

    private LevelCategory currentCategory = LevelCategory.SPIRITUAL_GUIDANCE;
    private LevelMetadata selectedLevel = null;
    private VBox selectedCardNode = null;

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

    public PlayerMenuController(GameSessionManager sessionManager, ResourceRegistry<LevelMetadata> levelMetadataRegistry, ResourceRegistry<DiscipleAsset> discipleAssetRegistry, ViewRouter sceneManager) {
        this.sessionManager = sessionManager;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.levelMetadataRegistry = levelMetadataRegistry;
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

        initLevelCards();

    }

    private void initLevelCards() {
        loadCardsForCategory(currentCategory);
    }

    private void loadCardsForCategory(LevelCategory category) {
        resetUI();

        int totalVirtues = calculateTotalVirtues();
        boolean isUnlocked = isCategoryUnlocked(category, totalVirtues);
        List<LevelMetadata> categoryLevels = fetchLevelsByCategory(category);

        populateGrid(categoryLevels, isUnlocked);
    }

    private void resetUI() {
        levelsContainer.getChildren().clear();
        detailsContainer.getChildren().clear();

        selectedLevel = null;
        selectedCardNode = null;
        playButton.setDisable(true);
    }

    private int calculateTotalVirtues() {
        DiscipleData data = sessionManager.getCurrentGame().getGameState().getDiscipleData();
        return data.getFaith() + data.getHope() + data.getLove();
    }

    private List<LevelMetadata> fetchLevelsByCategory(LevelCategory category) {
        return levelMetadataRegistry.getAllResources().stream()
                .filter(level -> level.category() == category)
                .toList();
    }

    private void populateGrid(List<LevelMetadata> levels, boolean isUnlocked) {
        int col = 0;
        int row = 0;

        for (LevelMetadata level : levels) {
            levelsContainer.add(buildCardUI(level, isUnlocked), col, row);

            if (++col >= MAX_GRID_COLUMNS) {
                col = 0;
                row++;
            }
        }
    }

    private Node buildCardUI(LevelMetadata level, boolean isUnlocked) {
        VBox card = new VBox();
        card.getStyleClass().add("level-card");

        Label titleLabel = new Label(level.title());
        titleLabel.getStyleClass().add("level-card-title");
        card.getChildren().add(titleLabel);

        if (!isUnlocked) {
            card.setOpacity(0.5);
        } else {
            card.setOnMouseClicked(e -> {
                if (level.equals(this.selectedLevel))
                    selectLevel(null, null);
                else
                    selectLevel(level, card);
            });
            card.getStyleClass().add("level-card-unlocked");
        }

        return card;
    }

    private void selectLevel(LevelMetadata level, VBox clickedCard) {
        if (selectedCardNode != null) {
            selectedCardNode.getStyleClass().remove("level-card-selected");
        }
        selectedCardNode = clickedCard;

        if (selectedCardNode != null)
            selectedCardNode.getStyleClass().add("level-card-selected");

        this.selectedLevel = level;
        this.playButton.setDisable(false);
        updateDetailsContainer(level);
    }

    private void updateDetailsContainer(LevelMetadata level) {
        detailsContainer.getChildren().clear();

        if (level == null)
            return;

        Label title = new Label(level.title());
        title.getStyleClass().add("level-detail-title");

        Label description = new Label(level.description());
        description.setWrapText(true);
        description.getStyleClass().add("level-detail-description");

        Label rewards = new Label(formatRewardsText(level.maxRewards()));
        rewards.getStyleClass().add("level-detail-rewards");

        detailsContainer.getChildren().addAll(title, description, rewards, playButton);
    }

    private String formatRewardsText(VirtueRewards rewards) {
        return String.format("Max Ottenibile: %d Fede | %d Speranza | %d Amore",
                rewards.faith(),
                rewards.hope(),
                rewards.love());
    }

    private boolean isCategoryUnlocked(LevelCategory category, int totalVirtues) {
        return switch (category) {
            case SPIRITUAL_GUIDANCE -> true;
            case THEOLOGICAL_DEBATE -> totalVirtues >= REQ_THEOLOGICAL_DEBATE;
            case BIBLE_STUDY -> totalVirtues >= REQ_BIBLE_STUDY;
        };
    }

    @FXML
    void onPlayLevelAction(ActionEvent event) {
        if (selectedLevel != null) {
            System.out.println("Avvio scenario: " + selectedLevel.levelScenarioPath());
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private void initDiscipleInfo(DiscipleData discipleData) {

        discipleNameLabel.setText(discipleData.getName() + " | " + discipleData.getJob());
        faithLabel.setText("Fede: " + discipleData.getFaith());
        hopeLabel.setText("Speranza: " + discipleData.getHope());
        loveLabel.setText("Carità: " + discipleData.getLove());

        DiscipleAsset discipleAsset = discipleAssetRegistry.getResource(discipleData.getColor());
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
