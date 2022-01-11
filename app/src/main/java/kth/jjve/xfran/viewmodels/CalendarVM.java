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
    private MutableLiveData<ArrayList<ArrayList<Integer>>> mCalendarResults;

    public CalendarVM(@NonNull Application application){super(application);}

    public void init(LocalDate date){
        mResultRepo = ResultRepo.getInstance();
        mCalendarResults = mResultRepo.getListOfDates(date);

    }

    public LiveData<ArrayList<ArrayList<Integer>>> getWorkoutDays(){ return mCalendarResults;}
}
