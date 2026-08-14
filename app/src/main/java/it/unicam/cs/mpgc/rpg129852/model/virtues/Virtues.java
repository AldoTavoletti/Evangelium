package it.unicam.cs.mpgc.rpg129852.model.virtues;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents the core numerical values (currency or stats) in the game.
 * This record is immutable and provides utility methods for mathematical comparisons,
 * typically used to check affordability or level rewards.
 *
 * @param faith the amount of faith points
 * @param hope  the amount of hope points
 * @param love  the amount of love points
 */
public record Virtues(
        int faith,
        int hope,
        int love
) {

    /**
     * Checks if every virtue in this instance is greater than or equal to the corresponding
     * virtue in the specified instance. Useful for checking if a player can afford an item.
     *
     * @param other the virtues to compare against
     * @return true if this instance has equal or greater points for all virtues, false otherwise
     * @throws NullPointerException if the provided virtues object is null
     */
    public boolean isGreaterThanOrEqualTo(Virtues other) {
        Objects.requireNonNull(other, "The virtues to compare against must not be null.");
        return this.faith >= other.faith && this.hope >= other.hope && this.love >= other.love;
    }

    /**
     * Checks if every virtue in this instance is less than or equal to the corresponding
     * virtue in the specified instance.
     *
     * @param other the virtues to compare against
     * @return true if this instance has equal or fewer points for all virtues, false otherwise
     * @throws NullPointerException if the provided virtues object is null
     */
    public boolean isLessThanOrEqualTo(Virtues other) {
        Objects.requireNonNull(other, "The virtues to compare against must not be null.");
        return this.faith <= other.faith && this.hope <= other.hope && this.love <= other.love;
    }

    /**
     * Checks if all the virtue points are exactly zero.
     *
     * @return true if faith, hope, and love are all zero, false otherwise
     */
    public boolean isZero() {
        return faith == 0 && hope == 0 && love == 0;
    }

    @Override
    public @NonNull String toString() {
        return "Fede: " + faith + ", Speranza: " + hope + ", Carità: " + love;
    }
}