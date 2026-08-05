package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.game.GameLoader;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadGameControllerTest {

    private FakeGameRepository repository;
    private FakeGameLoader gameLoader;
    private FakeViewRouter viewRouter;
    private LoadGameController controller;
    private VBox savesContainer;

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        repository = new FakeGameRepository();
        gameLoader = new FakeGameLoader();
        viewRouter = new FakeViewRouter();
        controller = new LoadGameController(repository, gameLoader, viewRouter);
        savesContainer = new VBox();

        Field containerField = LoadGameController.class.getDeclaredField("savesContainer");
        containerField.setAccessible(true);
        containerField.set(controller, savesContainer);
    }

    @Test
    void initialize_withNoSaves_showsEmptyMessage() {
        givenNoAvailableSaves();
        whenControllerIsInitialized();
        thenContainerShowsEmptyMessage();
    }

    @Test
    void initialize_withSaves_populatesContainerWithRows() {
        givenAvailableSaves("Save1", "Save2");
        whenControllerIsInitialized();
        thenContainerHasSaveRows(2);
    }

    @Test
    void clickLoadButton_callsGameLoader() {
        givenAvailableSaves("MySave");
        whenControllerIsInitialized();
        whenLoadButtonIsClickedForSave(0);
        thenGameLoaderIsCalledWith("MySave");
    }

    @Test
    void returnToMenuAction_switchesToMainMenu() {
        whenReturnToMenuIsTriggered();
        thenViewRouterSwitchedTo(ViewRoute.MAIN_MENU);
    }

    private void givenNoAvailableSaves() {
        repository.savesToReturn = Collections.emptyList();
    }

    private void givenAvailableSaves(String... saves) {
        repository.savesToReturn = Arrays.asList(saves);
    }

    private void whenControllerIsInitialized() {
        controller.initialize();
    }

    private void whenLoadButtonIsClickedForSave(int index) {
        HBox row = (HBox) savesContainer.getChildren().get(index);
        Button loadButton = (Button) row.getChildren().get(0);
        loadButton.fire();
    }

    private void whenReturnToMenuIsTriggered() {
        controller.onReturnToMenuAction(null);
    }

    private void thenContainerShowsEmptyMessage() {
        assertEquals(1, savesContainer.getChildren().size());
        assertTrue(savesContainer.getChildren().get(0) instanceof Label);
        Label label = (Label) savesContainer.getChildren().get(0);
        assertEquals("Nessun salvataggio trovato.", label.getText());
    }

    private void thenContainerHasSaveRows(int expectedCount) {
        assertEquals(expectedCount, savesContainer.getChildren().size());
        assertTrue(savesContainer.getChildren().get(0) instanceof HBox);
    }

    private void thenGameLoaderIsCalledWith(String expectedSaveName) {
        assertEquals(expectedSaveName, gameLoader.loadedSaveName);
    }

    private void thenViewRouterSwitchedTo(ViewRoute expectedRoute) {
        assertEquals(expectedRoute, viewRouter.switchedRoute);
    }

    class FakeGameRepository implements GameRepository {
        public List<String> savesToReturn = Collections.emptyList();

        @Override
        public void save(Game game) {}

        @Override
        public Game load(String saveName) {
            return null;
        }

        @Override
        public void delete(String saveName) {}

        @Override
        public List<String> getAvailableSaves() {
            return savesToReturn;
        }
    }

    class FakeGameLoader implements GameLoader {
        public String loadedSaveName;

        @Override
        public void loadGame(String saveName) {
            this.loadedSaveName = saveName;
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
