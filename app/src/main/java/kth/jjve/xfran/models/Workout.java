package kth.jjve.xfran.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Workout constructor
 */

public class Workout implements Serializable {

    private String title;
    private String type;
    private String timeCap;
    private int rounds;
    private ArrayList<String> exercises; //TODO substitute for exercises object

    public Workout(String title){
        this.title = title;
    }

    public Workout(){
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeCap() {
        return timeCap;
    }

    public void setTimeCap(String timeCap) {
        this.timeCap = timeCap;
    }

    public ArrayList<String> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<String> exercises) {
        this.exercises = exercises;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

}
