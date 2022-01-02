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
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

import androidx.lifecycle.ViewModelProviders;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.viewmodels.ResultVM;

import static android.view.View.GONE;

public class SaveResultsActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private LocalDate todayLocalDate;
    private Result mResult;
    private ResultVM mResultVM;

    /*_________ INTENT _________*/
    private Integer position;
    private Workout workout;

    /*------ HOOKS ------*/
    private TextView mName, mDescription, mExercises;
    private EditText mDate, mScore, mComments;
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
        mScore = findViewById(R.id.edit_score);
        mComments = findViewById(R.id.edit_comments);

        /*-----  VM  -----*/
        mResultVM = ViewModelProviders.of(this).get(ResultVM.class);
        mResultVM.init();
        //mResultVM.getResults().observe(this, this::setViews);

        /*------ LISTENERS ------*/
        cancelButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveResult());

        /*------ INTENT ------*/
        Intent intent = getIntent();
        position = intent.getIntExtra(WorkoutsListActivity.WORKOUT_ID,1);
        workout = (Workout) intent.getSerializableExtra(WorkoutsListActivity.WORKOUT_OBJ);
        Log.i(LOG_TAG, "workout read: "+ workout);

        // Build a string with exercises
        StringBuilder exercises = new StringBuilder();
        ArrayList<String> exercisesArray = workout.getDetails();
        for (String s : exercisesArray){
            exercises.append(s).append("\n");
        }

        // Fill the UI with workout info
        mName.setText(workout.getTitle());
        mDescription.setText(workout.getDescription());
        mExercises.setText(exercises.toString());
        workoutItemSaveButton.setVisibility(GONE);
        workoutItemPlanButton.setVisibility(GONE);

        // Default workout date set to current date
        todayLocalDate = LocalDate.now();
        mDate.setText(todayLocalDate.toString());

        //TODO finish this activity --> work in progress

    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    public void saveResult(){
        LocalDate date = LocalDate.parse(mDate.getText());
        Log.i(LOG_TAG, "date is: " + date);
        Log.i(LOG_TAG, "workout is: " + workout);

        if (TextUtils.isEmpty(date.toString())){
            mDate.setError("Date is required");
            return;
        }

        try{
            mResultVM.addNewResult(workout, date);
            Toast.makeText(getApplicationContext(), "Result saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Fill info to save workout", Toast.LENGTH_SHORT).show();
        }
    }
}
