package it.unicam.cs.mpgc.rpg129852.ui.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextAlignment;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A custom UI component representing a selectable level card.
 * Displays the level's title and an SVG icon indicating the completion state.
 */
public class LevelCardNode extends StackPane {

    private static final double PREF_WIDTH = 240.0;
    private static final double PREF_HEIGHT = 110.0;
    private static final double TITLE_MAX_WIDTH = 200.0;

    private static final String STYLE_CARD = "level-card";
    private static final String STYLE_UNLOCKED = "level-card-unlocked";
    private static final String STYLE_SELECTED = "level-card-selected";
    private static final String STYLE_TITLE = "level-card-title";
    private static final String STYLE_CHECK_FAILED = "check-failed";
    private static final String STYLE_CHECK_PARTIAL = "check-partial";
    private static final String STYLE_CHECK_PERFECT = "check-perfect";

    private static final String SVG_SAD = "M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm-2.715 5.933a.5.5 0 0 1-.183-.683A4.498 4.498 0 0 1 8 9.5a4.5 4.5 0 0 1 3.898 2.25.5.5 0 0 1-.866.5A3.498 3.498 0 0 0 8 10.5a3.498 3.498 0 0 0-3.032 1.75.5.5 0 0 1-.683.183zM10 8c-.552 0-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5S10.552 8 10 8z";
    private static final String SVG_NEUTRAL = "M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm-3 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zM10 8c-.552 0-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5S10.552 8 10 8z";
    private static final String SVG_HAPPY = "M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zM4.285 9.567a.5.5 0 0 1 .683.183A3.498 3.498 0 0 0 8 11.5a3.498 3.498 0 0 0 3.032-1.75.5.5 0 1 1 .866.5A4.498 4.498 0 0 1 8 12.5a4.498 4.498 0 0 1-3.898-2.25.5.5 0 0 1 .183-.683zM10 8c-.552 0-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5S10.552 8 10 8z";

    /**
     * Constructs a new level card.
     *
     * @param level          the metadata of the level
     * @param state          the completion state of the level (affects the status icon)
     * @param onSelectAction the callback triggered when the card is clicked
     */
    public LevelCardNode(LevelMetadata level, LevelCompletionState state, Consumer<LevelCardNode> onSelectAction) {
        initializeBaseStyle();

        VBox textContainer = createTextContainer(level.title());
        this.getChildren().add(textContainer);

        createStatusIcon(state).ifPresent(icon -> {
            StackPane.setAlignment(icon, Pos.BOTTOM_RIGHT);
            this.getChildren().add(icon);
        });

        this.setOnMouseClicked(event -> onSelectAction.accept(this));
    }

    /**
     * Toggles the visual selection state of the card.
     *
     * @param selected true to visually select the card, false to unselect
     */
    public void setSelected(boolean selected) {
        if (selected) {
            this.getStyleClass().add(STYLE_SELECTED);
        } else {
            this.getStyleClass().remove(STYLE_SELECTED);
        }
    }

    private void initializeBaseStyle() {
        this.getStyleClass().addAll(STYLE_CARD, STYLE_UNLOCKED);
        this.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
    }

    private VBox createTextContainer(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add(STYLE_TITLE);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(TITLE_MAX_WIDTH);
        titleLabel.setWrapText(true);

        VBox container = new VBox(titleLabel);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private Optional<SVGPath> createStatusIcon(LevelCompletionState state) {
        if (state == LevelCompletionState.NONE) {
            return Optional.empty();
        }

        SVGPath icon = new SVGPath();
        icon.setFillRule(FillRule.EVEN_ODD);

        switch (state) {
            case FAILED -> {
                icon.setContent(SVG_SAD);
                icon.getStyleClass().add(STYLE_CHECK_FAILED);
            }
            case GOOD -> {
                icon.setContent(SVG_NEUTRAL);
                icon.getStyleClass().add(STYLE_CHECK_PARTIAL);
            }
            case PERFECT -> {
                icon.setContent(SVG_HAPPY);
                icon.getStyleClass().add(STYLE_CHECK_PERFECT);
            }
        }

        return Optional.of(icon);
    }
}