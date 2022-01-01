package kth.jjve.xfran.models;

import java.time.LocalDate;

/**
 * Result constructor
 */


public class Result {

    private Workout workout;
    private String resultType;
    private double time;
    private double reps;
    private double weight;
    private Integer feelScore;
    private String comments;
    private LocalDate date;

    public Result(Workout workout, LocalDate date){
        this.workout = workout;
        this.date = date;
    }

    public Result(){
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public double getReps() {
        return reps;
    }

    public void setReps(double reps) {
        this.reps = reps;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Integer getFeelScore() {
        return feelScore;
    }

    public void setFeelScore(Integer feelScore) {
        this.feelScore = feelScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
