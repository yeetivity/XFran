package kth.jjve.xfran;

/*
Activity to input and save the event
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

import kth.jjve.xfran.models.Event;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class EditEventActivity extends AppCompatActivity
{
    private EditText eventName, eventStartTimeEdit, eventEndTimeEdit;

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

        String s_date = "Date: " + CalendarUtils.cleanDate(CalendarUtils.selectedDate);
        eventDate.setText(s_date);

        /*-------- LISTENERS ------------*/
        buttonSave.setOnClickListener(v -> {
            saveEvent();
            finish();
        });
    }

    private void saveEvent(){
        //method to save the name, start and end time of an event into a list
        String s_eventName = eventName.getText().toString(); //given name
        String startTime = eventStartTimeEdit.getText().toString(); //start time
        String endTime = eventEndTimeEdit.getText().toString(); //end time
        //TODO add some safety feature if time format wrong, currently need ie 04:00
        Event newEvent = new Event(s_eventName, CalendarUtils.selectedDate, LocalTime.parse(startTime), LocalTime.parse(endTime));
        Event.eventsList.add(newEvent);
        Toast.makeText(getApplicationContext(), "event saved", Toast.LENGTH_SHORT).show();
    }

    //TODO delete event
    //TODO save events into repository

}
