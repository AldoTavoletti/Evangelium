package it.unicam.cs.mpgc.rpg129852.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class SaveRowComponent extends HBox {

    private static final String LOAD_BTN_CLASS = "load-btn";
    private static final String DELETE_BTN_CLASS = "delete-btn";
    private static final String FALLBACK_DELETE_TEXT = "X";
    private static final double ICON_SIZE = 24.0;

    public SaveRowComponent(final String saveName, final Image trashImage, final Consumer<String> onLoad, final Consumer<String> onDelete) {
        super(10);
        this.setAlignment(Pos.CENTER);

        Button loadButton = createLoadButton(saveName, onLoad);
        Button deleteButton = createDeleteButton(saveName, trashImage, onDelete);

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

    private Button createDeleteButton(String saveName, Image trashImage, Consumer<String> onDelete) {
        Button deleteButton = new Button();
        deleteButton.getStyleClass().add(DELETE_BTN_CLASS);

        applyIconOrFallbackText(deleteButton, trashImage);

        deleteButton.setOnAction(event -> onDelete.accept(saveName));

        return deleteButton;
    }

    private void applyIconOrFallbackText(Button button, Image iconImage) {
        if (iconImage != null && !iconImage.isError()) {
            button.setGraphic(createIconView(iconImage));
        } else {
            button.setText(FALLBACK_DELETE_TEXT);
        }
    }

    private @NonNull ImageView createIconView(Image image) {
        ImageView iconView = new ImageView(image);
        iconView.setFitWidth(ICON_SIZE);
        iconView.setFitHeight(ICON_SIZE);
        return iconView;
    }
}