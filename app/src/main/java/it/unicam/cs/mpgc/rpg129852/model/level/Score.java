package it.unicam.cs.mpgc.rpg129852.model.level;

import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import java.util.Objects;

/**
 * Represents the final outcome of a completed level.
 * It bundles the numerical virtues earned with the qualitative state of completion.
 *
 * @param virtues         the virtues earned by the player in the level
 * @param completionState the qualitative result of the level attempt
 */
public record Score(Virtues virtues, LevelCompletionState completionState) {

    /**
     * Compact constructor to ensure the record is never instantiated with null values.
     */
    public Score {
        Objects.requireNonNull(virtues, "The virtues must not be null.");
        Objects.requireNonNull(completionState, "The completion state must not be null.");
    }

    /**
     * Compares this score's virtues against another score's virtues.
     *
     * @param other the score to compare against
     * @return true if this score's virtues are less than or equal to the other's, false otherwise
     * @throws NullPointerException if the provided score is null
     */
    public boolean isLessThanOrEqualTo(Score other) {
        Objects.requireNonNull(other, "The score to compare against must not be null.");
        return this.virtues.isLessThanOrEqualTo(other.virtues());
    }
}