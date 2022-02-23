package kth.jjve.xfran;

/*
Activity to input and save the event
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.utils.CalendarUtils.ymdToLocalDate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
    private EditText eventNameET;
    private TextView eventStartTimeInput, eventEndTimeInput, eventDateInput;
    private String string_eventName, startTime, stopTime, string_eventDate;
    private EventVM mEventVM;

    /*_________ INTENT _________*/
    private Integer position;
    private Workout mWorkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_planevent, contentFrameLayout);

        /*------ HOOKS ------*/
        eventNameET = findViewById(R.id.eventNameET);
        TextView eventNameTV = findViewById(R.id.eventNameTV);
        TextView eventDate = findViewById(R.id.eventDate);
        eventStartTimeInput = findViewById(R.id.eventStartTimeInput);
        eventEndTimeInput = findViewById(R.id.eventEndTimeInput);
        eventDateInput = findViewById(R.id.eventDateInput);
        Button buttonSave = findViewById(R.id.eventSave);
        Button buttonClose = findViewById(R.id.close);
        Button buttonExport = findViewById(R.id.savetogoogle);

        /*----- CALENDAR ------*/
        String string_date = "Date:";
        eventDate.setText(string_date);

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
            string_eventName = mWorkout.getTitle();
            eventNameTV.setText(string_eventName);
        }

        /*------ DATE & TIME PICKER DIALOGS -----*/
        eventDateInput.setOnClickListener(v -> datePickerDialog());
        eventStartTimeInput.setOnClickListener(v -> timePickerDialog(eventStartTimeInput));
        eventEndTimeInput.setOnClickListener(v -> timePickerDialog(eventEndTimeInput));

    }

    private void timePickerDialog(TextView textView){
        // opens time picker dialog
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(EventPlanActivity.this, (view, hourOfDay, minuteOfHour) -> {
            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
            textView.setText(time);
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.show();
    }

    private void datePickerDialog(){
        // calender class's instance and get current date , month and year from calender
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH); // ADD +1 to get actual month!!!
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventPlanActivity.this,
                (view, year, month, day) -> {
                    // set day of month , month and year value in the edit text
                    String date = day + "/"  + (month + 1) + "/" + year;
                    eventDateInput.setText(date);
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.show();
    }

    private void setEventInApp(){
        // method to obtain the events name and start/end time
        if (mWorkout == null){
            string_eventName = eventNameET.getText().toString();
        }
        string_eventDate = eventDateInput.getText().toString();
        startTime = eventStartTimeInput.getText().toString();
        stopTime = eventEndTimeInput.getText().toString();

        checkIfEmptyET(string_eventName, eventNameET);
        checkIfEmptyTV(string_eventDate, eventDateInput);
        checkIfEmptyTV(startTime, eventStartTimeInput);
        checkIfEmptyTV(stopTime, eventEndTimeInput);
    }

    //TODO understand why fail safe fails if no event name
    private void checkIfEmptyTV(String string, TextView textView){
        // checks if the textView is empty
        if (TextUtils.isEmpty(string)) {
            textView.setError("Required");
        }
        return;
    }
    private void checkIfEmptyET(String string, EditText editText){
        // checks if the editText is empty
        if (TextUtils.isEmpty(string)) {
            editText.setError("Required");
        }
        return;
    }

    private void saveEvent() {
        //method to save the name, start and end time of an event into a list, called by save button
        setEventInApp();
        createEventInApp();
    }
    //TODO delete event

    private void saveEventToCalendar() {
        // method to save the event in the calendar in the app and in the calendar provider, called by export button
        setEventInApp();
        exportToExternalCalendar();
        createEventInApp();
    }

    private void createEventInApp() {
        // method to create the event and output it in the app
        try {
            mEventVM.addNewEvent(string_eventName, ymdToLocalDate(string_eventDate), LocalTime.parse(startTime), LocalTime.parse(stopTime));
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
            beginTime.set(CalendarUtils.exportYear(ymdToLocalDate(string_eventDate)), CalendarUtils.exportMonth(ymdToLocalDate(string_eventDate)),
                    CalendarUtils.exportDay(ymdToLocalDate(string_eventDate)), CalendarUtils.exportHours(startTime), CalendarUtils.exportMinutes(startTime));
            Calendar endTime = Calendar.getInstance();
            endTime.set(CalendarUtils.exportYear(ymdToLocalDate(string_eventDate)), CalendarUtils.exportMonth(ymdToLocalDate(string_eventDate)),
                    CalendarUtils.exportDay(ymdToLocalDate(string_eventDate)), CalendarUtils.exportHours(stopTime), CalendarUtils.exportMinutes(stopTime));
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, string_eventName)
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
