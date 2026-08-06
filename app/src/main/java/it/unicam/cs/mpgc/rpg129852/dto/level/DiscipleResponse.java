package it.unicam.cs.mpgc.rpg129852.dto.level;

import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

public record DiscipleResponse(
   String displayReference,
   String scriptureId,
   PhaseAnswer answerValue,
   Job requiredJob
) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DiscipleResponse other)) {
            return false;
        }

        return java.util.Objects.equals(this.scriptureId, other.scriptureId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(scriptureId);
    }
}
