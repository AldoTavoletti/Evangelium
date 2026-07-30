package it.unicam.cs.mpgc.rpg129852.model;

public class DiscipleData {

    private String name;
    private String job;
    private String color;

    private int faith;
    private int hope;
    private int love;

    // required by Gson
    public DiscipleData() {}

    public DiscipleData(String name, String job, String color) {
        this.name = name;
        this.job = job;
        this.color = color;
    }

    public int getTotalVirtues() {
        return faith+hope+love;
    }

    public int getFaith() {
        return faith;
    }

    public int getHope() {
        return hope;
    }

    public int getLove() {
        return love;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getColor() {
        return color;
    }

    public void setFaith(int faith) {
        this.faith = faith;
    }

    public void setHope(int hope) {
        this.hope = hope;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
