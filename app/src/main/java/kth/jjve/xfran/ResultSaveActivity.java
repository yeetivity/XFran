package kth.jjve.xfran;
/*
Activity to let the user save a result
 */

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.utils.ResultUtils;
import kth.jjve.xfran.utils.WorkoutUtils;
import kth.jjve.xfran.viewmodels.ResultVM;

public class ResultSaveActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();
    private LocalDate todayLocalDate;
    private ResultVM mResultVM;

    /*------ INTENT ------*/
    private Integer position;
    private Workout workout;
    public static String WORKOUT_ID = "Workout ID";
    public static String WORKOUT_OBJ = "Workout Obj";

    /*------ HOOKS ------*/
    private TextView mName, mDescription, mExercises, mScoreType;
    private EditText mDate, mScore, mComments;
    private SwitchCompat mScaledSwitch;
    private SeekBar mScoreSeekBar;
    private Button workoutItemSaveButton, workoutItemPlanButton, workoutItemViewButton, saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_result_save, contentFrameLayout);

        /*------ HOOKS ------*/
        //workout_item
        mName = findViewById(R.id.workout_name);
        mDescription = findViewById(R.id.workout_description);
        mExercises = findViewById(R.id.workout_exercises);
        workoutItemSaveButton = findViewById(R.id.button_save_wod);
        workoutItemPlanButton = findViewById(R.id.button_plan_wod);
        workoutItemViewButton = findViewById(R.id.button_view_wod_results);
        //save results
        mDate = findViewById(R.id.edit_date);
        mScore = findViewById(R.id.edit_score);
        mComments = findViewById(R.id.edit_comments);
        mScoreType = findViewById(R.id.type_text);
        mScaledSwitch = findViewById(R.id.scaled_switch);
        mScoreSeekBar = findViewById(R.id.score_bar);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_save_button);

        /*------  VM  ------*/
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
        String exercises = WorkoutUtils.buildExerciseString(workout);

        // Fill the UI with workout info
        mName.setText(workout.getTitle());
        mDescription.setText(workout.getDescription());
        mExercises.setText(exercises);
        workoutItemSaveButton.setVisibility(GONE);
        workoutItemPlanButton.setVisibility(GONE);
        workoutItemViewButton.setVisibility(GONE);

        //  Change score_type text according to the current workout
        String scoreType = ResultUtils.setScoreType(workout);
        mScoreType.setText(scoreType);

        // Change hint of score input field according to workout type
        String scoreTypeHint = ResultUtils.setScoreTypeHint(workout);
        mScore.setHint(scoreTypeHint);

        // Change digits accepted on score input field according to workout type
        String scoreDigits = ResultUtils.setScoreDigits(workout);
        mScore.setKeyListener(DigitsKeyListener.getInstance(scoreDigits));

        // Default workout date set to current date
        todayLocalDate = LocalDate.now();
        mDate.setText(todayLocalDate.toString());

    }

    public void saveResult() {
        //Get all filled fields
        try {
            LocalDate date = LocalDate.parse(mDate.getText());
            boolean scaled = mScaledSwitch.isChecked();
            String score = String.valueOf(mScore.getText()); // Todo: deal better with different types of scores
            Integer rating = Math.round(mScoreSeekBar.getProgress());
            String comments = mComments.getText().toString();

            // Error message if score is not filled
            if (TextUtils.isEmpty(score)) {
                mScore.setError("Score is required");
                return;
            }
            // Error message if score has wrong format
            if (ResultUtils.isWrongScore(workout, score)) {
                mScore.setError("Score is required");
                return;
            }
            // Error message user enters date in the future
            if (date.isAfter(LocalDate.now())) {
                mDate.setError("You are not a clairvoyant");
                return;
            }

            // Save results
            mResultVM.addNewResult(workout, workout.getTitle(),score, rating, comments, date.toString(), scaled);
            Toast.makeText(getApplicationContext(), "Result saved", Toast.LENGTH_SHORT).show();

            //Go to result list of the workout saved
            //Todo: only use if we find a faster way to update the list
//            Intent intent = new Intent(this, ResultsActivity.class);
//            intent.putExtra(WORKOUT_ID, position);
//            intent.putExtra(WORKOUT_OBJ, Objects.requireNonNull(workout));
//            Log.i(LOG_TAG, "workout sent: " + workout);
//            startActivity(intent);
            finish();

        } catch (DateTimeParseException e) {
            //error message if date format is not respected and parsing fails
            e.printStackTrace();
            mDate.setError("Date format: yyyy-mm-dd");
        }
    }
}
