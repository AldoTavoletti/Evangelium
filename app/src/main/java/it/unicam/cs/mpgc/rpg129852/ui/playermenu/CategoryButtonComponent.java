package it.unicam.cs.mpgc.rpg129852.ui.playermenu;

import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import javafx.scene.control.ToggleButton;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A custom toggle button representing a specific level category in the user interface.
 * It encapsulates the category data and triggers a predefined action when selected by the user.
 */
public class CategoryButtonComponent extends ToggleButton {

    private static final String STYLE_CLASS_BUTTON = "button";
    private static final LevelCategory DEFAULT_CATEGORY = LevelCategory.FAITH;

    /**
     * Constructs a new category toggle button.
     * If the provided category matches the default category, the button automatically
     * selects itself and fires the associated action upon creation.
     *
     * @param category the level category associated with this button
     * @param onAction the callback to be executed when the button is selected
     * @throws NullPointerException if either the category or the callback is null
     */
    public CategoryButtonComponent(LevelCategory category, Consumer<LevelCategory> onAction) {
        super(Objects.requireNonNull(category, "The category must not be null.").getDisplayName());
        Objects.requireNonNull(onAction, "The onAction callback must not be null.");

        this.getStyleClass().add(STYLE_CLASS_BUTTON);

        this.setOnAction(e -> onAction.accept(category));

        if (category.equals(DEFAULT_CATEGORY)) {
            this.fire();
            this.setSelected(true);
        }
    }
}