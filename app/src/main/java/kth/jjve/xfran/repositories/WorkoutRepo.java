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
public class WorkoutRepo {

    private static WorkoutRepo instance;
    private ArrayList<Workout> dataSet = new ArrayList<>();

    public static WorkoutRepo getInstance() {
        if (instance == null) {
            instance = new WorkoutRepo();
        }
        return instance;
    }

    // Pretend to get data from a webservice or cage or online source
    public MutableLiveData<List<Workout>> getWorkouts() throws IOException {
        setWorkouts(); //Todo: save to firebase to allow custom workouts?
        MutableLiveData<List<Workout>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setWorkouts() throws IOException {
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
        String tag = null;
        String type = null;
        String description = null;
        ArrayList<String> details = null;
        // read workout info
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "title":
                    title = reader.nextString();
                    break;
                case "tag":
                    tag = reader.nextString();
                    break;
                case "type":
                    type = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                case "details":
                    // details is an array therefore requires a reader
                    details = readDetails(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        // create workout object from info read
        Workout workout = new Workout(title);
        workout.setDetails(details);
        workout.setDescription(description);
        workout.setType(type);
        workout.setTag(tag);
        return workout;
    }

    public ArrayList<String> readDetails(JsonReader reader) throws IOException {
        // details for each workout obj are stored in an ArrayList
        ArrayList<String> details = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            details.add(reader.nextString());
        }
        reader.endArray();
        return details;
    }

}
