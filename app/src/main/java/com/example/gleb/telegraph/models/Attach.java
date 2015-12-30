package com.example.gleb.telegraph.models;

/**
 * Created by Gleb on 30.12.2015.
 */
public class Attach {
    private String[] nameAttachs;
    private int[] numPositions;

    public Attach(String[] nameAttachs, int[] numPositions) {
        this.nameAttachs = nameAttachs;
        this.numPositions = numPositions;
    }

    public String[] getNameAttachs() {
        return nameAttachs;
    }

    public void setNameAttachs(String[] nameAttachs) {
        this.nameAttachs = nameAttachs;
    }

    public int[] getNumPositions() {
        return numPositions;
    }

    public void setNumPositions(int[] numPositions) {
        this.numPositions = numPositions;
    }
}
