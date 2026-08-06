package it.unicam.cs.mpgc.rpg129852.ui.level;

import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

import java.util.function.Consumer;

public class CategoryButtonComponent extends ToggleButton {

    private static final String STYLE_CLASS_BUTTON = "button";
    private static final LevelCategory DEFAULT_CATEGORY = LevelCategory.FAITH;


    public CategoryButtonComponent(LevelCategory category, Consumer<LevelCategory> onAction) {
        super(category.getDisplayName());
        this.getStyleClass().add(STYLE_CLASS_BUTTON);

        this.setOnAction(e -> onAction.accept(category));

        if (category.equals(LevelCategory.FAITH)) {
            this.fire();
            this.setSelected(true);
        }

    }
}