package kth.jjve.xfran.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.repositories.LocalStorage;
import kth.jjve.xfran.repositories.UserRepo;
import kth.jjve.xfran.repositories.WorkoutRepo;

public class HomeVM extends AndroidViewModel {
    private MutableLiveData<String> mUserName, mWODTitle, mWODDescription;
    private MutableLiveData<ArrayList<String>> mWODExercises;
    private int wodChooser, previousWOD;

    private UserRepo mUserRepo;
    private WorkoutRepo mWorkoutRepo;
    private LocalStorage mLocalStorage;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;


    public HomeVM(@NonNull Application application) {
        super(application);
    }

    public void init(Context context) {
        mContext = context;
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
        if (checkLastUpdate()) {

            // Get a random workout
            Random rand = new Random();
            while (wodChooser == previousWOD) {
                wodChooser = rand.nextInt(Objects.requireNonNull(mWorkouts.getValue()).size());
            }

            Workout WOD = Objects.requireNonNull(mWorkouts.getValue()).get(wodChooser);
            setWODDetails(WOD);
            updateLastUpdate(wodChooser);
        } else {
            Workout WOD = Objects.requireNonNull(mWorkouts.getValue()).get(previousWOD);
            setWODDetails(WOD);
        }

    }

    private boolean checkLastUpdate() {
        try {
            FileInputStream fin = mContext.openFileInput("storage.ser");

            // Wrapping our stream
            ObjectInputStream oin = new ObjectInputStream(fin);

            // Reading in our local storage object
            mLocalStorage = (LocalStorage) oin.readObject();

            // Closing our object stream which also closes the wrapped stream
            oin.close();

        } catch (Exception e) {
            Log.i("HomeVM", "Error is " + e);
            return true;
        }
        LocalDate previousDate = mLocalStorage.getDate();
        previousWOD = mLocalStorage.getPreviousWOD();
        LocalDate date = LocalDate.now();

        return previousDate.getDayOfMonth() != date.getDayOfMonth() ||
                previousDate.getMonthValue() != date.getMonthValue() ||
                previousDate.getYear() != date.getYear();
    }

    private void updateLastUpdate(int wod) {
        if (mLocalStorage == null) {
            mLocalStorage = new LocalStorage(LocalDate.now());
            mLocalStorage.setPreviousWOD(wod);
        } else {
            mLocalStorage.setDateStorage(LocalDate.now());
            mLocalStorage.setPreviousWOD(wod);
        }
        try {
            FileOutputStream fos = mContext.openFileOutput("storage.ser", Context.MODE_PRIVATE);

            // Wrapping our filestream
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Writing the object to the file
            oos.writeObject(mLocalStorage);

            // Closing the stream
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setWODDetails(Workout wod) {
        mWODTitle = new MutableLiveData<>();
        mWODDescription = new MutableLiveData<>();
        mWODExercises = new MutableLiveData<>();

        mWODTitle.setValue(wod.getTitle());
        mWODDescription.setValue(wod.getType());
        mWODExercises.setValue(wod.getDetails());
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
