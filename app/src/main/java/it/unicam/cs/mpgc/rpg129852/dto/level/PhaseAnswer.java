package it.unicam.cs.mpgc.rpg129852.dto.level;

public enum PhaseAnswer {
    PERFECT(0.5),
    GOOD(0.3),
    BAD(0);

    private double healValue;

    PhaseAnswer(double healValue) {
        this.healValue = healValue;
    }

    public double getHealValue() {
        return healValue;
    }
}