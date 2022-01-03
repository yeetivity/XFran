package kth.jjve.xfran.repositories;

/*
Function: repository for events
Activity: PlanEventActivity & WeeklyCalendarActivity
Jitse van Esch, Mariah Sabioni & Elisa Perini
 */

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kth.jjve.xfran.models.EventInApp;

public class EventRepo {

    private static EventRepo instance;
    private ArrayList<EventInApp> dataSet = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private EventInApp event = new EventInApp();
    String userID;
    String eventID;

    private MutableLiveData<EventInApp> ev;

    public static EventRepo getInstance() {
        if (instance == null) {
            instance = new EventRepo();
        }
        return instance;
    }

    public MutableLiveData<List<EventInApp>> getEvents() {
        //Todo: read from firebase
        MutableLiveData<List<EventInApp>> data = new MutableLiveData<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        data.setValue(dataSet);
        return data;
    }


    public void addNewEvent(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        event = new EventInApp(name, date, startTime, endTime);
        eventID = date.toString() + "_" + event.getName().replaceAll(" ", "-").toLowerCase(); //Create identifier
        if (ev != null) ev.setValue(event);   //Add the result to the mutable data list

        //Save the data to the database
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            userID = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("events").document(eventID);
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("workout", event.getName());
            eventData.put("date", event.getDate().toString());
            eventData.put("startTime", event.getStartTime());
            eventData.put("endTime", event.getEndTime());
            documentReference.set(eventData).addOnSuccessListener(aVoid -> Log.d("EventRepo", "onSuccess: result was saved for " + userID + "at" + eventID));
        } else {
            Log.i("EventRepo", "User is not logged in");
        }

    }

}
