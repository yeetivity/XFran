package kth.jjve.xfran;

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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kth.jjve.xfran.adapters.ResultsWODRecyclerAdapter;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.utils.ResultUtils;
import kth.jjve.xfran.utils.WorkoutUtils;
import kth.jjve.xfran.viewmodels.ResultVM;

import static android.view.View.GONE;

public class ResultsListWODActivity extends BaseActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    /*------ INTENT ------*/
    public static String WORKOUT_ID = "Workout ID";
    public static String WORKOUT_OBJ = "Workout Obj";

    /*------ VIEW MODEL ------*/
    private ResultVM mResultVM;
    private ResultsWODRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /*------ INTENT ------*/
    private String workoutName;
    private Integer position;
    private Workout workout;

    /*------ HOOKS ------*/
    private TextView mName, mDescription, mExercises;
    private Button workoutItemSaveButton, workoutItemPlanButton, workoutItemViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_result_view_wod, contentFrameLayout);

        navigationView.setCheckedItem(R.id.nav_results);

        /*------ HOOKS ------*/
        //workout_item
        mName = findViewById(R.id.workout_name);
        mDescription = findViewById(R.id.workout_description);
        mExercises = findViewById(R.id.workout_exercises);
        workoutItemSaveButton = findViewById(R.id.button_save_wod);
        workoutItemPlanButton = findViewById(R.id.button_plan_wod);
        workoutItemViewButton = findViewById(R.id.button_view_wod_results);

        /*------ INTENT ------*/
        Intent intent = getIntent();
        position = intent.getIntExtra(WORKOUT_ID, 1);
        workout = (Workout) intent.getSerializableExtra(WORKOUT_OBJ);
        workoutName = workout.getTitle();
        Log.i(LOG_TAG, "workout read: " + workoutName);

        // Build a string with exercises
        String exercises = WorkoutUtils.buildExerciseString(workout);

        // Fill the UI with workout info
        mName.setText(workout.getTitle());
        mDescription.setText(workout.getDescription());
        mExercises.setText(exercises);
        workoutItemSaveButton.setVisibility(GONE);
        workoutItemPlanButton.setVisibility(GONE);
        workoutItemViewButton.setVisibility(GONE);

        /*------ VIEW ------*/
        mRecyclerView = findViewById(R.id.rv_resultlistwod);

        /*------ VIEW MODEL ------*/
        mResultVM = ViewModelProviders.of(this).get(ResultVM.class);
        mResultVM.initFiltered(workoutName);

        /*------ INIT ------*/
        mAdapter = new ResultsWODRecyclerAdapter(this, mResultVM.getResults().getValue());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
        mResultVM.getResults().observe(this, this::setRecyclerView);
    }

    private void setRecyclerView(List<Result> resultList) {
        // update UI with data retrieved from firebase
        mRecyclerView.setAdapter(new ResultsWODRecyclerAdapter(this, resultList));
        Log.d(LOG_TAG, "new adapter set for new resultList: " + resultList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_results);
    }

}