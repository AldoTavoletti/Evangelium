package it.unicam.cs.mpgc.rpg129852.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.function.Consumer;

public class SaveRowComponent extends HBox {

    private static final String LOAD_BTN_CLASS = "load-btn";
    private static final String DELETE_BTN_CLASS = "delete-btn";
    private static final double ICON_SIZE = 24.0;

    public SaveRowComponent(String saveName, Image trashImageCache, Consumer<String> onLoad, Consumer<String> onDelete) {
        super(10);
        this.setAlignment(Pos.CENTER);

        Button loadButton = createLoadButton(saveName, onLoad);
        Button deleteButton = createDeleteButton(saveName, trashImageCache, onDelete);

        this.getChildren().addAll(loadButton, deleteButton);
    }

    private Button createLoadButton(String saveName, Consumer<String> onLoad) {
        Button loadButton = new Button(saveName);
        loadButton.getStyleClass().add(LOAD_BTN_CLASS);
        loadButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(loadButton, Priority.ALWAYS);
        loadButton.setOnAction(event -> onLoad.accept(saveName));
        return loadButton;
    }

    private Button createDeleteButton(String saveName, Image trashImageCache, Consumer<String> onDelete) {
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

        deleteButton.setOnAction(event -> onDelete.accept(saveName));
        return deleteButton;
    }
}