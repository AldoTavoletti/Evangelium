package it.unicam.cs.mpgc.rpg129852.model.level;

import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

public record Score(Virtues virtues, LevelCompletionState completionState) {

    public boolean isLessThanOrEqualTo(Score other) {
        return this.virtues.isLessThanOrEqualTo(other.virtues);
    }
}
