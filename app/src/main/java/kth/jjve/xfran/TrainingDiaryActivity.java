package kth.jjve.xfran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.adapters.RecyclerAdapter;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.viewmodels.TrainingDiaryViewModel;

public class TrainingDiaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /*_________ VIEW MODEL _________*/
    private TrainingDiaryViewModel mTrainingDiaryViewModel;
    private RecyclerAdapter mAdapter;

    /*_________ VIEW _________*/
    private RecyclerView mRecyclerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_diary);

        /*------ HOOKS ------*/
        drawerLayout = findViewById(R.id.drawerlayout_trainingdiary);
        mRecyclerView = findViewById(R.id.rv_trainingdiary);
        navigationView = findViewById(R.id.nav_view_diary);
        toolbar = findViewById(R.id.diary_toolbar);

        /*----- VIEW MODEL -----*/
        mTrainingDiaryViewModel = ViewModelProviders.of(this).get(TrainingDiaryViewModel.class);
        mTrainingDiaryViewModel.init();
        mTrainingDiaryViewModel.getWorkouts().observe(this, workouts -> mAdapter.notifyDataSetChanged());

        /*------ INIT ------*/
        setSupportActionBar(toolbar);
        initNavMenu();
        initRecyclerView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_diary);
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
            finish();
        } else if (id == R.id.nav_settings){
            //Todo: create something towards settings
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRecyclerView(){
        mAdapter = new RecyclerAdapter(this, mTrainingDiaryViewModel.getWorkouts().getValue());
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
        navigationView.setCheckedItem(R.id.nav_home);
    }
}