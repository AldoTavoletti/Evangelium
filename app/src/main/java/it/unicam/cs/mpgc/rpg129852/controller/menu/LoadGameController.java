package it.unicam.cs.mpgc.rpg129852.controller.menu;

import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameStorageException;
import it.unicam.cs.mpgc.rpg129852.persistence.game.SaveCorruptedException;
import it.unicam.cs.mpgc.rpg129852.persistence.game.SaveNotFoundException;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.ui.common.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.ui.save.SaveRowComponent;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

/**
 * Controller for the Load Game screen.
 * It manages the display of available save files, allowing the player to load
 * an existing session or delete unwanted saves.
 */
public class LoadGameController {

    private static final String TRASH_ICON_PATH = "/images/recycle-bin.png";
    private static final String EMPTY_SAVES_MSG = "Nessun salvataggio trovato.";

    private static final String DELETE_TITLE = "Elimina Salvataggio";
    private static final String DELETE_HEADER = "Stai per eliminare il salvataggio: ";
    private static final String DELETE_CONTENT = "L'operazione è irreversibile. Vuoi continuare?";

    private static final String ERR_LOAD_MSG = "Impossibile caricare il salvataggio";
    private static final String ERR_DELETE_MSG = "Impossibile eliminare il file";
    private static final String ERR_CORRUPTED_MSG = "Il file di salvataggio è danneggiato o illeggibile.\n";
    private static final String ERR_NOT_FOUND_MSG = "Il salvataggio non esiste più sul disco.";
    private static final String ERR_STORAGE_MSG = "Si è verificato un errore di accesso al disco.\n";
    private static final String ERR_DELETE_NOT_FOUND_MSG = "Il salvataggio è già stato rimosso o non esiste.";
    private static final String ERR_DELETE_STORAGE_MSG = "Impossibile accedere al file per l'eliminazione.\n";

    private final GameRepository repository;
    private final GameSessionManager gameSessionManager;
    private final ViewRouter sceneManager;
    private Image trashImageCache;

    @FXML
    private VBox savesContainer;

    /**
     * Constructs the controller with its required dependencies.
     *
     * @param repository         the repository handling the physical save files
     * @param gameSessionManager the manager to set the loaded game into the active session
     * @param sceneManager       the router responsible for switching views
     * @throws NullPointerException if any of the dependencies are null
     */
    public LoadGameController(GameRepository repository, GameSessionManager gameSessionManager, ViewRouter sceneManager) {
        this.repository = Objects.requireNonNull(repository, "The game repository must not be null.");
        this.gameSessionManager = Objects.requireNonNull(gameSessionManager, "The game session manager must not be null.");
        this.sceneManager = Objects.requireNonNull(sceneManager, "The scene manager must not be null.");
    }

    @FXML
    public void initialize() {
        trashImageCache = ImageUtils.loadImage(TRASH_ICON_PATH);
        refreshSavesList();
    }

    @FXML
    void onBackToMenuButtonClicked() {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private void refreshSavesList() {
        savesContainer.getChildren().clear();

        List<String> saveNames = repository.getAvailableSaves();

        if (saveNames.isEmpty()) {
            savesContainer.getChildren().add(new Label(EMPTY_SAVES_MSG));
            return;
        }

        List<SaveRowComponent> saveRows = generateSaveRows(saveNames);
        savesContainer.getChildren().addAll(saveRows);
    }

    private void loadSelectedGame(String saveName) {
        try {
            Game loadedGame = repository.load(saveName);
            gameSessionManager.setCurrentGame(loadedGame);
            sceneManager.switchScene(ViewRoute.PLAYER_MENU);

        } catch (SaveCorruptedException e) {
            AlertHelper.showError(ERR_LOAD_MSG, ERR_CORRUPTED_MSG + e.getMessage());
        } catch (SaveNotFoundException e) {
            AlertHelper.showError(ERR_LOAD_MSG, ERR_NOT_FOUND_MSG);
            refreshSavesList();
        } catch (GameStorageException e) {
            AlertHelper.showError(ERR_LOAD_MSG, ERR_STORAGE_MSG + e.getMessage());
        }
    }

    private void confirmAndDeleteSave(String saveName) {
        if (AlertHelper.askConfirmation(DELETE_TITLE, DELETE_HEADER + saveName, DELETE_CONTENT)) {
            executeDeletion(saveName);
        }
    }

    private void executeDeletion(String saveName) {
        try {
            repository.delete(saveName);
            refreshSavesList();

        } catch (SaveNotFoundException e) {
            AlertHelper.showError(ERR_DELETE_MSG, ERR_DELETE_NOT_FOUND_MSG);
            refreshSavesList();
        } catch (GameStorageException e) {
            AlertHelper.showError(ERR_DELETE_MSG, ERR_DELETE_STORAGE_MSG + e.getMessage());
        }
    }

    private List<SaveRowComponent> generateSaveRows(List<String> saveNames) {
        return saveNames.stream()
                .map(saveName -> new SaveRowComponent(saveName, trashImageCache, this::loadSelectedGame, this::confirmAndDeleteSave))
                .toList();
    }

}