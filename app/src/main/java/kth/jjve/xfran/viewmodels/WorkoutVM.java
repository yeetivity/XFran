package kth.jjve.xfran.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.repositories.WorkoutRepo;

public class WorkoutVM extends ViewModel {
    private MutableLiveData<List<Workout>> mWorkouts; //subclass of LiveData (mutable)
    private WorkoutRepo mRepo;

    public void init() {
        if(mWorkouts != null){
            return;
        }
        mRepo = WorkoutRepo.getInstance();
        try {
            mWorkouts = mRepo.getWorkouts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Workout>> getWorkouts(){
        /*
        LiveData can only be observed, not mutable
        Therefore it will not change during a state change of the fragment
         */
        return mWorkouts;
    }
}