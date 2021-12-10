package kth.jjve.xfran.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.repositories.WorkoutRepository;

public class TrainingDiaryViewModel extends ViewModel {
    private MutableLiveData<List<Workout>> mWorkouts; //subclass of LiveData (mutable)
    private WorkoutRepository mRepo;

    public void init(){
        if(mWorkouts != null){
            return;
        }
        mRepo = WorkoutRepository.getInstance();
        mWorkouts = mRepo.getWorkouts();
    }

    public LiveData<List<Workout>> getWorkouts(){
        /*
        LiveData can only be observed, not mutable
        Therefore it will not change during a state change of the fragment
         */
        return mWorkouts;
    }
}
