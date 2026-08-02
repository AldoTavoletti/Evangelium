package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;
import it.unicam.cs.mpgc.rpg129852.service.LevelCatalog;
import it.unicam.cs.mpgc.rpg129852.service.LevelStarter;
import it.unicam.cs.mpgc.rpg129852.ui.LevelCardNode;
import it.unicam.cs.mpgc.rpg129852.ui.LevelDetailsNode;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PlayerMenuController {

    private final ViewRouter sceneManager;
    private final GameSessionManager sessionManager;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;
    private final LevelCatalog levelCatalog;
    private final LevelStarter levelStarter;

    private LevelMetadata selectedLevel;
    private LevelCardNode selectedCardNode;
    private int totalVirtues;

    @FXML private DiscipleHeaderController discipleHeaderController;
    @FXML private ToolBar categoryToolBar;
    @FXML private TilePane levelsContainer;
    @FXML private VBox detailsContainer;
    @FXML private Button playButton;

    public PlayerMenuController(GameSessionManager sessionManager,
                                LevelCatalog levelCatalog,
                                LevelStarter levelStarter,
                                ResourceRegistry<DiscipleAsset> discipleAssetRegistry,
                                ViewRouter sceneManager) {
        this.sessionManager = sessionManager;
        this.levelCatalog = levelCatalog;
        this.levelStarter  = levelStarter;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        validateSession();

        DiscipleData discipleData = sessionManager.getCurrentDiscipleData();
        this.totalVirtues = discipleData.getTotalVirtues();

        setupHeader(discipleData);
        initCategoryButtons();
        loadCardsForCategory(LevelCategory.SPIRITUAL_GUIDANCE);
    }

    @FXML
    void onPlayLevelAction(ActionEvent event) {
        if (selectedLevel != null) {
            levelStarter.start(selectedLevel);
            sceneManager.switchScene(ViewRoute.GAMEPLAY);

        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private void validateSession() {
        if (!sessionManager.hasActiveGame()) {
            throw new IllegalStateException("There must be an active game context while in the player menu.");
        }
    }

    private void setupHeader(DiscipleData data) {
        DiscipleAsset asset = discipleAssetRegistry.getResource(data.getColor());
        Image gif = ImageUtils.loadImage(asset.gifPath());

        discipleHeaderController.initData(data, gif);
    }

    private void initCategoryButtons() {
        List<Button> buttons = Arrays.stream(LevelCategory.values())
                .map(this::createCategoryButton)
                .toList();

        categoryToolBar.getItems().setAll(buttons);
    }

    private Button createCategoryButton(LevelCategory category) {
        Button btn = new Button(category.getDisplayName());
        btn.getStyleClass().add("button");
        btn.setOnAction(e -> loadCardsForCategory(category));
        return btn;
    }

    private void loadCardsForCategory(LevelCategory category) {
        resetUI();
        boolean isUnlocked = category.isUnlocked(totalVirtues);
        List<LevelMetadata> categoryLevels = levelCatalog.getLevelsByCategory(category);
        populateGrid(categoryLevels, isUnlocked);
    }

    private void populateGrid(List<LevelMetadata> levels, boolean isUnlocked) {
        List<LevelCardNode> cards = levels.stream()
                .map(level -> new LevelCardNode(level, isUnlocked, clickedCard -> handleCardClick(level, clickedCard)))
                .toList();

        levelsContainer.getChildren().setAll(cards);
    }

    private void handleCardClick(LevelMetadata level, LevelCardNode card) {
        if (level.equals(this.selectedLevel)) {
            unselectCurrentLevel();
        } else {
            selectLevel(level, card);
        }
    }

    private void selectLevel(LevelMetadata level, LevelCardNode clickedCard) {
        if (selectedCardNode != null) {
            selectedCardNode.setSelected(false);
        }

        selectedCardNode = clickedCard;
        selectedCardNode.setSelected(true);
        selectedLevel = level;

        playButton.setDisable(false);
        updateDetailsContainer(level);
    }

    private void unselectCurrentLevel() {
        if (selectedCardNode != null) {
            selectedCardNode.setSelected(false);
        }
        clearSelectionState();
    }

    private void resetUI() {
        levelsContainer.getChildren().clear();
        clearSelectionState();
    }

    private void clearSelectionState() {
        selectedLevel = null;
        selectedCardNode = null;
        playButton.setDisable(true);
        updateDetailsContainer(null);
    }

    private void updateDetailsContainer(LevelMetadata level) {
        detailsContainer.getChildren().clear();
        if (level != null) {
            GameState currentGameState = sessionManager.getCurrentGame().getGameState();
            Optional<Virtues> bestAttempt = currentGameState.getScoreForLevel(level.id());
            detailsContainer.getChildren().add(new LevelDetailsNode(level, bestAttempt));
        }
    }
}