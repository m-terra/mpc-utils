package org.mterra.mpc.model;

public class Bpm implements Comparable<Bpm> {

    private final Double bpm;
    private final String projectName;

    public Bpm(Double bpm, String projectName) {
        this.bpm = bpm;
        this.projectName = projectName;
    }

    public Double getBpm() {
        return bpm;
    }

    public String getProjectName() {
        return projectName;
    }

    @Override
    public int compareTo(Bpm bpm) {
        return Double.compare(this.bpm, bpm.bpm);
    }

}
