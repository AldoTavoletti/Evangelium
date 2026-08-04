package it.unicam.cs.mpgc.rpg129852.model;

public record Virtues(
        int faith,
        int hope,
        int love
) {

    public int getTotalPoints(){
        return this.faith + this.hope + this.love;
    }

    public int compareTo(Virtues other) {
        if (this.getTotalPoints() > other.getTotalPoints())
            return 1;

        if (this.getTotalPoints() < other.getTotalPoints())
            return -1;

        return 0;
    }

    @Override
    public String toString() {
        return "Fede: " + faith + ", Speranza: " + hope + ", Carità: " + love;
    }
}
