package it.unicam.cs.mpgc.rpg129852.model.disciple;

import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

public class DiscipleData {

    private String name;
    private Job job;
    private String color;

    private Virtues virtues;

    // required by Gson
    public DiscipleData() {
    }

    public DiscipleData(String name, Job job, String color) {
        this.name = name;
        this.job = job;
        this.color = color;
        this.virtues = getStartingVirtues(job);
    }

    public void addVirtues(Virtues virtuesToAdd) {
        virtues = new Virtues(virtues.faith() + virtuesToAdd.faith(), virtues.hope() + virtuesToAdd.hope(), virtues.love() + virtuesToAdd.love());
    }

    public void subtractVirtues(Virtues virtuesToSubtract) {
        virtues = new Virtues(virtues.faith() - virtuesToSubtract.faith(), virtues.hope() - virtuesToSubtract.hope(), virtues.love() - virtuesToSubtract.love());
    }

    public int getTotalVirtues() {

        return virtues.getTotalPoints();
    }

    public Virtues getVirtues() {
        return virtues;
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

    public void setVirtues(Virtues virtues) {
        this.virtues = virtues;
    }

    private Virtues getStartingVirtues(Job job) {
        return new Virtues(0, 0, 0);
    }

}
