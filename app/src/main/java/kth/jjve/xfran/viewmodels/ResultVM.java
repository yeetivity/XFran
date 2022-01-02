package kth.jjve.xfran.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.repositories.ResultRepo;

public class ResultVM extends ViewModel {
    private MutableLiveData<List<Result>> mResults; //subclass of LiveData (mutable)
    private ResultRepo mRepo;

    public void init() {
        if(mResults != null){
            return;
        }
        mRepo = ResultRepo.getInstance();
        mResults = mRepo.getResults();
    }

    public LiveData<List<Result>> getResults(){
        return mResults;
    }
}

