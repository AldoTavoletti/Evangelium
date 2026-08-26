package it.unicam.cs.mpgc.rpg129852.model.level;

/**
 * Represents the overarching category or thematic focus of a level.
 * These categories align with the primary virtues tracked in the game.
 */
public enum LevelCategory {

    FAITH("Fede"),

    HOPE("Speranza"),

    LOVE("Carità");

    private final String displayName;

    LevelCategory(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the human-readable display name of the category.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return this.displayName;
    }
}