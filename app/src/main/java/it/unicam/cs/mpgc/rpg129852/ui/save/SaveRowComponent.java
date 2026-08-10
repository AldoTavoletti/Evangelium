package it.unicam.cs.mpgc.rpg129852.ui.save;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A custom UI component representing a single row in the load game screen.
 * It provides interactive elements to either load or delete a specific save file.
 */
public class SaveRowComponent extends HBox {

    private static final double DEFAULT_SPACING = 10.0;

    private static final String LOAD_BTN_CLASS = "load-btn";
    private static final String DELETE_BTN_CLASS = "delete-btn";
    private static final String FALLBACK_DELETE_TEXT = "X";
    private static final double ICON_SIZE = 24.0;

    /**
     * Constructs a new save row component.
     *
     * @param saveName   the name of the save file, displayed on the load button
     * @param trashImage the icon to display on the delete button; if null or broken, a text fallback is used
     * @param onLoad     the callback triggered when the load button is clicked
     * @param onDelete   the callback triggered when the delete button is clicked
     * @throws NullPointerException if saveName, onLoad, or onDelete is null
     */
    public SaveRowComponent(final String saveName, final Image trashImage, final Consumer<String> onLoad, final Consumer<String> onDelete) {
        super(DEFAULT_SPACING);

        Objects.requireNonNull(saveName, "The save name must not be null.");
        Objects.requireNonNull(onLoad, "The onLoad callback must not be null.");
        Objects.requireNonNull(onDelete, "The onDelete callback must not be null.");

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