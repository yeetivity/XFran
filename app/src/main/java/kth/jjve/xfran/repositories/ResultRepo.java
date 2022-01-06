package kth.jjve.xfran.repositories;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kth.jjve.xfran.adapters.MontlyCalendarAdapter;
import kth.jjve.xfran.models.Result;

public class ResultRepo {

    private static final String LOG_TAG = ResultRepo.class.getSimpleName();
    private static ResultRepo instance;
    static FirebaseAuth fAuth;
    static FirebaseFirestore fStore;
    static String userID;
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

    private void setResults() {
        // Todo: Add result to the mutable data list & save result to firebase
    }

    public MutableLiveData<List<Integer>> getListOfDates(LocalDate selectedDate) {
        List<Integer> list = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        String month;

        if (selectedDate.getMonthValue() < 10){
            month = "0" + selectedDate.getMonthValue();
        }else{
            month = String.valueOf(selectedDate.getMonthValue());
        }

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();

            CollectionReference c = fStore.collection("users").document(userID).collection("results");
            c.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        dates.add(document.getId());

                        // convert document.getID to localDate
                        // check if the month and the year are correct
                        // append the day (int) to the list
                        // append the list to the muteable livedata shizzle
                    }

                }
            });
        }

        String startString = selectedDate.getYear() + "-" + month + "-";



        MutableLiveData<List<String>> m = new MutableLiveData<>();

        list.add(8);
        list.add(10);
        list.add(14);


        return list;
    }

}
