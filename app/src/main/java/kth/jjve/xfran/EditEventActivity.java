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
    private EditText eventName;
    private TextView eventDate, eventTime;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        /*------ HOOKS ------*/
        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        Button buttonSave = findViewById(R.id.eventSave);

        time = LocalTime.now();
        String s_date = "Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        String s_time = "Time: " + CalendarUtils.formattedTime(time);
        eventDate.setText(s_date);
        eventTime.setText(s_time);

        /*-------- LISTENERS ------------*/
        buttonSave.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "event saved", Toast.LENGTH_SHORT).show();
            String s_eventName = eventName.getText().toString();
            Event newEvent = new Event(s_eventName, CalendarUtils.selectedDate, time);
            Event.eventsList.add(newEvent);
            finish();
        });

        //TODO edit time
        //TODO delete event
        //TODO save events into repository

    }

}
