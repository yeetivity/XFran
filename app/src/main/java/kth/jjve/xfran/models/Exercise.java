package kth.jjve.xfran.models;

import java.util.ArrayList;

/**
 * Exercise constructor
 */

public class Exercise {

    private String name;
    private int reps;
    private double weightMale;
    private double weightFemale;
    private double bwMultiplier;
    private double barbellMultiplier;


    public Exercise(String name, int reps, double weightMale, double weightFemale){
        this.name = name;
        this.reps = reps;
        this.weightMale = weightMale;
        this.weightFemale = weightFemale;
    }

    public Exercise(){
    }

    public void setHeightAndWeight(){
        //TODO define the multipliers for power calculation
        switch (name) {
            case "thruster":
                bwMultiplier = 1.1;
                barbellMultiplier = 1;
            default:
                bwMultiplier = 1;
                barbellMultiplier = 1;
        }
    }

}
