package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.GameStorageException;
import it.unicam.cs.mpgc.rpg129852.SaveCorruptedException;
import it.unicam.cs.mpgc.rpg129852.SaveNotFoundException;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.GameLoader;
import it.unicam.cs.mpgc.rpg129852.ui.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.ui.SaveRowComponent;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.List;

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
    private final GameLoader gameLoader;
    private final ViewRouter sceneManager;
    private Image trashImageCache;

    @FXML
    private VBox savesContainer;

    public LoadGameController(GameRepository repository, GameLoader gameLoader, ViewRouter sceneManager) {
        this.repository = repository;
        this.gameLoader = gameLoader;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        trashImageCache = ImageUtils.loadImage(TRASH_ICON_PATH);
        refreshSavesList();
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

            gameLoader.loadGame(saveName);
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

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}