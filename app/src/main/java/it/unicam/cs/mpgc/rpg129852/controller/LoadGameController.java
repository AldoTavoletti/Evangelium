package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.GameLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class LoadGameController {

    private static final String TRASH_ICON_PATH = "/images/recycle-bin.png";
    private static final double ICON_SIZE = 24.0;
    private static final String LOAD_BTN_CLASS = "load-btn";
    private static final String DELETE_BTN_CLASS = "delete-btn";

    @FXML
    private VBox savesContainer;

    private final GameRepository repository;
    private final GameLoader gameLoader;
    private final ViewRouter sceneManager;

    public LoadGameController(GameRepository repository, GameLoader gameLoader, ViewRouter sceneManager) {
        this.repository = repository;
        this.gameLoader = gameLoader;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        refreshSavesList();
    }

    private void refreshSavesList() {
        savesContainer.getChildren().clear();

        List<String> saves = repository.getAvailableSaves();

        if (saves.isEmpty()) {
            savesContainer.getChildren().add(new Label("Nessun salvataggio trovato."));
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

        try {
            ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream(TRASH_ICON_PATH)));
            trashIcon.setFitWidth(ICON_SIZE);
            trashIcon.setFitHeight(ICON_SIZE);
            deleteButton.setGraphic(trashIcon);
        } catch (Exception e) {
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
            showErrorAlert("Impossibile caricare il salvataggio", e.getMessage());
        }
    }

    private void confirmAndDeleteSave(String saveName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina Salvataggio");
        alert.setHeaderText("Stai per eliminare il salvataggio: " + saveName);
        alert.setContentText("L'operazione è irreversibile. Vuoi continuare?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            executeDeletion(saveName);
        }
    }

    private void executeDeletion(String saveName) {
        try {
            repository.delete(saveName);
            refreshSavesList();
        } catch (Exception e) {
            showErrorAlert("Impossibile eliminare il file", e.getMessage());
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}