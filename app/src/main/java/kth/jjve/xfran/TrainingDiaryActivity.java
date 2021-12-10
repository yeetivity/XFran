package kth.jjve.xfran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.adapters.RecyclerAdapter;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.viewmodels.TrainingDiaryViewModel;

public class TrainingDiaryActivity extends AppCompatActivity {
    private TrainingDiaryViewModel mTrainingDiaryViewModel;
    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_diary);
        mRecyclerView = findViewById(R.id.rv_trainingdiary);

        mTrainingDiaryViewModel = ViewModelProviders.of(this).get(TrainingDiaryViewModel.class);

        mTrainingDiaryViewModel.init();

        mTrainingDiaryViewModel.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                mAdapter.notifyDataSetChanged();
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView(){
        mAdapter = new RecyclerAdapter(this, mTrainingDiaryViewModel.getWorkouts().getValue());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);

    }
}