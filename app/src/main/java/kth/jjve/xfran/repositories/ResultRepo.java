package kth.jjve.xfran.repositories;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;

public class ResultRepo {

    private static final String LOG_TAG = ResultRepo.class.getSimpleName();

    private static ResultRepo instance;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    String resultID;
    private Result result = new Result();
    private MutableLiveData<Result> res;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            userID = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("results").document(resultID);
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
}
