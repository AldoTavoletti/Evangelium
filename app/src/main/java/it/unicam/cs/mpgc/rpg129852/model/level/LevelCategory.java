package it.unicam.cs.mpgc.rpg129852.model.level;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;

public enum LevelCategory {

    @SerializedName("spiritual_guidance")
    SPIRITUAL_GUIDANCE("Direzione Spirituale"),

    @SerializedName("mercy")
    MERCY("Livelli della Misericordia"),

    @SerializedName("theological_debate")
    THEOLOGICAL_DEBATE("Dibattiti Teologici");

    private final String displayName;

    LevelCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Optional<LevelCategory> getPrevious() {
        int ordinal = this.ordinal();
        if (ordinal == 0) {
            return Optional.empty(); // La prima categoria non ha precedenti
        }
        return Optional.of(LevelCategory.values()[ordinal - 1]);
    }
}