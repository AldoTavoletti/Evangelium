package it.unicam.cs.mpgc.rpg129852.ui;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class LevelCardNode extends VBox {

    public LevelCardNode(LevelMetadata level, boolean isUnlocked, Consumer<LevelCardNode> onSelectAction) {
        this.getStyleClass().add("level-card");

        Label titleLabel = new Label(level.title());
        titleLabel.getStyleClass().add("level-card-title");
        this.getChildren().add(titleLabel);

        if (!isUnlocked) {
            this.setOpacity(0.5);
        } else {
            this.getStyleClass().add("level-card-unlocked");

            // Passa l'istanza corrente (this) al Consumer quando viene cliccata
            this.setOnMouseClicked(e -> onSelectAction.accept(this));
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            this.getStyleClass().add("level-card-selected");
        } else {
            this.getStyleClass().remove("level-card-selected");
        }
    }
}