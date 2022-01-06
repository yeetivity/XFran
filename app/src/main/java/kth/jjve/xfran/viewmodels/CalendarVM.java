package kth.jjve.xfran.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.ArrayList;

import kth.jjve.xfran.repositories.ResultRepo;

public class CalendarVM  extends AndroidViewModel {

    private ResultRepo mResultRepo;
    private MutableLiveData<ArrayList<Integer>> mWorkoutDays;

    public CalendarVM(@NonNull Application application){super(application);}

    public void init(LocalDate date){
        mResultRepo = ResultRepo.getInstance();
        mWorkoutDays = mResultRepo.getListOfDates(date);

    }

    public LiveData<ArrayList<Integer>> getWorkoutDays(){ return mWorkoutDays;}
}
