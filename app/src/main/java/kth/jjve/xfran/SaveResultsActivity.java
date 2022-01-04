package kth.jjve.xfran;
/*
Activity to let the user save a result
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import androidx.lifecycle.ViewModelProviders;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.viewmodels.ResultVM;

import static android.view.View.GONE;

public class SaveResultsActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private LocalDate todayLocalDate;
    private ResultVM mResultVM;

    /*_________ INTENT _________*/
    private Integer position;
    private Workout workout;

    /*------ HOOKS ------*/
    private TextView mName, mDescription, mExercises;
    private EditText mDate, mScore, mComments;
    private Switch mScaledSwitch;
    private SeekBar mScoreSeekBar;
    private Button workoutItemSaveButton, workoutItemPlanButton, saveButton, cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_result_save, contentFrameLayout);

        navigationView.setCheckedItem(R.id.nav_workouts);

        /*------ HOOKS ------*/
        //workout_item
        mName = findViewById(R.id.workout_name);
        mDescription = findViewById(R.id.workout_description);
        mExercises = findViewById(R.id.workout_exercises);
        workoutItemSaveButton = findViewById(R.id.button_save_wod);
        workoutItemPlanButton = findViewById(R.id.button_plan_wod);
        //save results
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_save_button);
        mDate = findViewById(R.id.edit_date);
        mScaledSwitch = findViewById(R.id.scaled_switch);
        mScore = findViewById(R.id.edit_score);
        mScoreSeekBar = findViewById(R.id.score_bar);
        mComments = findViewById(R.id.edit_comments);

        /*-----  VM  -----*/
        mResultVM = ViewModelProviders.of(this).get(ResultVM.class);
        mResultVM.init();

        /*------ LISTENERS ------*/
        cancelButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveResult());

        /*------ INTENT ------*/
        Intent intent = getIntent();
        position = intent.getIntExtra(WorkoutsListActivity.WORKOUT_ID, 1);
        workout = (Workout) intent.getSerializableExtra(WorkoutsListActivity.WORKOUT_OBJ);
        Log.i(LOG_TAG, "workout read: " + workout);

        // Build a string with exercises
        StringBuilder exercises = new StringBuilder();
        ArrayList<String> exercisesArray = workout.getDetails();
        for (String s : exercisesArray) {
            exercises.append(s).append("\n");
        }

        // Fill the UI with workout info
        mName.setText(workout.getTitle());
        mDescription.setText(workout.getDescription());
        mExercises.setText(exercises.toString());
        workoutItemSaveButton.setVisibility(GONE);
        workoutItemPlanButton.setVisibility(GONE);

        //Todo: change score_type text according to the current workout

        // Default workout date set to current date
        // Todo: add something to get correct date format
        todayLocalDate = LocalDate.now();
        mDate.setText(todayLocalDate.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    public void saveResult() {
        //get all filled fields
        try {
            LocalDate date = LocalDate.parse(mDate.getText());
            boolean scaled = mScaledSwitch.isChecked();
            String score = String.valueOf(mScore.getText()); // Todo: change this to double --> needs to deal with rounds+reps
            Integer rating = Math.round(mScoreSeekBar.getProgress());
            String comments = mComments.getText().toString();

            //error message if score is not filled
            if (TextUtils.isEmpty(score.toString())) {
                mScore.setError("Score is required");
                return;
            }

            //save results
            mResultVM.addNewResult(workout, score, rating, comments, date, scaled);
            Toast.makeText(getApplicationContext(), "Result saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (DateTimeParseException e) {
            //error message if date format is not respected and parsing fails
            e.printStackTrace();
            mDate.setError("Date format: yyyy-mm-dd");
        }
    }
}
