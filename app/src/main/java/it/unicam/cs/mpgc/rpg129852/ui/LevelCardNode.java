package it.unicam.cs.mpgc.rpg129852.ui;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;

public class LevelCardNode extends StackPane {

    public enum CompletionState {
        NONE, FAILED, PARTIAL, PERFECT
    }

    public LevelCardNode(LevelMetadata level, boolean isUnlocked, CompletionState state, Consumer<LevelCardNode> onSelectAction) {
        this.getStyleClass().add("level-card");
        this.setPrefSize(240, 110);

        VBox textContainer = new VBox();
        textContainer.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(level.title());
        titleLabel.getStyleClass().add("level-card-title");
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(200);
        titleLabel.setWrapText(true);

        textContainer.getChildren().add(titleLabel);
        this.getChildren().add(textContainer);

        if (state != CompletionState.NONE) {
            SVGPath statusIcon = new SVGPath();

            // FONDAMENTALE: Forza JavaFX a ritagliare correttamente gli occhi e la bocca (rendendoli trasparenti)
            statusIcon.setFillRule(javafx.scene.shape.FillRule.EVEN_ODD);

            if (state == CompletionState.FAILED) {
                // Faccia Triste Piena
                statusIcon.setContent("M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm-2.715 5.933a.5.5 0 0 1-.183-.683A4.498 4.498 0 0 1 8 9.5a4.5 4.5 0 0 1 3.898 2.25.5.5 0 0 1-.866.5A3.498 3.498 0 0 0 8 10.5a3.498 3.498 0 0 0-3.032 1.75.5.5 0 0 1-.683.183zM10 8c-.552 0-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5S10.552 8 10 8z");
                statusIcon.getStyleClass().add("check-failed");
            } else if (state == CompletionState.PARTIAL) {
                // Faccia Neutra Piena
                statusIcon.setContent("M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm-3 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zM10 8c-.552 0-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5S10.552 8 10 8z");
                statusIcon.getStyleClass().add("check-partial");
            } else if (state == CompletionState.PERFECT) {
                // Faccia Sorridente Piena
                statusIcon.setContent("M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zM4.285 9.567a.5.5 0 0 1 .683.183A3.498 3.498 0 0 0 8 11.5a3.498 3.498 0 0 0 3.032-1.75.5.5 0 1 1 .866.5A4.498 4.498 0 0 1 8 12.5a4.498 4.498 0 0 1-3.898-2.25.5.5 0 0 1 .183-.683zM10 8c-.552 0-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5S10.552 8 10 8z");
                statusIcon.getStyleClass().add("check-perfect");
            }

            // Allineamento
            StackPane.setAlignment(statusIcon, Pos.BOTTOM_RIGHT);

            this.getChildren().add(statusIcon);
        }

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