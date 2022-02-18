package kth.jjve.xfran;

/*
Activity to input and save the event
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.utils.CalendarUtils.buildDate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import java.time.LocalTime;

import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.utils.CalendarUtils;
import kth.jjve.xfran.viewmodels.EventVM;

public class EventPlanActivity extends BaseActivity {

    private static final String LOG_TAG = EventPlanActivity.class.getSimpleName();
    private EditText eventNameET, eventStartTimeEdit, eventEndTimeEdit, eventDateET;
    private String s_eventName, startTime, stopTime, s_eventDate;
    private EventVM mEventVM;

    /*_________ INTENT _________*/
    private Integer position;
    private Workout mWorkout;

    /*-------- DATE PICKER ------*/
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_planevent, contentFrameLayout);

        /*------ HOOKS ------*/
        eventNameET = findViewById(R.id.eventNameET);
        TextView eventNameTV = findViewById(R.id.eventNameTV);
        TextView eventDate = findViewById(R.id.eventDate);
        TextView eventStartTime = findViewById(R.id.eventStartTime);
        TextView eventEndTime = findViewById(R.id.eventEndTime);
        eventStartTimeEdit = findViewById(R.id.eventStartTimeEdit);
        eventEndTimeEdit = findViewById(R.id.eventEndTimeEdit);
        eventDateET = findViewById(R.id.eventDateET);
        Button buttonSave = findViewById(R.id.eventSave);
        Button buttonClose = findViewById(R.id.close);
        Button buttonExport = findViewById(R.id.savetogoogle);

        /*----- CALENDAR ------*/
        String s_date = "Date:";
        eventDate.setText(s_date);

        /*-----  VM  -----*/
        mEventVM = ViewModelProviders.of(this).get(EventVM.class);
        mEventVM.init(CalendarUtils.selectedDate);

        /*-------- LISTENERS ------------*/
        buttonSave.setOnClickListener(v -> saveEvent());
        buttonClose.setOnClickListener(v -> finish());
        buttonExport.setOnClickListener(this::onClick);

        /*------ INTENT ------*/
        Intent intent = getIntent();
        position = intent.getIntExtra(WorkoutsListActivity.WORKOUT_ID,1);
        mWorkout = (Workout) intent.getSerializableExtra(WorkoutsListActivity.WORKOUT_OBJ);

        /*----- VISIBILITY ------*/
        if (mWorkout == null){
            eventNameTV.setVisibility(View.GONE);
        } else { //if accessing from WorkoutsListActivity (with intent)
            eventNameET.setVisibility(View.GONE);
            s_eventName = mWorkout.getTitle();
            eventNameTV.setText(s_eventName);
        }

        /*------ DATE PICKER DIALOG -----*/
        // TODO: see why you need to press the edittext twice
        eventDateET.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH); // ADD +1 to get actual month!!!
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(EventPlanActivity.this,
                    (view, year, month, day) -> {
                        // set day of month , month and year value in the edit text
                        String date = day + "/"  + (month + 1) + "/" + year;
                        eventDateET.setText(date);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

    }

    private void setEventInApp(){
        // method to obtain the events name and start/end time
        if (mWorkout == null){
            s_eventName = eventNameET.getText().toString();
        }
        s_eventDate = eventDateET.getText().toString();
        startTime = eventStartTimeEdit.getText().toString();
        stopTime = eventEndTimeEdit.getText().toString();

        if (TextUtils.isEmpty(s_eventName)){
            eventNameET.setError("Event name is required");
            return;
        }

        if (TextUtils.isEmpty(s_eventDate)){
            eventDateET.setError("Date is required");
            return;
        }

        //TODO make safety feature if time format wrong pretty
        if (TextUtils.isEmpty(startTime)){
            eventStartTimeEdit.setError("Start time is required");
            return;
        }

        if (TextUtils.isEmpty(stopTime)){
            eventEndTimeEdit.setError("End time is required");
        }
    }

    private void saveEvent() {
        //method to save the name, start and end time of an event into a list, called by save button
        setEventInApp();
        createEventInApp();
    }
    //TODO delete event

    private void saveEventToCalendar() {
        // method to save the event in the calendar in the app and in the calendar provider, called by export buttom
        setEventInApp();
        exportToExternalCalendar();
        createEventInApp();
    }

    private void createEventInApp() {
        // method to create the event and output it in the app
        try {
            mEventVM.addNewEvent(s_eventName, buildDate(s_eventDate), LocalTime.parse(startTime), LocalTime.parse(stopTime));
            Toast.makeText(getApplicationContext(), "event saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "no event info added", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToExternalCalendar() {
        try {
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(CalendarUtils.exportYear(buildDate(s_eventDate)), CalendarUtils.exportMonth(buildDate(s_eventDate)),
                    CalendarUtils.exportDay(buildDate(s_eventDate)), CalendarUtils.exportHours(startTime), CalendarUtils.exportMinutes(startTime));
            Calendar endTime = Calendar.getInstance();
            endTime.set(CalendarUtils.exportYear(buildDate(s_eventDate)), CalendarUtils.exportMonth(buildDate(s_eventDate)),
                    CalendarUtils.exportDay(buildDate(s_eventDate)), CalendarUtils.exportHours(stopTime), CalendarUtils.exportMinutes(stopTime));
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, s_eventName)
                    .putExtra(CalendarContract.Events.DESCRIPTION, "Group class"); //TODO: add WO description here
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "couldn't export event", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClick(View v) {
        saveEventToCalendar();
    }
}
