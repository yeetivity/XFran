package kth.jjve.xfran.repositories;

import android.content.Context;
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
import kth.jjve.xfran.TrainingDiaryActivity;
import kth.jjve.xfran.models.Workout;

/**
 * Singleton pattern
 */
public class WorkoutRepository {

    // Build your method and classes to access database chage or webservice
    // right now it is hardcoded

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
        InputStream workoutData  = AppCtx.getContext().getResources().openRawResource(R.raw.workouts_data);
        JsonReader reader = new JsonReader(new InputStreamReader(workoutData, "UTF-8"));
        try {
            parseWorkouts(reader);
        } finally {
            reader.close();
        }
    }

    public void parseWorkouts(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            dataSet.add(readWorkout(reader));
        }
        reader.endArray();
    }

    public Workout readWorkout(JsonReader reader) throws IOException {
        long id = -1;
        String title = null;
        String type = null;
        ArrayList<String> exercises = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                title = reader.nextString();
                Log.i(LOG_TAG, "title: "+title);
            } else if (name.equals("description")) {
                type = reader.nextString();
                Log.i(LOG_TAG, "type: "+type);
            } else if (name.equals("excercises")) {
                exercises = readExercises(reader);
                Log.i(LOG_TAG, "exercises: "+exercises);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Workout workout = new Workout(title);
        workout.setExercises(exercises);
        workout.setType(type);
        Log.i(LOG_TAG, "workout: "+workout);
        return workout;
    }

    public ArrayList<String> readExercises(JsonReader reader) throws IOException {
        ArrayList<String> exercises = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            exercises.add(reader.nextString());
        }
        reader.endArray();
        return exercises;
    }

}
