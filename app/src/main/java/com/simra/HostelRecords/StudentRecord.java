package com.simra.HostelRecords;

import java.io.Serializable;

/**
 * Created by Simra Afreen on 03-10-2017.
 */

public class StudentRecord implements Serializable {

    private long id;
    private String name;
    private int tot_attendance;
    private int leaves;
    private  int absents;
    private int fine;

    public StudentRecord(long id, String name, int tot_attendance, int leaves, int absents, int fine) {
        this.id = id;
        this.name = name;
        this.tot_attendance = tot_attendance;
        this.leaves = leaves;
        this.absents = absents;
        this.fine = fine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTot_attendance() {
        return tot_attendance;
    }

    public void setTot_attendance(int tot_attendance) {
        this.tot_attendance = tot_attendance;
    }

    public int getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public int getAbsents() {
        return absents;
    }

    public void setAbsents(int absents) {
        this.absents = absents;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}
