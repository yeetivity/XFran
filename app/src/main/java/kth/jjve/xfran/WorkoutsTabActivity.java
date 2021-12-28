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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import kth.jjve.xfran.adapters.WorkoutsRecyclerAdapter;
import kth.jjve.xfran.viewmodels.WorkoutsViewModel;

public class WorkoutsTabActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WorkoutsRecyclerAdapter.ListItemClickListener {

    private final String LOG_TAG = getClass().getSimpleName();

    /*_________ VIEW MODEL _________*/
    private WorkoutsViewModel mWorkoutsViewModel;
    private WorkoutsRecyclerAdapter mAdapter;

    /*_________ VIEW _________*/
    private RecyclerView mRecyclerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    /*_________ INTENT _________*/
    public static String WORKOUT_ID="Workout ID";
    public static String WORKOUT_OBJ="Workout Obj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts_tab);

        /*------ HOOKS ------*/
        drawerLayout = findViewById(R.id.drawerlayout_trainingdiary);
        mRecyclerView = findViewById(R.id.rv_trainingdiary);
        navigationView = findViewById(R.id.nav_view_diary);
        toolbar = findViewById(R.id.diary_toolbar);

        /*----- VIEW MODEL -----*/
        mWorkoutsViewModel = ViewModelProviders.of(this).get(WorkoutsViewModel.class);
        mWorkoutsViewModel.init();
        mWorkoutsViewModel.getWorkouts().observe(this, workouts -> mAdapter.notifyDataSetChanged());

        /*------ INIT ------*/
        setSupportActionBar(toolbar);
        initNavMenu();
        initRecyclerView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home){
            Intent intent = new Intent(WorkoutsTabActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRecyclerView(){
        mAdapter = new WorkoutsRecyclerAdapter(this, mWorkoutsViewModel.getWorkouts().getValue(), this,this::onPlanButtonClick, this::onSaveButtonClick);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initNavMenu(){
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_workouts);
    }

    @Override
    public void onListItemClick(int position) {
        //List Item listener
        //Do something on UI when the item is clicked
        //Toast.makeText(this, mWorkoutsViewModel.getWorkouts().getValue().get(position).getType(), Toast.LENGTH_SHORT).show();
    }

    public void onPlanButtonClick(int position){
        //TODO start activity of workout planner
        //Intent intent = new Intent (this, HomeScreenActivity.class);
        //startActivity(intent);
        Toast.makeText(this, "Plan workout in development", Toast.LENGTH_SHORT).show();
    }

    public void onSaveButtonClick(int position){
        //start activity of workout saver
        Intent intent = new Intent (this, SaveResultsActivity.class);
        //send the position and the workout object selected
        intent.putExtra(WORKOUT_ID, position);
        intent.putExtra(WORKOUT_OBJ,mWorkoutsViewModel.getWorkouts().getValue().get(position));
        Log.i(LOG_TAG, "workout sent: "+mWorkoutsViewModel.getWorkouts().getValue().get(position));
        startActivity(intent);
    }

}