package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.Resource;
import java.util.List;
import java.util.Optional;

public record LevelMetadata(
        String id,
        LevelCategory category,
        String title,
        String description,
        Virtues maxRewards,
        List<String> requiredBookIds,
        String scenarioPath
) implements Resource {

    public LevelCompletionState evaluateAttempt(Optional<Virtues> bestAttempt) {
        if (bestAttempt.isEmpty()) {
            return LevelCompletionState.NONE;
        }

        Virtues scoreObtained = bestAttempt.get();

        if (scoreObtained.faith() == 0 && scoreObtained.hope() == 0 && scoreObtained.love() == 0) {
            return LevelCompletionState.FAILED;
        }

        if (scoreObtained.equals(this.maxRewards())) {
            return LevelCompletionState.PERFECT;
        }

        return LevelCompletionState.PARTIAL;
    }

}

