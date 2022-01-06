package kth.jjve.xfran.repositories;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Map;

import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.utils.CalendarUtils;

public class ResultRepo {

    private static final String LOG_TAG = ResultRepo.class.getSimpleName();

    private static ResultRepo instance;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String resultID;
    private Result result = new Result();
    private MutableLiveData<Result> res;
    private MutableLiveData<ArrayList<Integer>> days = new MutableLiveData<>();

    private ArrayList<Result> dataSet = new ArrayList<>();

    public static ResultRepo getInstance() {
        if (instance == null) {
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

    public void addNewResult(Workout workout, String score, Integer rating,
                             String comments, LocalDate date, boolean scaled) {
        result = new Result(workout, score, rating, comments, date, scaled);
        resultID = date.toString() + "_" + workout.getTitle().replaceAll(" ", "-").toLowerCase(); //Create identifier
        if (res != null) res.setValue(result);   //Add the result to the mutable data list

        //Save the data to the database
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID).collection("results").document(resultID);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("workout", result.getWorkout());
            resultData.put("date", result.getDate().toString());
            resultData.put("scaled", result.isScaled());
            resultData.put("score", result.getScore());
            resultData.put("feelScore", result.getFeelScore());
            resultData.put("comments", result.getComments());
            documentReference.set(resultData).addOnSuccessListener(aVoid -> Log.d("ResultRepo", "onSuccess: result was saved for " + userID + "at" + resultID));
        } else {
            Log.i("ResultRepo", "User is not logged in");
        }
    }


    public MutableLiveData<ArrayList<Integer>> getListOfDates(LocalDate selectedDate) {
        ArrayList<Integer> dates = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();

            CollectionReference c = fStore.collection("users").document(userID).collection("results");
            c.get().addOnCompleteListener(task -> { // gets all the files inside of the collection
                if (task.isSuccessful()){
                    // loop through the documents and check the dates
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String d = document.getId();
                        LocalDate resultDate = CalendarUtils.dateFromString(d);

                        if (resultDate.getMonthValue() == selectedDate.getMonthValue() &&
                        resultDate.getYear() == selectedDate.getYear()){
                            dates.add(resultDate.getDayOfMonth());
                        }
                    }
                    days.setValue(dates);
                }
            });
        }

        return days;
    }

}
