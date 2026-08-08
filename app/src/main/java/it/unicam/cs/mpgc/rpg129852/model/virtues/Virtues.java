package it.unicam.cs.mpgc.rpg129852.model.virtues;

public record Virtues(
        int faith,
        int hope,
        int love
) {

    public int getTotalPoints(){
        return this.faith + this.hope + this.love;
    }

    public boolean isGreaterThanOrEqualTo(Virtues other) {
            return this.faith >= other.faith && this.hope >= other.hope && this.love >= other.love;
    }

    public boolean isLessThanOrEqualTo(Virtues other) {
        return this.faith <= other.faith && this.hope <= other.hope && this.love <= other.love;
    }

    @Override
    public String toString() {
        return "Fede: " + faith + ", Speranza: " + hope + ", Carità: " + love;
    }

    public boolean isZero() {
        return faith == 0 && hope == 0 && love == 0;
    }
}
