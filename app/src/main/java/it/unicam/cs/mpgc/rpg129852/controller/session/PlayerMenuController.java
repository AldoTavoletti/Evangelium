package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.LevelBrowserService;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.LevelStarter;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.SummaryService;
import it.unicam.cs.mpgc.rpg129852.ui.playermenu.CategoryButtonComponent;
import it.unicam.cs.mpgc.rpg129852.ui.level.LevelCardNode;
import it.unicam.cs.mpgc.rpg129852.ui.level.LevelDetailsNode;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PlayerMenuController {

    private final DiscipleProfileService discipleProfile;
    private final LevelBrowserService levelBrowser;
    private final LevelStarter levelStarter;
    private final ViewRouter sceneManager;
    private final SummaryService summaryService;

    private LevelMetadata selectedLevelMetadata;
    private LevelCardNode selectedCardNode;

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

    @FXML
    private Button shopButton;

    public PlayerMenuController(SummaryService summaryService,
                                DiscipleProfileService discipleProfile,
                                LevelStarter levelStarter,
                                LevelBrowserService levelBrowser,
                                ViewRouter sceneManager) {
        this.discipleProfile = discipleProfile;
        this.summaryService = summaryService;
        this.levelStarter = levelStarter;
        this.levelBrowser = levelBrowser;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {

        if (summaryService.shouldSummaryBeShown()) {
            Platform.runLater(() -> sceneManager.switchScene(ViewRoute.SUMMARY));
            return;
        }

        setupDiscipleData();
        initCategoryToolBar();
    }

    @FXML
    void onPlayLevelAction(ActionEvent event) {
        if (selectedLevelMetadata != null) {
            levelStarter.start(selectedLevelMetadata, discipleProfile.getCurrentData().getJob());
            sceneManager.switchScene(ViewRoute.GAMEPLAY);
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    @FXML
    void onGoToShopButton(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.SHOP);
    }

    private void setupDiscipleData() {
        DiscipleData data = discipleProfile.getCurrentData();
        String gifPath = discipleProfile.getAvatarGifPath();
        Image gif = ImageUtils.loadImage(gifPath);

        discipleHeaderController.initData(data, gif);
    }

    private void initCategoryToolBar() {

        ToggleGroup categoryGroup = new ToggleGroup();

        List<CategoryButtonComponent> buttons = Arrays.stream(LevelCategory.values())
                .map(category -> {
                    CategoryButtonComponent btn = new CategoryButtonComponent(category, this::loadCardsForCategory);
                    btn.setToggleGroup(categoryGroup);
                    return btn;
                })
                .toList();

        categoryToolBar.getItems().setAll(buttons);

    }

    private void loadCardsForCategory(LevelCategory category) {
        resetUI();
        List<LevelMetadata> categoryMetadataList = levelBrowser.getLevelsByCategory(category);
        populateGrid(categoryMetadataList);
    }

    private void populateGrid(List<LevelMetadata> metadataList) {
        List<LevelCardNode> cards = metadataList.stream()
                .map(this::generateLevelCard)
                .toList();

        levelsContainer.getChildren().setAll(cards);
    }

    private LevelCardNode generateLevelCard(LevelMetadata levelMetadata) {
        Optional<Score> bestAttempt = levelBrowser.getScoreForLevel(levelMetadata.id());
        LevelCompletionState state = LevelCompletionState.evaluate(levelMetadata.maxRewards(), bestAttempt);

        return new LevelCardNode(levelMetadata, state, clickedCard -> handleCardClick(levelMetadata, clickedCard));
    }

    private void handleCardClick(LevelMetadata levelMetadata, LevelCardNode clickedCard) {
        if (levelMetadata.equals(this.selectedLevelMetadata)) {
            clearSelection();
        } else {
            applyNewSelection(levelMetadata, clickedCard);
        }
    }

    private void applyNewSelection(LevelMetadata levelMetadata, LevelCardNode clickedCard) {
        if (selectedCardNode != null) {
            selectedCardNode.setSelected(false);
        }

        selectedCardNode = clickedCard;
        selectedCardNode.setSelected(true);
        selectedLevelMetadata = levelMetadata;

        refreshDetailsContainer(levelMetadata);

        boolean isUnlocked = levelBrowser.isLevelUnlocked(levelMetadata);
        playButton.setDisable(!isUnlocked);
    }

    private void refreshDetailsContainer(LevelMetadata levelMetadata) {
        List<String> bookNames = levelBrowser.getRequiredBookNames(levelMetadata);
        String formattedBookNames = formatRequiredBooks(bookNames);

        Optional<Score> bestAttempt = levelBrowser.getScoreForLevel(levelMetadata.id());

        detailsContainer.getChildren().setAll(new LevelDetailsNode(levelMetadata, formattedBookNames, bestAttempt));
    }

    private String formatRequiredBooks(List<String> bookNames) {
        if (bookNames == null || bookNames.isEmpty()) {
            return "Nessun requisito";
        }
        return String.join(", ", bookNames);
    }

    private void clearSelection() {
        if (selectedCardNode != null) {
            selectedCardNode.setSelected(false);
        }

        selectedLevelMetadata = null;
        selectedCardNode = null;

        playButton.setDisable(true);
        detailsContainer.getChildren().clear();
    }

    private void resetUI() {
        levelsContainer.getChildren().clear();
        clearSelection();
    }
}