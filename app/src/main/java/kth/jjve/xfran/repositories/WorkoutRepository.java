package kth.jjve.xfran.repositories;

import android.util.JsonReader;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.adapters.AppCtx;
import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Workout;

/**
 * Singleton pattern
 */
public class WorkoutRepository {

    private static WorkoutRepository instance;
    private ArrayList<Workout> dataSet = new ArrayList<>();

    public static WorkoutRepository getInstance() {
        if (instance == null) {
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
        // open json file
        InputStream workoutData = AppCtx.getContext().getResources().openRawResource(R.raw.workouts_data);
        // initialize reader
        JsonReader reader = new JsonReader(new InputStreamReader(workoutData, StandardCharsets.UTF_8));
        try {
            parseWorkouts(reader);
        } finally {
            reader.close();
        }
    }

    public void parseWorkouts(JsonReader reader) throws IOException {
        // read json array, loop through workouts on dataSet and read each of them
        reader.beginArray();
        while (reader.hasNext()) {
            dataSet.add(readWorkout(reader));
        }
        reader.endArray();
    }

    public Workout readWorkout(JsonReader reader) throws IOException {
        // initialize workout parameters
        String title = null;
        String type = null;
        ArrayList<String> exercises = null;
        // read workout info
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "name":
                    title = reader.nextString();
                    break;
                case "description":
                    type = reader.nextString();
                    break;
                case "excercises":
                    // exercise is an array therefore requires a reader
                    // Todo: change the spelling to correct spelling
                    exercises = readExercises(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        // create workout object from info read
        Workout workout = new Workout(title);
        workout.setExercises(exercises);
        workout.setType(type);
        return workout;
    }

    public ArrayList<String> readExercises(JsonReader reader) throws IOException {
        // exercises for each workout obj are stored in an ArrayList
        ArrayList<String> exercises = new ArrayList<>();
        // loop through exercises and read each of them
        reader.beginArray();
        while (reader.hasNext()) {
            exercises.add(reader.nextString());
        }
        reader.endArray();
        return exercises;
    }

}
