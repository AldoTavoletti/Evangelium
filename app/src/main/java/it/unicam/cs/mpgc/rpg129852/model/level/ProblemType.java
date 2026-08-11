package it.unicam.cs.mpgc.rpg129852.model.level;

/**
 * Defines the psychological or spiritual challenges an NPC might face during a level.
 */
public enum ProblemType {
    DOUBT("Dubbio"),
    SUFFERING ("Sofferenza");

    private final String displayValue;

    ProblemType(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Retrieves the human-readable display name of the problem.
     *
     * @return the display name
     */
    public String getDisplayValue() {
        return displayValue;
    }
}