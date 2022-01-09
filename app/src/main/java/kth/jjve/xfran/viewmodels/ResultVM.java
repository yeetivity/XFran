package kth.jjve.xfran.viewmodels;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.repositories.ResultRepo;

public class ResultVM extends AndroidViewModel {

    private static final String LOG_TAG = ResultVM.class.getSimpleName();

    private MutableLiveData<List<Result>> mResults; //subclass of LiveData (mutable)
    private ResultRepo mRepo;

    public ResultVM(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (mResults != null) {
            mResults = mRepo.getResults();
            return;
        }
        mRepo = ResultRepo.getInstance();
        mResults = mRepo.getResults();
        Log.d(LOG_TAG, "mResults is: " + mResults.getValue().size() + " elements");
    }

    public void initFiltered(String workoutName) {
        if (mResults != null) {
            mResults = mRepo.getFilteredResults(workoutName);
            return;
        }
        mRepo = ResultRepo.getInstance();
        mResults = mRepo.getFilteredResults(workoutName);
        Log.d(LOG_TAG, "mResults is: " + mResults.getValue().size() + " elements");
    }

    public LiveData<List<Result>> getResults() {
        return mResults;
    }

    public void addNewResult(Workout workout, String wodName, String score, Integer rating,
                             String comments, String date, boolean scaled) {
        mRepo.addNewResult(workout, wodName, score, rating, comments, date, scaled);
    }
}

