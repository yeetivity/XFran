package kth.jjve.xfran.repositories;


import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.models.Result;

public class ResultRepo {

    private static final String LOG_TAG = ResultRepo.class.getSimpleName();;
    private static ResultRepo instance;
    private ArrayList<Result> dataSet = new ArrayList<>();

    public static ResultRepo getInstance(){
        if(instance == null){
            instance = new ResultRepo();
        }
        return instance;
    }

    // Pretend to get data from a webservice or cage or online source
    public MutableLiveData<List<Result>> getResults() {
        //Todo: read from firebase
        MutableLiveData<List<Result>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setResults() throws IOException {
        // Todo: Add result to the mutable data list & save result to firebase
    }

}
