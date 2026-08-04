package it.unicam.cs.mpgc.rpg129852.ui;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;

public class LevelCardNode extends VBox {

    public LevelCardNode(LevelMetadata level, boolean isUnlocked, Consumer<LevelCardNode> onSelectAction) {
        this.getStyleClass().add("level-card");

        // Altezza ridotta a 110 per una forma più rettangolare ed elegante
        this.setPrefSize(240, 110);
        this.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(level.title());
        titleLabel.getStyleClass().add("level-card-title");

        // Centra il testo multiriga
        titleLabel.setTextAlignment(TextAlignment.CENTER);

        // NOVITÀ: Centra fisicamente il contenuto della Label all'interno del suo spazio
        titleLabel.setAlignment(Pos.CENTER);

        // Obbliga la label a non superare i 200px per farla andare a capo in sicurezza
        titleLabel.setMaxWidth(200);
        // Opzionale ma consigliato per i titoli lunghi:
        titleLabel.setWrapText(true);

        this.getChildren().add(titleLabel);

        if (!isUnlocked) {
            this.setOpacity(0.5);
        } else {
            this.getStyleClass().add("level-card-unlocked");
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