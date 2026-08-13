package it.unicam.cs.mpgc.rpg129852.model.disciple;

/**
 * Represents the chosen order or path of the disciple.
 * This selection can influence dialogue availability.
 */
public enum Job {

    NONE("Nessuno"),

    DOMINICAN("Domenicano"),

    FRANCISCAN("Francescano"),

    CARMELITE("Carmelitano");

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