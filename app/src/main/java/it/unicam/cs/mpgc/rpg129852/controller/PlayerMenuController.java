package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.dto.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;
import it.unicam.cs.mpgc.rpg129852.service.PlayerMenuService;
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

    private static final String STYLE_CLASS_BUTTON = "button";
    private static final LevelCategory DEFAULT_CATEGORY = LevelCategory.SPIRITUAL_GUIDANCE;

    private final ViewRouter sceneManager;
    private final PlayerMenuService menuService;
    private final ResourceRegistry<DiscipleAsset> discipleAssetRegistry;

    private LevelMetadata selectedLevel;
    private LevelCardNode selectedCardNode;
    private int totalVirtues;

    @FXML
    private DiscipleHeaderController discipleHeaderController;
    @FXML
    private ToolBar categoryToolBar;
    @FXML
    private TilePane levelsContainer;
    @FXML
    private VBox detailsContainer;
    @FXML
    private Button playButton;

    public PlayerMenuController(PlayerMenuService menuService,
                                ResourceRegistry<DiscipleAsset> discipleAssetRegistry,
                                ViewRouter sceneManager) {
        this.menuService = menuService;
        this.discipleAssetRegistry = discipleAssetRegistry;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        menuService.validateSession();
        setupDiscipleData();
        initCategoryButtons();
        loadCardsForCategory(DEFAULT_CATEGORY);
    }

    private void setupDiscipleData() {
        DiscipleData data = menuService.getCurrentDiscipleData();
        this.totalVirtues = menuService.getTotalVirtues();

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
        btn.getStyleClass().add(STYLE_CLASS_BUTTON);
        btn.setOnAction(e -> loadCardsForCategory(category));
        return btn;
    }

    private void loadCardsForCategory(LevelCategory category) {
        resetUI();

        boolean isUnlocked = category.isUnlocked(totalVirtues);
        List<LevelMetadata> categoryLevels = menuService.getLevelsByCategory(category);

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
            Optional<Virtues> bestAttempt = menuService.getScoreForLevel(level.id());
            detailsContainer.getChildren().add(new LevelDetailsNode(level, bestAttempt));
        }
    }

    @FXML
    void onPlayLevelAction(ActionEvent event) {
        if (selectedLevel != null) {
            menuService.startLevel(selectedLevel);
            sceneManager.switchScene(ViewRoute.GAMEPLAY);
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}