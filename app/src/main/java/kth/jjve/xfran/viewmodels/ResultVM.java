package kth.jjve.xfran.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.repositories.ResultRepo;
import kth.jjve.xfran.repositories.WorkoutRepo;

public class ResultVM extends ViewModel {
    private MutableLiveData<List<Result>> mResults; //subclass of LiveData (mutable)
    private ResultRepo mRepo;

    public void init() {
        if(mResults != null){
            return;
        }
        mRepo = ResultRepo.getInstance();
        try {
            mResults = mRepo.getResults();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Result>> getResults(){
        /*
        LiveData can only be observed, not mutable
        Therefore it will not change during a state change of the fragment
         */
        return mResults;
    }
}

