package it.unicam.cs.mpgc.rpg129852.ui.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.Optional;

/**
 * A custom UI component that displays detailed information about a selected level.
 * This includes the title, description, required books, maximum achievable rewards,
 * and the player's best historical score (if available).
 */
public class LevelDetailsNode extends VBox {

    private static final double DEFAULT_SPACING = 10.0;

    private static final String STYLE_TITLE = "level-detail-title";
    private static final String STYLE_DESCRIPTION = "level-detail-description";
    private static final String STYLE_INFO_SECTION = "level-detail-rewards";

    /**
     * Constructs a new levelMetadata details view.
     *
     * @param levelMetadata             the metadata of the selected level
     * @param requiredBookNames         a formatted string containing the names of the required books
     * @param bestAttempt               an {@link Optional} containing the player's best score, or empty if never played
     * @throws NullPointerException     if any of the parameters are null
     */
    public LevelDetailsNode(LevelMetadata levelMetadata, String requiredBookNames, Optional<Score> bestAttempt) {
        Objects.requireNonNull(levelMetadata, "Level metadata must not be null.");
        Objects.requireNonNull(requiredBookNames, "Required book names must not be null.");
        Objects.requireNonNull(bestAttempt, "Best attempt Optional must not be null.");

        this.setSpacing(DEFAULT_SPACING);

        this.getChildren().addAll(
                createTitleLabel(levelMetadata.title()),
                createDescriptionLabel(levelMetadata.description()),
                createInfoSectionLabel("Libri necessari:\n" + requiredBookNames),
                createInfoSectionLabel("Massimo ottenibile:\n" + formatRewardsText(levelMetadata.maxRewards()))
        );

        bestAttempt.ifPresent(score -> {
            Label bestAttemptLabel = createInfoSectionLabel("Miglior tentativo:\n" + formatRewardsText(score.virtues()));
            this.getChildren().add(bestAttemptLabel);
        });
    }

    private Label createTitleLabel(String text) {
        Label title = new Label(text);
        title.getStyleClass().add(STYLE_TITLE);
        return title;
    }

    private Label createDescriptionLabel(String text) {
        Label description = new Label(text);
        description.setWrapText(true);
        description.getStyleClass().add(STYLE_DESCRIPTION);
        return description;
    }

    private Label createInfoSectionLabel(String text) {
        Label sectionLabel = new Label(text);
        sectionLabel.getStyleClass().add(STYLE_INFO_SECTION); // Reused for books, rewards, and best attempt
        return sectionLabel;
    }

    private String formatRewardsText(Virtues rewards) {
        return String.format("%d Fede | %d Speranza | %d Carità",
                rewards.faith(),
                rewards.hope(),
                rewards.love());
    }
}