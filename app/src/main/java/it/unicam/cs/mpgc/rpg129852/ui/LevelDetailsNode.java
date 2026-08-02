package it.unicam.cs.mpgc.rpg129852.ui;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class LevelDetailsNode extends VBox {

    public LevelDetailsNode(LevelMetadata level, Optional<Virtues> bestAttempt) {
        this.setSpacing(10);

        Label title = new Label(level.title());
        title.getStyleClass().add("level-detail-title");

        Label description = new Label(level.description());
        description.setWrapText(true);
        description.getStyleClass().add("level-detail-description");

        Label rewards = new Label("Massimo ottenibile:\n" + formatRewardsText(level.maxRewards()));
        rewards.getStyleClass().add("level-detail-rewards");

        Label bestAttemptLabel = new Label("Miglior tentativo:\n" + formatRewardsText(bestAttempt.get()));
        bestAttemptLabel.getStyleClass().add("level-detail-rewards");

        this.getChildren().addAll(title, description, rewards, bestAttemptLabel);
    }

    private String formatRewardsText(Virtues rewards) {
        return String.format("%d Fede | %d Speranza | %d Amore",
                rewards.faith(),
                rewards.hope(),
                rewards.love());
    }
}