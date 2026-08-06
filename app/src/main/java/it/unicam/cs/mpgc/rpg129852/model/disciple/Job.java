package it.unicam.cs.mpgc.rpg129852.model.disciple;

public enum Job {
    NONE ("Nessuno"),
    HISTORIAN ("Storico"),
    POET ("Poeta"),
    PROPHET ("Profeta");

    private final String displayValue;

    Job(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }
}