package kth.jjve.xfran.repositories;
/*
Repository for result objects
 */

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.utils.CalendarUtils;

public class ResultRepo {

    private static final String LOG_TAG = ResultRepo.class.getSimpleName();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String resultID;
    private Integer resultNumber; //Todo: find a better name for this

    private static ResultRepo instance;
    private List<Result> resultList = new ArrayList<>();
    private Result result = new Result();

    private final MutableLiveData<List<Result>> res = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ArrayList<Integer>>> resCalendar = new MutableLiveData<>();

    public static ResultRepo getInstance() {
        if (instance == null) {
            instance = new ResultRepo();
        }
        return instance;
    }

    private boolean initFirebase() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();
            return true;
        } else {
            return false;
        }
    }

    public MutableLiveData<List<Result>> getResults() {

        if (initFirebase()) {
            // get all the results on firebase for the logged user
            resultList = new ArrayList<>();
            fStore.collection("users").document(userID).collection("results")
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
            Log.d(LOG_TAG, "res is: " + res);
        }
        return res;
    }

    public MutableLiveData<List<Result>> getFilteredResults(String workoutName) {
        if (initFirebase()) {
            // get results on firebase that match the workout name for the logged user
            resultList = new ArrayList<>();
            fStore.collection("users").document(userID).collection("results")
                    .whereEqualTo("wodName", workoutName)
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

            Log.d(LOG_TAG, "res is: " + res);
        }
        return res;
    }

    public void addNewResult(Workout workout, String wodName, String score, Integer rating,
                             String comments, String date, boolean scaled) {

        resultNumber = 1;
        result = new Result(workout, wodName, score, rating, comments, date, scaled);


        //Save the data to the database
        if (initFirebase()){
            resultID = date;
            checkAndWrite(resultID, wodName, date, result);

        } else {
            Log.i("ResultRepo", "User is not logged in");
        }
    }

    public void checkAndWrite(String ID, String wodName, String date, Result result){
        DocumentReference docRef = fStore.collection("users").document(userID).collection("results").document(ID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()){
                    if (Objects.equals(document.getString("wodName"), wodName)){
                        saveResult(docRef, result);
                    } else{
                        resultID = date + "_" + resultNumber;
                        resultNumber ++;
                        checkAndWrite(resultID, wodName, date, result);
                    }
                }else{
                    saveResult(docRef, result);
                }
            }
        });

    }

    public void saveResult(DocumentReference dr, Result result){
        Map<String, Object> resultData = new HashMap<>();

        resultData.put("workout", result.getWorkout());
        resultData.put("wodName", result.getWodName());
        resultData.put("date", result.getDate());
        resultData.put("scaled", result.isScaled());
        resultData.put("score", result.getScore());
        resultData.put("feelScore", result.getFeelScore());
        resultData.put("comments", result.getComments());

        dr.set(resultData).addOnSuccessListener(aVoid ->
                Log.d("ResultRepo", "onSuccess: result was saved for " + userID + "at" + resultID));

    }

    public MutableLiveData<ArrayList<ArrayList<Integer>>> getListOfDates(LocalDate selectedDate) {
        /*
        Method that creates both a list of dates on which the user has worked out and the feelScores that were logged.
        The list is only made for the month the user is currently looking at in the calendar activity
        If a workout is only planned, the feelScore is non existing in the database and a 0 is added to the list
         */
        ArrayList<Integer> dates = new ArrayList<>();
        ArrayList<Integer> feels = new ArrayList<>();
        ArrayList<ArrayList<Integer>> combine = new ArrayList<>();

        if (initFirebase()){
            CollectionReference c = fStore.collection("users").document(userID).collection("results");
            c.get().addOnCompleteListener(task -> { // Gets all the files inside of the collection
                if (task.isSuccessful()){
                    // Loop through the documents and check the dates
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String date = document.getId();
                        String actualdate = date.substring(0, 10);

                        LocalDate resultDate = CalendarUtils.dateFromString(actualdate);

                        if (resultDate.getMonthValue() == selectedDate.getMonthValue() &&
                        resultDate.getYear() == selectedDate.getYear()){
                            dates.add(resultDate.getDayOfMonth());
                            try{
                                int feel = Objects.requireNonNull(document.getLong("feelScore")).intValue();
                                feels.add(feel);
                            } catch (Exception e){
                                feels.add(0); // no result is logged, which means the event is planned
                            }
                        }
                    }
                    combine.add(dates);
                    combine.add(feels);
                    resCalendar.setValue(combine);
                }
            });
        }

        return resCalendar;
    }
}
