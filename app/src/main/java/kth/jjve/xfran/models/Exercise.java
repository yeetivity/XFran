package kth.jjve.xfran.models;

import java.util.ArrayList;

/**
 * Exercise constructor
 */

//TODO this is not yet used, will be used for power output calculations.
// The workout item will contain a exercise object instead of a string

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

    public void setROM(){
        //TODO define the multipliers for power calculation.
        /*
            This is used to calculate mechanical work of each movement.
            Winter table of sizes and masses is used as an approximation to calculate the distance
            that the body segments and the extra weight were moved.
            Work is calculated as the work performed against gravity.
         */
        switch (name) {
            case "thruster":
                bwMultiplier = 1;
                barbellMultiplier = 1;
            case "pull-up":
                bwMultiplier = 1;
                barbellMultiplier = 1;
            default:
                bwMultiplier = 1;
                barbellMultiplier = 1;
        }
    }

}
