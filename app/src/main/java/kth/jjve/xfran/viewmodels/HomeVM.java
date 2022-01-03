package kth.jjve.xfran.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.repositories.UserRepo;
import kth.jjve.xfran.repositories.WorkoutRepo;

public class HomeVM extends AndroidViewModel {
    private MutableLiveData<String> mUserName, mWODTitle, mWODDescription;
    private MutableLiveData<ArrayList<String>> mWODExercises;
    private LocalDate mDate;
    private int wodChooser, previousWOD;

    private UserRepo mUserRepo;
    private WorkoutRepo mWorkoutRepo;


    public HomeVM(@NonNull Application application) {
        super(application);
    }

    public void init() {
        mUserRepo = UserRepo.getInstance();
        mUserName = mUserRepo.getUserName();

        mWorkoutRepo = WorkoutRepo.getInstance();
        try {
            setWOD(mWorkoutRepo.getWorkouts());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setWOD(MutableLiveData<List<Workout>> mWorkouts) {
        // Todo: mDate now gets overwritten when activity is destroyed, find a way to make sure it does not
        if (LocalDate.now() != mDate) {
            mDate = LocalDate.now();

            // Get a random workout
            Random rand = new Random();
            while (wodChooser == previousWOD) {
                wodChooser = rand.nextInt(Objects.requireNonNull(mWorkouts.getValue()).size());
            }

            Workout WOD = Objects.requireNonNull(mWorkouts.getValue()).get(wodChooser);
            setWODDetails(WOD);
            previousWOD = wodChooser;
        }

    }

    private void setWODDetails(Workout wod) {
        mWODTitle = new MutableLiveData<>();
        mWODDescription = new MutableLiveData<>();
        mWODExercises = new MutableLiveData<>();

        mWODTitle.setValue(wod.getTitle());
        mWODDescription.setValue(wod.getType());
        mWODExercises.setValue(wod.getExercises());
    }

    public LiveData<String> getUserName() {
        return mUserName;
    }

    public LiveData<String> getWODTitle() {
        return mWODTitle;
    }

    public LiveData<String> getWODDescription() {
        return mWODDescription;
    }

    public LiveData<ArrayList<String>> getWODExercises() {
        return mWODExercises;
    }
}
