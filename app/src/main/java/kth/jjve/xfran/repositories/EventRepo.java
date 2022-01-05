package kth.jjve.xfran.repositories;

/*
Function: repository for events
Activity: PlanEventActivity & WeeklyCalendarActivity
Jitse van Esch, Mariah Sabioni & Elisa Perini
 */

import static kth.jjve.xfran.weeklycalendar.CalendarUtils.dateFromString;
import static kth.jjve.xfran.weeklycalendar.CalendarUtils.timeFromString;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kth.jjve.xfran.EditEventActivity;
import kth.jjve.xfran.adapters.EventAdapter;
import kth.jjve.xfran.models.EventInApp;

public class EventRepo {

    private static final String LOG_TAG = EventRepo.class.getSimpleName();
    private static EventRepo instance;
    private ArrayList<EventInApp> dataSet = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

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
        //Todo: read from firebase
        ev = new MutableLiveData<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        String dateString = date.toString().replaceAll(" ", "-");

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("events").document(dateString);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                // fill user profile with firestore data
                try {
                    // First try to fill the complete profile
                    //noinspection ConstantConditions
                    List<EventInApp> eventList = new ArrayList<>();
                    EventInApp eventInApp = new EventInApp(
                            document.getString("workout"),
                            dateFromString(document.getString("date")),
                            timeFromString(document.getString("endTime")),
                            timeFromString(document.getString("startTime")));
                    eventList.add(eventInApp);
                    ev.setValue(eventList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else Log.d("firebase", String.valueOf(task.getResult()));
        });

        return ev;
    }


    public void addNewEvent(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        event = new EventInApp(name, date, startTime, endTime);
        //eventID = date.toString() + "_" + event.getName().replaceAll(" ", "-").toLowerCase(); //Create identifier
        eventID = date.toString().replaceAll(" ", "-");

        //Save the data to the database
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            userID = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("events").document(eventID);
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("workout", event.getName());
            eventData.put("date", event.getDate().toString());
            eventData.put("startTime", event.getStartTime().toString());
            eventData.put("endTime", event.getEndTime().toString());
            documentReference.set(eventData).addOnSuccessListener(aVoid -> Log.d("EventRepo", "onSuccess: result was saved for " + userID + "at" + eventID));
        } else {
            Log.i("EventRepo", "User is not logged in");
        }
    }

    /*
    public static ArrayList<EventInApp> eventsForDate(LocalDate date) {
        ArrayList<EventInApp> eventInApps = new ArrayList<>();

        for(EventInApp eventInApp : eventsList) {
            if(eventInApp.getDate().equals(date))
                eventInApps.add(eventInApp);
        }

        return eventInApps;
    } */

}

