package kth.jjve.xfran;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import kth.jjve.xfran.models.Workout;

import static android.view.View.GONE;

public class SaveResultsActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    /*_________ INTENT _________*/
    private Integer position;
    private Workout mWorkout;

    /*------ VIEWS ------*/
    private TextView mName, mDescription, mExercises;


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

        Button saveButton = findViewById(R.id.save_button);
        Button cancelButton = findViewById(R.id.cancel_save_button);
        Button workoutItemSaveButton = findViewById(R.id.button_save_wod);
        Button workoutItemPlanButton = findViewById(R.id.button_plan_wod);

        /*------ LISTENERS ------*/
        cancelButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> saveResult());

        /*------ INTENT ------*/
        Intent intent = getIntent();
        position = intent.getIntExtra(WorkoutsActivity.WORKOUT_ID,1);
        mWorkout = (Workout) intent.getSerializableExtra(WorkoutsActivity.WORKOUT_OBJ);
        Log.i(LOG_TAG, "workout read: " + mWorkout);

        // Create a string with all the exercises
        StringBuilder exercises = new StringBuilder();
        ArrayList<String> exercisesArray = mWorkout.getExercises();
        for (String s : exercisesArray){
            exercises.append(s).append("\n");
        }

        // Update the UI with the exercises
        mName.setText(mWorkout.getTitle());
        mDescription.setText(mWorkout.getType());
        mExercises.setText(exercises.toString());

        // hide the buttons from workout_item view
        workoutItemSaveButton.setVisibility(GONE);
        workoutItemPlanButton.setVisibility(GONE);

        //TODO finish this activity --> requires result object to create result fields

    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }


    public void saveResult(){
        // TODO save result object --> requires result object to save result
        Toast.makeText(this, "Save workout in development", Toast.LENGTH_SHORT).show();
    }
}
