package it.unicam.cs.mpgc.rpg129852.dto.level;

import it.unicam.cs.mpgc.rpg129852.dto.Resource;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

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

