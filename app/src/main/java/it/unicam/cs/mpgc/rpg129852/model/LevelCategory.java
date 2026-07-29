package it.unicam.cs.mpgc.rpg129852.model;

import com.google.gson.annotations.SerializedName;

public enum LevelCategory {

    @SerializedName("spiritual_guidance")
    SPIRITUAL_GUIDANCE(0, "Direzione Spirituale"),

    @SerializedName("mercy")
    MERCY(50, "Livelli della Misericordia"),

    @SerializedName("theological_debate")
    THEOLOGICAL_DEBATE(120, "Dibattiti Teologici");

    private final int requiredVirtues;
    private final String displayName;

    LevelCategory(int requiredVirtues, String displayName) {
        this.requiredVirtues = requiredVirtues;
        this.displayName = displayName;
    }

    public boolean isUnlocked(int totalVirtues) {
        return totalVirtues >= this.requiredVirtues;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}