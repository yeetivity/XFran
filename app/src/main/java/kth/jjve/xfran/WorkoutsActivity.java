package kth.jjve.xfran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import kth.jjve.xfran.adapters.WorkoutsRecyclerAdapter;
import kth.jjve.xfran.viewmodels.WorkoutsVM;

public class WorkoutsActivity extends BaseActivity implements WorkoutsRecyclerAdapter.ListItemClickListener {

    private final String LOG_TAG = getClass().getSimpleName();

    /*_________ VIEW MODEL _________*/
    private WorkoutsVM mWorkoutsViewModel;
    private WorkoutsRecyclerAdapter mAdapter;

    /*_________ INTENT _________*/
    public static String WORKOUT_ID="Workout ID";
    public static String WORKOUT_OBJ="Workout Obj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_workouts, contentFrameLayout);
        navigationView.setCheckedItem(R.id.nav_workouts);

        /*------ HOOKS ------*/
        /*_________ VIEW _________*/
        RecyclerView mRecyclerView = findViewById(R.id.rv_trainingdiary);

        /*----- VIEW MODEL -----*/
        mWorkoutsViewModel = ViewModelProviders.of(this).get(WorkoutsVM.class);
        mWorkoutsViewModel.init();
        mWorkoutsViewModel.getWorkouts().observe(this, workouts -> mAdapter.notifyDataSetChanged());

        /*------ INIT ------*/
        mAdapter = new WorkoutsRecyclerAdapter(this, mWorkoutsViewModel.getWorkouts().getValue(), this,this::onPlanButtonClick, this::onSaveButtonClick);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    @Override
    public void onListItemClick(int position) {
        //Todo: Do something on UI when the item is clicked
    }

    public void onPlanButtonClick(int position){
        //TODO start activity of workout planner
        Toast.makeText(this, "Plan workout in development", Toast.LENGTH_SHORT).show();
    }

    public void onSaveButtonClick(int position){
        //start activity of workout saver
        Intent intent = new Intent (this, SaveResultsActivity.class);
        intent.putExtra(WORKOUT_ID, position);
        intent.putExtra(WORKOUT_OBJ, Objects.requireNonNull(mWorkoutsViewModel.getWorkouts().getValue()).get(position));
        Log.i(LOG_TAG, "workout sent: "+mWorkoutsViewModel.getWorkouts().getValue().get(position));
        startActivity(intent);
    }

}