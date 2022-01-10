package kth.jjve.xfran.utils;
/*
Utilities for workout object
 */

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

import kth.jjve.xfran.models.Workout;

public class WorkoutUtils {

    //string builder for exercises from workout obj
    public static String buildExerciseString(Workout workout) {
        StringBuilder exercises = new StringBuilder();
        ArrayList<String> exercisesArray = workout.getDetails();
        for (String s : exercisesArray) {
            exercises.append(s).append("\n");
        }
        return exercises.toString();
    }

    /*------ METHODS FOR PARSING WORKOUT LIST ------*/
    public static void parseWorkouts(JsonReader reader, ArrayList<Workout> dataSet) throws IOException {
        // read json array, loop through workouts on dataSet and read each of them
        reader.beginArray();
        while (reader.hasNext()) {
            dataSet.add(readWorkout(reader));
        }
        reader.endArray();
    }

    public static Workout readWorkout(JsonReader reader) throws IOException {
        // initialize workout parameters
        String title = null;
        String tag = null;
        String type = null;
        String description = null;
        ArrayList<String> details = null;
        ArrayList<String> equipment = null;
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
                    details = readArrayList(reader);
                    break;
                case "equipment":
                    // equipment is an array therefore requires a reader
                    equipment = readArrayList(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        Workout workout = new Workout(title, tag, type, description, details, equipment);
        return workout;
    }

    public static ArrayList<String> readArrayList(JsonReader reader) throws IOException {
        ArrayList<String> stringArrayList = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            stringArrayList.add(reader.nextString());
        }
        reader.endArray();
        return stringArrayList;
    }
}
