package kth.jjve.xfran.models;

import java.time.LocalDate;

/**
 * Result constructor
 */


public class Result {

    private Workout workout;
    private String scoreType;
    private double score;
    private Integer feelScore;
    private String comments;
    private LocalDate date;
    private boolean empty;

    public Result(Workout workout, LocalDate date){
        this.workout = workout;
        this.date = date;
        this.empty = false;
    }

    public Result(){
    }

    public Workout getWorkout() {
        return workout;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public boolean checkEmpty(){return empty;}
}
