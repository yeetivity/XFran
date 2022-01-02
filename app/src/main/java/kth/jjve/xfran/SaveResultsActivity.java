package kth.jjve.xfran;
/*
Activity to let the user save a result
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kth.jjve.xfran.models.Workout;

import static android.view.View.GONE;

public class SaveResultsActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    /*_________ INTENT _________*/
    private Integer position;
    private Workout mWorkout;

    /*------ HOOKS ------*/
    private TextView mName, mDescription, mExercises;
    private Button workoutItemSaveButton, workoutItemPlanButton, saveButton, cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_result_save, contentFrameLayout);

        navigationView.setCheckedItem(R.id.nav_workouts);

        /*------ HOOKS ------*/
        mName = findViewById(R.id.workout_name);
        mDescription = findViewById(R.id.workout_description);
        mExercises = findViewById(R.id.workout_exercises);

        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_save_button);
        workoutItemSaveButton = findViewById(R.id.button_save_wod);
        workoutItemPlanButton = findViewById(R.id.button_plan_wod);

        /*------ LISTENERS ------*/
        cancelButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveResult());

        /*------ INTENT ------*/
        Intent intent = getIntent();
        position = intent.getIntExtra(WorkoutsListActivity.WORKOUT_ID,1);
        mWorkout = (Workout) intent.getSerializableExtra(WorkoutsListActivity.WORKOUT_OBJ);
        Log.i(LOG_TAG, "workout read: "+mWorkout);

        // Build a string with exercises
        StringBuilder exercises = new StringBuilder();
        ArrayList<String> exercisesArray = mWorkout.getExercises();
        for (String s : exercisesArray){
            exercises.append(s).append("\n");
        }

        // Fill the UI with workout info
        mName.setText(mWorkout.getTitle());
        mDescription.setText(mWorkout.getType());
        mExercises.setText(exercises.toString());

        workoutItemSaveButton.setVisibility(GONE);
        workoutItemPlanButton.setVisibility(GONE);

        //TODO finish this activity --> work in progress

    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    public void saveResult(){
        // TODO save result object --> work in progress
        Toast.makeText(this, "Save workout in development", Toast.LENGTH_SHORT).show();
    }

}
