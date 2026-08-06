package it.unicam.cs.mpgc.rpg129852.model.level;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;

public enum LevelCategory {

    @SerializedName("faith")
    SPIRITUAL_GUIDANCE("Fede"),

    @SerializedName("hope")
    MERCY("Speranza"),

    @SerializedName("love")
    THEOLOGICAL_DEBATE("Carità");

    private final String displayName;

    LevelCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}