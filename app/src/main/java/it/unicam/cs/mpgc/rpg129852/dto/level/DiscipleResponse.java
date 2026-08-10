package it.unicam.cs.mpgc.rpg129852.dto.level;

import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

public record DiscipleResponse(
        String displayReference,
        String scriptureId,
        PhaseAnswer answerValue,
        Job requiredJob
) {
}
