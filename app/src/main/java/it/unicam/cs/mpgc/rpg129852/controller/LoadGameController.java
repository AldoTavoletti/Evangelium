package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.GameLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class LoadGameController {

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
        initSavesList();
    }

    private void initSavesList() {
        savesContainer.getChildren().clear();

        List<String> saves = repository.getAvailableSaves();

        if (saves.isEmpty()) {
            savesContainer.getChildren().add(new Label("Nessun salvataggio trovato."));
            return;
        }

        for (String saveName : saves) {
            Button saveButton = new Button(saveName);

            saveButton.setMaxWidth(Double.MAX_VALUE);

            saveButton.setOnAction(event -> loadSelectedGame(saveName));

            savesContainer.getChildren().add(saveButton);
        }
    }

    private void loadSelectedGame(String saveName) {
        try {
            gameLoader.loadGame(saveName);

            //todo: switch to next scene

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile caricare il salvataggio");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}