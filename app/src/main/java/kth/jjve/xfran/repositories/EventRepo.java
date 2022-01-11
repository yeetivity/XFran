package kth.jjve.xfran.repositories;

/*
Function: repository for events, read and write data from firebase
Activity: PlanEventActivity & WeeklyCalendarActivity
Jitse van Esch, Mariah Sabioni & Elisa Perini
 */

import static kth.jjve.xfran.utils.CalendarUtils.dateFromString;
import static kth.jjve.xfran.utils.CalendarUtils.timeFromString;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private List<EventInApp> eventList;
    private Integer filenumber;
    private String dateString;

    private EventInApp event = new EventInApp();
    String userID;
    String eventID;

    private MutableLiveData<List<EventInApp>> ev;

    public static EventRepo getInstance() {
        if (instance == null) {
            instance = new EventRepo();
        }
        return instance;
    }

    public MutableLiveData<List<EventInApp>> getEvents(LocalDate date) {
        ev = new MutableLiveData<>();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            userID = fAuth.getCurrentUser().getUid();

            dateString = date.toString().replaceAll(" ", "-");
            eventList = new ArrayList<>();
            filenumber = 1;

            ev = readMultipleDocument(date);
            ev.setValue(eventList);
        }
        return ev;
    }


    public void addNewEvent(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        event = new EventInApp(name, date, startTime, endTime); // Create new event object
        eventID = date.toString().replaceAll(" ", "-");
        filenumber = 1;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            userID = fAuth.getCurrentUser().getUid();
            checkDocument(date);

        } else {
            Log.i("EventRepo", "User is not logged in");
        }
    }

    private void checkDocument(LocalDate date){
        DocumentReference docRef = fStore.collection("users").document(userID).collection("events").document(eventID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){ // document with eventID already exists
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()){
                    eventID = date.toString().replaceAll(" ", "-") + "_" + filenumber;
                    filenumber ++;
                    checkDocument(date);
                } else {
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("workout", event.getName());
                    eventData.put("date", event.getDate().toString());
                    eventData.put("startTime", event.getStartTime().toString());
                    eventData.put("endTime", event.getEndTime().toString());
                    docRef.set(eventData).addOnSuccessListener(aVoid -> Log.d("EventRepo", "onSuccess: result was saved for " + userID + "at" + eventID));
                }
            } else {
                Log.d("EventRepo", "something went wrong");
            }
        });
    }

    private MutableLiveData<List<EventInApp>> readMultipleDocument(LocalDate date){
        DocumentReference documentReference = fStore.collection("users").document(userID).collection("events").document(dateString);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    try {
                        // update the event and add it to the list
                        EventInApp eventInApp = new EventInApp(
                                document.getString("workout"),
                                dateFromString(document.getString("date")),
                                timeFromString(document.getString("endTime")),
                                timeFromString(document.getString("startTime")));
                        eventList.add(eventInApp);

                        dateString = date.toString().replaceAll(" ", "-") + "_" + filenumber;
                        filenumber ++;
                        readMultipleDocument(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else Log.d("firebase", String.valueOf(task.getResult()));
        });
        ev.setValue(eventList);
        return ev;
    }

}

