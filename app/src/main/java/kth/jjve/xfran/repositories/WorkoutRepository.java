package kth.jjve.xfran.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.models.Workout;

/**
 * Singleton pattern
 */
public class WorkoutRepository {
    // Build your method and classes to access database chage or webservice
    // right now it is hardcoded

    private static WorkoutRepository instance;
    private ArrayList<Workout> dataSet = new ArrayList<>();

    public static WorkoutRepository getInstance(){
        if(instance == null){
            instance = new WorkoutRepository();
        }
        return instance;
    }

    // Pretend to get data from a webservice or cage or online source
    public MutableLiveData<List<Workout>> getWorkouts(){
        setWorkouts(); //Todo: normally retrieve data from webservice or anything here
        MutableLiveData<List<Workout>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setWorkouts(){
        // This mimics what the database would normally do
        dataSet.add(new Workout("Fran", "21-12"));
        dataSet.add(new Workout("DT", "10-12"));
        dataSet.add(new Workout("Long jump", "10-12"));
    }
}
