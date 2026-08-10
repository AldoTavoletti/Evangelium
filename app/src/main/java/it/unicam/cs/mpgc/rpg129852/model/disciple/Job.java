package it.unicam.cs.mpgc.rpg129852.model.disciple;

/**
 * Represents the chosen order or path of the disciple.
 * This selection can influence dialogue availability and potential starting statistics.
 */
public enum Job {
    NONE("None"),
    DOMINICAN("Dominican"),
    FRANCISCAN("Franciscan"),
    CARMELITE("Carmelite");

    private final String displayValue;

    Job(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Retrieves the human-readable display name of the job.
     *
     * @return the display name
     */
    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }
}