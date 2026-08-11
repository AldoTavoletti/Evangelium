package it.unicam.cs.mpgc.rpg129852.model.disciple;

import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.Objects;

/**
 * Represents the core profile and progression state of the player's character.
 * It tracks their chosen identity, appearance, and the total virtues accumulated over the playthrough.
 */
public class DiscipleData {

    private static final Virtues STARTING_VIRTUES = new Virtues(0, 0, 0);

    private String name;
    private Job job;
    private String color;

    private Virtues virtues;

    /**
     * Default constructor required by Gson for JSON deserialization.
     * Kept protected to prevent accidental instantiation of incomplete disciple profiles.
     */
    protected DiscipleData() {
    }

    /**
     * Constructs a new disciple profile with the specified traits.
     *
     * @param name  the chosen name of the disciple
     * @param job   the chosen job or order of the disciple
     * @param color the chosen theme color representing the disciple
     * @throws IllegalArgumentException if the name or color is null or blank
     * @throws NullPointerException     if the job is null
     */
    public DiscipleData(String name, Job job, String color) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The disciple name must not be null or blank.");
        }
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("The disciple color must not be null or blank.");
        }

        this.name = name;
        this.job = Objects.requireNonNull(job, "The disciple job must not be null.");
        this.color = color;
        this.virtues = getStartingVirtues(job);
    }

    /**
     * Adds the specified virtues to the disciple's current total.
     *
     * @param virtuesToAdd the virtues to add
     * @throws NullPointerException if the virtues to add are null
     */
    public void addVirtues(Virtues virtuesToAdd) {
        Objects.requireNonNull(virtuesToAdd, "The virtues to add must not be null.");

        this.virtues = new Virtues(
                this.virtues.faith() + virtuesToAdd.faith(),
                this.virtues.hope() + virtuesToAdd.hope(),
                this.virtues.love() + virtuesToAdd.love()
        );
    }

    /**
     * Subtracts the specified virtues from the disciple's current total.
     *
     * @param virtuesToSubtract the virtues to subtract
     * @throws NullPointerException if the virtues to subtract are null
     */
    public void subtractVirtues(Virtues virtuesToSubtract) {
        Objects.requireNonNull(virtuesToSubtract, "The virtues to subtract must not be null.");

        this.virtues = new Virtues(
                this.virtues.faith() - virtuesToSubtract.faith(),
                this.virtues.hope() - virtuesToSubtract.hope(),
                this.virtues.love() - virtuesToSubtract.love()
        );
    }

    /**
     * Retrieves the total sum of all virtue points combined.
     *
     * @return the total amount of virtue points
     */
    public int getTotalVirtues() {
        return virtues.getTotalPoints();
    }

    public Virtues getVirtues() {
        return virtues;
    }

    public void setVirtues(Virtues virtues) {
        this.virtues = Objects.requireNonNull(virtues, "The virtues must not be null.");
    }

    public String getName() {
        return name;
    }

    public Job getJob() {
        return job;
    }

    public String getColor() {
        return color;
    }

    private Virtues getStartingVirtues(Job job) {
        return STARTING_VIRTUES;
    }
}