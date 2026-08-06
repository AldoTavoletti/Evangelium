package it.unicam.cs.mpgc.rpg129852.model.level;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;

public enum LevelCategory {

    @SerializedName("faith")
    FAITH("Fede"),

    @SerializedName("hope")
    HOPE("Speranza"),

    @SerializedName("love")
    LOVE("Carità");

    private final String displayName;

    LevelCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}