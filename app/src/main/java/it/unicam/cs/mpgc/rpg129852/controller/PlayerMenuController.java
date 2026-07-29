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
    private static final int REQ_MERCY = 50;
    private static final int REQ_THEOLOGICAL_DEBATE = 120;

    private final GameSessionManager sessionManager;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;
    private final ResourceRegistry<LevelMetadata> levelMetadataRegistry;
    private final ViewRouter sceneManager;

    private int totalVirtues;
    private LevelCategory currentCategory;
    private LevelMetadata selectedLevel;
    private VBox selectedCardNode;

    @FXML private VBox detailsContainer;
    @FXML private GridPane levelsContainer;
    @FXML private Label discipleNameLabel;
    @FXML private Button mercyMenuButton;
    @FXML private Button spiritualGuidanceMenuButton;
    @FXML private Button theologicalDebatesMenuButton;
    @FXML private ImageView discipleImageView;
    @FXML private Label faithLabel;
    @FXML private Label hopeLabel;
    @FXML private Label loveLabel;
    @FXML private Button playButton;

    public PlayerMenuController(GameSessionManager sessionManager,
                                ResourceRegistry<LevelMetadata> levelMetadataRegistry,
                                ResourceRegistry<DiscipleAsset> discipleAssetRegistry,
                                ViewRouter sceneManager) {
        this.sessionManager = sessionManager;
        this.levelMetadataRegistry = levelMetadataRegistry;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        if (!sessionManager.hasActiveGame()) {
            throw new IllegalStateException("No game is active while in the player menu");
        }

        Game game = sessionManager.getCurrentGame();
        GameState gameState = game.getGameState();
        DiscipleData discipleData = gameState.getDiscipleData();

        initDiscipleInfo(discipleData);

        this.totalVirtues = calculateTotalVirtues(discipleData);
        loadCardsForCategory(LevelCategory.SPIRITUAL_GUIDANCE);
    }

    private void loadCardsForCategory(LevelCategory category) {
        resetUI();
        currentCategory = category;
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

    private int calculateTotalVirtues(DiscipleData discipleData) {
        return discipleData.getFaith() + discipleData.getHope() + discipleData.getLove();
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
            card.setOnMouseClicked(e -> handleCardClick(level, card));
            card.getStyleClass().add("level-card-unlocked");
        }

        return card;
    }

    private void handleCardClick(LevelMetadata level, VBox card) {
        if (level.equals(this.selectedLevel)) {
            unselectCurrentLevel();
        } else {
            selectLevel(level, card);
        }
    }

    private void unselectCurrentLevel() {
        if (selectedCardNode != null) {
            selectedCardNode.getStyleClass().remove("level-card-selected");
        }
        selectedCardNode = null;
        selectedLevel = null;
        playButton.setDisable(true);
        detailsContainer.getChildren().clear();
    }

    private void selectLevel(LevelMetadata level, VBox clickedCard) {
        if (clickedCard == null) {
            throw new IllegalArgumentException("The card passed cannot be null.");
        }

        if (selectedCardNode != null) {
            selectedCardNode.getStyleClass().remove("level-card-selected");
        }

        selectedCardNode = clickedCard;
        selectedCardNode.getStyleClass().add("level-card-selected");

        selectedLevel = level;
        playButton.setDisable(false);
        updateDetailsContainer(level);
    }

    private void updateDetailsContainer(LevelMetadata level) {
        if (level == null) {
            throw new IllegalArgumentException("The level passed cannot be null.");
        }

        detailsContainer.getChildren().clear();

        Label title = new Label(level.title());
        title.getStyleClass().add("level-detail-title");

        Label description = new Label(level.description());
        description.setWrapText(true);
        description.getStyleClass().add("level-detail-description");

        Label rewards = new Label(formatRewardsText(level.maxRewards()));
        rewards.getStyleClass().add("level-detail-rewards");

        detailsContainer.getChildren().addAll(title, description, rewards);
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
            case MERCY -> totalVirtues >= REQ_MERCY;
            case THEOLOGICAL_DEBATE -> totalVirtues >= REQ_THEOLOGICAL_DEBATE;
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

    @FXML
    void onSwitchToMercyAction(ActionEvent event) {
        loadCardsForCategory(LevelCategory.MERCY);
    }

    @FXML
    void onSwitchToSpiritualGuidanceAction(ActionEvent event) {
        loadCardsForCategory(LevelCategory.SPIRITUAL_GUIDANCE);
    }

    @FXML
    void onSwitchToTheologicalDebatesAction(ActionEvent event) {
        loadCardsForCategory(LevelCategory.THEOLOGICAL_DEBATE);
    }
}