package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.GameLoader;
import it.unicam.cs.mpgc.rpg129852.ui.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class LoadGameController {

    private static final String TRASH_ICON_PATH = "/images/recycle-bin.png";
    private static final double ICON_SIZE = 24.0;
    private static final String LOAD_BTN_CLASS = "load-btn";
    private static final String DELETE_BTN_CLASS = "delete-btn";

    private static final String DELETE_TITLE = "Elimina Salvataggio";
    private static final String DELETE_HEADER = "Stai per eliminare il salvataggio: ";
    private static final String DELETE_CONTENT = "L'operazione è irreversibile. Vuoi continuare?";

    private static final String EMPTY_SAVES_MSG = "Nessun salvataggio trovato.";
    private static final String ERR_LOAD_MSG = "Impossibile caricare il salvataggio";
    private static final String ERR_DELETE_MSG = "Impossibile eliminare il file";

    @FXML
    private VBox savesContainer;

    private final GameRepository repository;
    private final GameLoader gameLoader;
    private final ViewRouter sceneManager;

    private Image trashImageCache;

    public LoadGameController(GameRepository repository, GameLoader gameLoader, ViewRouter sceneManager) {
        this.repository = repository;
        this.gameLoader = gameLoader;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        preloadTrashImage();
        refreshSavesList();
    }

    private void preloadTrashImage() {
        try {
            trashImageCache = new Image(getClass().getResourceAsStream(TRASH_ICON_PATH));
        } catch (Exception e) {
            trashImageCache = null;
        }
    }

    private void refreshSavesList() {
        savesContainer.getChildren().clear();

        List<String> saves = repository.getAvailableSaves();

        if (saves.isEmpty()) {
            savesContainer.getChildren().add(new Label(EMPTY_SAVES_MSG));
            return;
        }

        for (String saveName : saves) {
            savesContainer.getChildren().add(createSaveRow(saveName));
        }
    }

    private HBox createSaveRow(String saveName) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);

        Button loadButton = createLoadButton(saveName);
        Button deleteButton = createDeleteButton(saveName);

        row.getChildren().addAll(loadButton, deleteButton);
        return row;
    }

    private Button createLoadButton(String saveName) {
        Button loadButton = new Button(saveName);
        loadButton.getStyleClass().add(LOAD_BTN_CLASS);
        loadButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(loadButton, Priority.ALWAYS);

        loadButton.setOnAction(event -> loadSelectedGame(saveName));

        return loadButton;
    }

    private Button createDeleteButton(String saveName) {
        Button deleteButton = new Button();
        deleteButton.getStyleClass().add(DELETE_BTN_CLASS);

        if (trashImageCache != null && !trashImageCache.isError()) {
            ImageView trashIcon = new ImageView(trashImageCache);
            trashIcon.setFitWidth(ICON_SIZE);
            trashIcon.setFitHeight(ICON_SIZE);
            deleteButton.setGraphic(trashIcon);
        } else {
            deleteButton.setText("X");
        }

        deleteButton.setOnAction(event -> confirmAndDeleteSave(saveName));

        return deleteButton;
    }

    private void loadSelectedGame(String saveName) {
        try {
            gameLoader.loadGame(saveName);
            // todo: switch to next scene
        } catch (Exception e) {
            AlertHelper.showError(ERR_LOAD_MSG, e.getMessage());
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
        } catch (Exception e) {
            AlertHelper.showError(ERR_DELETE_MSG, e.getMessage());
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}