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

            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER);

            Button loadButton = new Button(saveName);
            loadButton.getStyleClass().add("save-btn");
            loadButton.setMaxWidth(Double.MAX_VALUE);

            HBox.setHgrow(loadButton, Priority.ALWAYS);
            loadButton.setOnAction(event -> loadSelectedGame(saveName));

            Button deleteButton = new Button();
            deleteButton.getStyleClass().add("delete-btn");

            try {
                ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/recycle-bin.png")));
                trashIcon.setFitWidth(24);
                trashIcon.setFitHeight(24);
                deleteButton.setGraphic(trashIcon);
            } catch (Exception e) {
                deleteButton.setText("X");
            }

            deleteButton.setOnAction(event -> confirmAndDeleteSave(saveName));

            row.getChildren().addAll(loadButton, deleteButton);
            savesContainer.getChildren().add(row);
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

    private void confirmAndDeleteSave(String saveName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina Salvataggio");
        alert.setHeaderText("Stai per eliminare il salvataggio: " + saveName);
        alert.setContentText("L'operazione è irreversibile. Vuoi continuare?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                repository.delete(saveName);

                initSavesList();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Errore");
                errorAlert.setHeaderText("Impossibile eliminare il file");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}