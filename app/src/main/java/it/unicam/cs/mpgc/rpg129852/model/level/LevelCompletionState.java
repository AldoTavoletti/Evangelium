package it.unicam.cs.mpgc.rpg129852.model.level;

import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.Optional;

public enum LevelCompletionState {
    NONE,
    FAILED,
    PARTIAL,
    PERFECT;

    public static LevelCompletionState evaluate(Virtues maxRewards, Optional<Virtues> bestAttempt) {
        if (bestAttempt.isEmpty()) {
            return LevelCompletionState.NONE;
        }

        Virtues scoreObtained = bestAttempt.get();

        if (scoreObtained.faith() == 0 && scoreObtained.hope() == 0 && scoreObtained.love() == 0) {
            return LevelCompletionState.FAILED;
        }

        if (scoreObtained.equals(maxRewards)) {
            return LevelCompletionState.PERFECT;
        }

        return LevelCompletionState.PARTIAL;
    }

}