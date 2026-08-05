package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.dto.AssetRegistry;
import it.unicam.cs.mpgc.rpg129852.dto.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarter;
import it.unicam.cs.mpgc.rpg129852.service.NewGameRequest;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscipleCreationControllerTest {

    private FakeGameStarter gameStarter;
    private StubAssetRegistry assetRegistry;
    private FakeViewRouter viewRouter;
    private DiscipleCreationController controller;

    private ImageView currentGifImage;
    private ChoiceBox<String> jobSelector;
    private TextField discipleNameField;
    private TextField saveNameField;
    private Button startGameButton;

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        gameStarter = new FakeGameStarter();
        assetRegistry = new StubAssetRegistry();
        viewRouter = new FakeViewRouter();
        List<String> jobs = List.of("Warrior", "Mage");

        controller = new DiscipleCreationController(gameStarter, assetRegistry, jobs, viewRouter);

        currentGifImage = new ImageView();
        jobSelector = new ChoiceBox<>();
        discipleNameField = new TextField();
        saveNameField = new TextField();
        startGameButton = new Button();

        injectField("currentGifImage", currentGifImage);
        injectField("jobSelector", jobSelector);
        injectField("discipleNameField", discipleNameField);
        injectField("saveNameField", saveNameField);
        injectField("startGameButton", startGameButton);
    }

    @Test
    void initialize_setsInitialStateCorrectly() {
        whenControllerIsInitialized();
        thenJobSelectorHasItemsAndSelectsFirst("Warrior", "Mage");
        thenStartGameButtonIsDisabled();
    }

    @Test
    void typingName_enablesStartGameButton() {
        whenControllerIsInitialized();
        whenDiscipleNameIsEntered("Arthur");
        thenStartGameButtonIsEnabled();
    }

    @Test
    void returnToMenuAction_switchesToMainMenu() {
        whenReturnToMenuIsTriggered();
        thenViewRouterSwitchedTo(ViewRoute.MAIN_MENU);
    }

    @Test
    void startGameAction_sendsCorrectRequest() {
        whenControllerIsInitialized();
        whenDiscipleNameIsEntered("Arthur");
        whenSaveNameIsEntered("Save1");
        whenStartGameIsTriggered();
        thenGameStarterReceivedRequest("Arthur", "Warrior", "1", "Save1");
    }

    @Test
    void nextGifAction_changesSelectedAssetId() {
        whenControllerIsInitialized();
        whenNextGifIsTriggered();
        whenDiscipleNameIsEntered("Arthur");
        whenStartGameIsTriggered();
        thenGameStarterReceivedRequest("Arthur", "Warrior", "2", "");
    }

    @Test
    void previousGifAction_changesSelectedAssetId() {
        whenControllerIsInitialized();
        whenPreviousGifIsTriggered();
        whenDiscipleNameIsEntered("Arthur");
        whenStartGameIsTriggered();
        thenGameStarterReceivedRequest("Arthur", "Warrior", "2", "");
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = DiscipleCreationController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private void whenControllerIsInitialized() {
        controller.initialize();
    }

    private void whenDiscipleNameIsEntered(String name) {
        discipleNameField.setText(name);
    }

    private void whenSaveNameIsEntered(String saveName) {
        saveNameField.setText(saveName);
    }

    private void whenNextGifIsTriggered() {
        controller.onNextGifAction(null);
    }

    private void whenPreviousGifIsTriggered() {
        controller.onPreviousGifAction(null);
    }

    private void whenStartGameIsTriggered() {
        controller.onStartGameAction(null);
    }

    private void whenReturnToMenuIsTriggered() {
        controller.onReturnToMenuAction(null);
    }

    private void thenJobSelectorHasItemsAndSelectsFirst(String... expectedJobs) {
        assertEquals(List.of(expectedJobs), jobSelector.getItems());
        assertEquals(expectedJobs[0], jobSelector.getSelectionModel().getSelectedItem());
    }

    private void thenStartGameButtonIsDisabled() {
        assertTrue(startGameButton.isDisabled());
    }

    private void thenStartGameButtonIsEnabled() {
        assertFalse(startGameButton.isDisabled());
    }

    private void thenGameStarterReceivedRequest(String name, String job, String color, String saveName) {
        assertEquals(name, gameStarter.receivedRequest.discipleName());
        assertEquals(job, gameStarter.receivedRequest.job());
        assertEquals(color, gameStarter.receivedRequest.color());
        assertEquals(saveName, gameStarter.receivedRequest.saveName());
    }

    private void thenViewRouterSwitchedTo(ViewRoute expectedRoute) {
        assertEquals(expectedRoute, viewRouter.switchedRoute);
    }

    class FakeGameStarter implements GameStarter {
        public NewGameRequest receivedRequest;

        @Override
        public void startNewGame(NewGameRequest request) {
            this.receivedRequest = request;
        }

        @Override
        public void overwriteAndStartNewGame(NewGameRequest request) {
            this.receivedRequest = request;
        }
    }

    class StubAssetRegistry implements AssetRegistry<DiscipleAsset> {
        @Override
        public void loadAssets(String filePath) {
        }

        @Override
        public List<DiscipleAsset> getAllAssets() {
            return List.of(
                    new DiscipleAsset("1", "file:dummy1.gif", "file:dummy1.png"),
                    new DiscipleAsset("2", "file:dummy2.gif", "file:dummy2.png")
            );
        }

        @Override
        public DiscipleAsset getAsset(String id) {
            return null;
        }
    }

    class FakeViewRouter implements ViewRouter {
        public ViewRoute switchedRoute;

        @Override
        public void switchScene(ViewRoute route) {
            this.switchedRoute = route;
        }
    }
}