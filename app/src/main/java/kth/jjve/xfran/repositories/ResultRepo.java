package kth.jjve.xfran.repositories;

import android.util.JsonReader;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.AppCtx;
import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;

/**
 * Singleton pattern
 */
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
    public MutableLiveData<List<Result>> getResults() throws IOException {
        setResults(); //Todo: normally retrieve data from webservice or anything here
        MutableLiveData<List<Result>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setResults() throws IOException {
        // This mimics what the database would normally do
    }

}
