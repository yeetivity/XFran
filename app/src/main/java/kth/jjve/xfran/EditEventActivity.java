package kth.jjve.xfran;

/*
Activity to input and save the event
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.time.LocalTime;

import kth.jjve.xfran.models.EventInApp;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class EditEventActivity extends AppCompatActivity {
    private static final String LOG_TAG = EditEventActivity.class.getSimpleName();
    private EditText eventName, eventStartTimeEdit, eventEndTimeEdit;
    private String s_eventName, startTime, endTime;

    /*----- CALENDAR API -----*/
    final int callbackId = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        /*------ HOOKS ------*/
        eventName = findViewById(R.id.eventName);
        TextView eventDate = findViewById(R.id.eventDate);
        TextView eventStartTime = findViewById(R.id.eventStartTime);
        TextView eventEndTime = findViewById(R.id.eventEndTime);
        eventStartTimeEdit = findViewById(R.id.eventStartTimeEdit);
        eventEndTimeEdit = findViewById(R.id.eventEndTimeEdit);
        Button buttonSave = findViewById(R.id.eventSave);
        Button buttonClose = findViewById(R.id.close);
        Button buttonExport = findViewById(R.id.savetogoogle);

        String s_date = "Date: " + CalendarUtils.cleanDate(CalendarUtils.selectedDate);
        eventDate.setText(s_date);

        /*-------- LISTENERS ------------*/
        buttonClose.setOnClickListener(v -> { finish(); });
        buttonSave.setOnClickListener(v -> { saveEvent(); });
        buttonExport.setOnClickListener(v -> { saveEventToCalendar(); });

    }

    private void setEventInApp(){
        // method to obtain the events name and start/end time
        s_eventName = eventName.getText().toString(); //given name
        startTime = eventStartTimeEdit.getText().toString(); //start time
        endTime = eventEndTimeEdit.getText().toString(); //end time

        if (TextUtils.isEmpty(s_eventName)){
            eventName.setError("Event name is required");
            return;
        }

        //TODO make safety feature if time format wrong pretty
        if (TextUtils.isEmpty(startTime)){
            eventStartTimeEdit.setError("Start time is required");
            return;
        }

        if (TextUtils.isEmpty(endTime)){
            eventEndTimeEdit.setError("End time is required");
            return;
        }
    }

    private void saveEvent(){
        //method to save the name, start and end time of an event into a list, called by save button
        setEventInApp();
        createEventInApp();
    }
    //TODO delete event
    //TODO save events into repository

    private void saveEventToCalendar(){
        // method to save the event in the calendar in the app and in the calendar provider, called by export buttom
        setEventInApp();
        calendarProviderAPI();
        createEventInApp();
    }

    private void createEventInApp(){
        // method to create the event and output it in the app
        try {
            EventInApp newEventInApp = new EventInApp(s_eventName, CalendarUtils.selectedDate, LocalTime.parse(startTime), LocalTime.parse(endTime));
            EventInApp.eventsList.add(newEventInApp);
            Toast.makeText(getApplicationContext(), "event saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "no event info added", Toast.LENGTH_SHORT).show();
        }
    }

    private void calendarProviderAPI(){
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        //TODO remove hardcoding of date
        beginTime.set(2022, 1, 1, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2022, 1, 1, 8, 45);
        endMillis = endTime.getTimeInMillis();

        checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "Jazzercise");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());

        //Toast.makeText(getApplicationContext(), (int) eventID, Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, String.valueOf(eventID));

    }

    private void checkPermission(int callbackId, String... permissionsId) {
        // method to check if permissions granted, needed for "dangerous" permission like write calendar
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }
        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }

}
