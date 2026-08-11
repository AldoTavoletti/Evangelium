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
import it.unicam.cs.mpgc.rpg129852.service.summary.SummaryService;
import it.unicam.cs.mpgc.rpg129852.ui.playermenu.CategoryButtonComponent;
import it.unicam.cs.mpgc.rpg129852.ui.level.LevelCardNode;
import it.unicam.cs.mpgc.rpg129852.ui.level.LevelDetailsNode;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller for the player's main hub menu.
 * Handles level browsing, category selection, displaying level requirements,
 * and navigating to the shop, gameplay, or summary screens.
 */
public class PlayerMenuController {

    private final SummaryService summaryService;
    private final DiscipleProfileService discipleProfile;
    private final LevelStarter levelStarter;
    private final LevelBrowserService levelBrowser;
    private final ViewRouter sceneManager;

    @FXML
    private DiscipleHeaderController discipleHeaderController;
    @FXML
    private Button playButton;
    @FXML
    private Button summaryButton;
    @FXML
    private ToolBar categoryToolBar;
    @FXML
    private TilePane levelsContainer;
    @FXML
    private VBox detailsContainer;

    private LevelMetadata selectedLevelMetadata;
    private LevelCardNode selectedCardNode;

    /**
     * Constructs the player menu controller with its required dependencies.
     *
     * @param summaryService  the service evaluating overall game completion
     * @param discipleProfile the service managing the player's character data
     * @param levelStarter    the service handling the transition into a playable level
     * @param levelBrowser    the service fetching level metadata and unlock requirements
     * @param sceneManager    the router responsible for switching views
     * @throws NullPointerException if any of the dependencies are null
     */
    public PlayerMenuController(SummaryService summaryService,
                                DiscipleProfileService discipleProfile,
                                LevelStarter levelStarter,
                                LevelBrowserService levelBrowser,
                                ViewRouter sceneManager) {
        this.summaryService = Objects.requireNonNull(summaryService, "The summary service must not be null.");
        this.discipleProfile = Objects.requireNonNull(discipleProfile, "The disciple profile service must not be null.");
        this.levelStarter = Objects.requireNonNull(levelStarter, "The level starter must not be null.");
        this.levelBrowser = Objects.requireNonNull(levelBrowser, "The level browser service must not be null.");
        this.sceneManager = Objects.requireNonNull(sceneManager, "The scene manager must not be null.");
    }

    @FXML
    public void initialize() {
        evaluateSummaryVisibility();
        initializeDiscipleHeader();
        initializeCategoryToolBar();
    }

    @FXML
    void onPlayButtonClicked() {
        if (selectedLevelMetadata != null) {
            levelStarter.start(selectedLevelMetadata, discipleProfile.getCurrentData().getJob());
            sceneManager.switchScene(ViewRoute.GAMEPLAY);
        }
    }

    @FXML
    void onBackToMenuButtonClicked() {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    @FXML
    void onShopButtonClicked() {
        sceneManager.switchScene(ViewRoute.SHOP);
    }

    @FXML
    void onSummaryButtonClicked() {
        sceneManager.switchScene(ViewRoute.SUMMARY);
    }

    private void evaluateSummaryVisibility() {
        if (summaryService.areAllLevelsWon()) {
            summaryButton.setManaged(true);
            summaryButton.setVisible(true);
            summaryService.setSummaryShown(true);
        }
    }

    private void initializeDiscipleHeader() {
        DiscipleData data = discipleProfile.getCurrentData();
        String gifPath = discipleProfile.getGifPath();
        Image gif = ImageUtils.loadImage(gifPath);

        discipleHeaderController.initData(data, gif);
    }

    private void initializeCategoryToolBar() {
        ToggleGroup categoryGroup = new ToggleGroup();

        List<CategoryButtonComponent> buttons = Arrays.stream(LevelCategory.values())
                .map(category -> createCategoryButton(category, categoryGroup))
                .toList();

        categoryToolBar.getItems().setAll(buttons);
    }

    private CategoryButtonComponent createCategoryButton(LevelCategory category, ToggleGroup categoryGroup) {
        CategoryButtonComponent button = new CategoryButtonComponent(category, this::loadLevelCardsForCategory);
        button.setToggleGroup(categoryGroup);
        return button;
    }

    private void loadLevelCardsForCategory(LevelCategory category) {
        levelsContainer.getChildren().clear();
        clearSelection();

        List<LevelCardNode> cards = levelBrowser.getLevelsMetadataByCategory(category).stream()
                .map(this::createLevelCard)
                .toList();

        levelsContainer.getChildren().setAll(cards);
    }

    private LevelCardNode createLevelCard(LevelMetadata levelMetadata) {
        Optional<Score> bestAttempt = levelBrowser.getScoreForLevel(levelMetadata.id());

        LevelCompletionState state = bestAttempt
                .map(Score::completionState)
                .orElse(LevelCompletionState.NONE);

        return new LevelCardNode(levelMetadata, state, clickedCard -> handleCardSelection(levelMetadata, clickedCard));
    }

    private void handleCardSelection(LevelMetadata levelMetadata, LevelCardNode clickedCard) {
        boolean isCardAlreadySelected = levelMetadata.equals(selectedLevelMetadata);

        if (isCardAlreadySelected) {
            clearSelection();
        } else {
            selectNewCard(levelMetadata, clickedCard);
        }
    }

    private void selectNewCard(LevelMetadata levelMetadata, LevelCardNode clickedCard) {
        clearSelection();

        selectedCardNode = clickedCard;
        selectedCardNode.setSelected(true);
        selectedLevelMetadata = levelMetadata;

        updateLevelDetailsView(levelMetadata);
        updatePlayButtonState(levelMetadata);
    }

    private void updateLevelDetailsView(LevelMetadata levelMetadata) {
        List<String> bookNames = levelBrowser.getRequiredBookNames(levelMetadata);
        String formattedBookNames = formatRequiredBooks(bookNames);
        Optional<Score> bestAttempt = levelBrowser.getScoreForLevel(levelMetadata.id());

        detailsContainer.getChildren().setAll(new LevelDetailsNode(levelMetadata, formattedBookNames, bestAttempt));
    }

    private void updatePlayButtonState(LevelMetadata levelMetadata) {
        boolean isUnlocked = levelBrowser.isLevelUnlocked(levelMetadata);
        playButton.setDisable(!isUnlocked);
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
}