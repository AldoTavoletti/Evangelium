package it.unicam.cs.mpgc.rpg129852.ui;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.VirtueRewards;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LevelDetailsNode extends VBox {

    public LevelDetailsNode(LevelMetadata level) {
        this.setSpacing(10);

        Label title = new Label(level.title());
        title.getStyleClass().add("level-detail-title");

        Label description = new Label(level.description());
        description.setWrapText(true);
        description.getStyleClass().add("level-detail-description");

        Label rewards = new Label(formatRewardsText(level.maxRewards()));
        rewards.getStyleClass().add("level-detail-rewards");

        this.getChildren().addAll(title, description, rewards);
    }

    private String formatRewardsText(VirtueRewards rewards) {
        return String.format("Max Ottenibile: %d Fede | %d Speranza | %d Amore",
                rewards.faith(),
                rewards.hope(),
                rewards.love());
    }
}