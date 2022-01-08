package kth.jjve.xfran.repositories;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kth.jjve.xfran.LoginActivity;
import kth.jjve.xfran.models.EventInApp;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;

import static kth.jjve.xfran.weeklycalendar.CalendarUtils.dateFromString;
import static kth.jjve.xfran.weeklycalendar.CalendarUtils.timeFromString;

public class ResultRepo {

    private static final String LOG_TAG = ResultRepo.class.getSimpleName();

    private static ResultRepo instance;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    String resultID;
    private List<Result> resultList = new ArrayList<Result>();
    ;
    private Result result = new Result();
    private MutableLiveData<List<Result>> res;

    public static ResultRepo getInstance() {
        if (instance == null) {
            instance = new ResultRepo();
        }
        return instance;
    }

    public MutableLiveData<List<Result>> getResults() {
        res = new MutableLiveData<>();
        initFirebase();
        if (firebaseAuth.getCurrentUser() != null) {
            userID = firebaseAuth.getCurrentUser().getUid();

            res = readAllCollection();
            res.setValue(resultList);
            Log.d(LOG_TAG, "res is: " + res);
        }
        return res;
    }


    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private MutableLiveData<List<Result>> readAllCollection() {
        resultList = new ArrayList<>();
        firebaseFirestore.collection("users").document(userID).collection("results")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            resultList.add(document.toObject(Result.class));
                            Log.d(LOG_TAG, "resultList is => " + resultList);
                        }
                        res.setValue(resultList);
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                });
        return res;
    }


    public void addNewResult(Workout workout, String score, Integer rating,
                             String comments, String date, boolean scaled) {
        result = new Result(workout, score, rating, comments, date, scaled);
        resultID = date.toString() + "_" + workout.getTitle().replaceAll(" ", "-").toLowerCase(); //Create identifier

        //resultList.add(result);
        //if (res != null) res.setValue(resultList);   //Add the result to the mutable data list

        //Save the data to the database
        initFirebase();

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
