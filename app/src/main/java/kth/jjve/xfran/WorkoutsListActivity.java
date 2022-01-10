package kth.jjve.xfran;
/*
Activity to let the user view workouts
 */

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.Objects;

import kth.jjve.xfran.adapters.WorkoutsRecyclerAdapter;
import kth.jjve.xfran.viewmodels.WorkoutVM;

public class WorkoutsListActivity extends BaseActivity implements WorkoutsRecyclerAdapter.ListItemClickListener {

    private final String LOG_TAG = getClass().getSimpleName();

    /*_________ VIEW MODEL _________*/
    private WorkoutVM mWorkoutVM;
    private WorkoutsRecyclerAdapter mAdapter;

    /*_________ INTENT _________*/
    public static String WORKOUT_ID = "Workout ID";
    public static String WORKOUT_OBJ = "Workout Obj";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_workouts, contentFrameLayout);

        navigationView.setCheckedItem(R.id.nav_workouts);

        /*------ HOOKS ------*/
        /*_________ VIEW _________*/
        RecyclerView mRecyclerView = findViewById(R.id.rv_workoutlist);

        /*----- VIEW MODEL -----*/
        mWorkoutVM = ViewModelProviders.of(this).get(WorkoutVM.class);
        mWorkoutVM.init();
        mWorkoutVM.getWorkouts().observe(this, workouts -> mAdapter.notifyDataSetChanged());

        /*------ INIT ------*/
        mAdapter = new WorkoutsRecyclerAdapter(this, mWorkoutVM.getWorkouts().getValue(), this, this::onPlan, this::onSave, this::onView);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    @Override
    public void onListItemClick(int position) {
    }

    public void onPlan(int position) {
        Intent intent = new Intent(this, CalendarViewActivity.class);
        intent.putExtra(WORKOUT_ID, position);
        intent.putExtra(WORKOUT_OBJ, Objects.requireNonNull(mWorkoutVM.getWorkouts().getValue()).get(position));
        Log.i(LOG_TAG, "workout sent: " + mWorkoutVM.getWorkouts().getValue().get(position));
        startActivity(intent);
    }

    public void onSave(int position) {
        Intent intent = new Intent(this, SaveResultsActivity.class);
        intent.putExtra(WORKOUT_ID, position);
        intent.putExtra(WORKOUT_OBJ, Objects.requireNonNull(mWorkoutVM.getWorkouts().getValue()).get(position));
        Log.i(LOG_TAG, "workout sent: " + mWorkoutVM.getWorkouts().getValue().get(position));
        startActivity(intent);
    }

    public void onView(int position) {
        Intent intent = new Intent(this, ResultsListWODActivity.class);
        intent.putExtra(WORKOUT_ID, position);
        intent.putExtra(WORKOUT_OBJ, Objects.requireNonNull(mWorkoutVM.getWorkouts().getValue()).get(position));
        Log.i(LOG_TAG, "workout sent: " + mWorkoutVM.getWorkouts().getValue().get(position));
        startActivity(intent);
    }

}