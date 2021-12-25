package kth.jjve.xfran;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Event;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class EditEventActivity extends AppCompatActivity
{
    private EditText eventName, eventStartTimeEdit, eventEndTimeEdit;
    private TextView eventDate, eventStartTime, eventEndTime;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        /*------ HOOKS ------*/
        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.eventDate);
        eventStartTime = findViewById(R.id.eventStartTime);
        eventEndTime = findViewById(R.id.eventEndTime);
        eventStartTimeEdit = findViewById(R.id.eventStartTimeEdit);
        eventEndTimeEdit = findViewById(R.id.eventEndTimeEdit);
        Button buttonSave = findViewById(R.id.eventSave);

        time = LocalTime.now();
        String s_date = "Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        eventDate.setText(s_date);

        /*-------- LISTENERS ------------*/
        buttonSave.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "event saved", Toast.LENGTH_SHORT).show();
            String s_eventName = eventName.getText().toString();
            String startTime = eventStartTimeEdit.getText().toString();
            String endTime = eventEndTimeEdit.getText().toString();
            Event newEvent = new Event(s_eventName, CalendarUtils.selectedDate, LocalTime.parse(startTime), LocalTime.parse(endTime));
            Event.eventsList.add(newEvent);
            finish();
        });

        //TODO delete event
        //TODO save events into repository

    }

}
