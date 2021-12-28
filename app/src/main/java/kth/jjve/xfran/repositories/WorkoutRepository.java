package kth.jjve.xfran.repositories;

import android.util.JsonReader;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.AppCtx;
import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Workout;

/**
 * Singleton pattern
 */
public class WorkoutRepository {

    private static final String LOG_TAG = WorkoutRepository.class.getSimpleName();;
    private static WorkoutRepository instance;
    private ArrayList<Workout> dataSet = new ArrayList<>();

    public static WorkoutRepository getInstance(){
        if(instance == null){
            instance = new WorkoutRepository();
        }
        return instance;
    }

    // Pretend to get data from a webservice or cage or online source
    public MutableLiveData<List<Workout>> getWorkouts() throws IOException {
        setWorkouts(); //Todo: normally retrieve data from webservice or anything here
        MutableLiveData<List<Workout>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setWorkouts() throws IOException {
        // This mimics what the database would normally do
        // For now it reads a json file with workout info
        //open json file
        InputStream workoutData  = AppCtx.getContext().getResources().openRawResource(R.raw.workouts_data);
        //initialize reader
        JsonReader reader = new JsonReader(new InputStreamReader(workoutData, "UTF-8"));
        try {
            parseWorkouts(reader);
        } finally {
            reader.close();
        }
    }

    public void parseWorkouts(JsonReader reader) throws IOException {
        //read json array, loop through workouts on dataSet and read each of them
        reader.beginArray();
        while (reader.hasNext()) {
            dataSet.add(readWorkout(reader));
        }
        reader.endArray();
    }

    public Workout readWorkout(JsonReader reader) throws IOException {
        //initialize workout parameters
        String title = null;
        String type = null;
        ArrayList<String> exercises = null;
        //read workout info
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                title = reader.nextString();
            } else if (name.equals("description")) {
                type = reader.nextString();
            } else if (name.equals("excercises")) {
                //exercise is an array therefore requires a reader
                exercises = readExercises(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        //create workout object from info read
        Workout workout = new Workout(title);
        workout.setExercises(exercises);
        workout.setType(type);
        //Log.i(LOG_TAG, "workout: "+workout);
        return workout;
    }

    public ArrayList<String> readExercises(JsonReader reader) throws IOException {
        //exercises for each workout obj are stored in an ArrayList
        ArrayList<String> exercises = new ArrayList<String>();
        //loop through exercises and read each of them
        reader.beginArray();
        while (reader.hasNext()) {
            exercises.add(reader.nextString());
        }
        reader.endArray();
        return exercises;
    }

}
