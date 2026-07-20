package it.unicam.cs.mpgc.rpg129852.model;

public class DiscipleData {

    private String name;
    private String job;
    private String discipleColor;
    private int faith;
    private int hope;
    private int charity;

    // required by Gson
    public DiscipleData() {}

    public DiscipleData(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public int getFaith() {
        return faith;
    }

    public int getHope() {
        return hope;
    }

    public int getCharity() {
        return charity;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public void setFaith(int faith) {
        this.faith = faith;
    }

    public void setHope(int hope) {
        this.hope = hope;
    }

    public void setCharity(int charity) {
        this.charity = charity;
    }
}
