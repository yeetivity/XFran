package kth.jjve.xfran.models;

public class Workout {

    private String title;
    private String workoutDate;

    public Workout(String title, String workoutDate){
        this.title = title;
        this.workoutDate = workoutDate;
    }

    public Workout(){
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getWorkoutDate(){
        return workoutDate;
    }

    public void setWorkoutDate(String workoutDate){
        this.workoutDate = workoutDate;
    }
}
