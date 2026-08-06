package it.unicam.cs.mpgc.rpg129852.ui.level;

import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import javafx.scene.control.Button;
import java.util.function.Consumer;

public class CategoryButtonComponent extends Button {

    private static final String STYLE_CLASS_BUTTON = "button";

    public CategoryButtonComponent(LevelCategory category, Consumer<LevelCategory> onAction) {
        super(category.getDisplayName());
        this.getStyleClass().add(STYLE_CLASS_BUTTON);
        this.setOnAction(e -> onAction.accept(category));
    }
}