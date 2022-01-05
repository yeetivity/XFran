package kth.jjve.xfran;

/*
Activity to set out the weekly calendar view and output the events
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.weeklycalendar.CalendarUtils.daysInWeekArray;
import static kth.jjve.xfran.weeklycalendar.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.adapters.EventAdapter;
import kth.jjve.xfran.models.EventInApp;
import kth.jjve.xfran.adapters.CalendarAdapter;
import kth.jjve.xfran.repositories.EventRepo;
import kth.jjve.xfran.viewmodels.EventVM;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class CalendarViewActivity extends BaseActivity implements CalendarAdapter.OnItemListener {

    /*_________ VIEW _________*/
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_calendar_week, contentFrameLayout);
        navigationView.setCheckedItem(R.id.nav_calendar);

        /*------ HOOKS ------*/
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        ImageButton buttonNext = findViewById(R.id.buttonNext);
        Button buttonNewEvent = findViewById(R.id.newEventInApp);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);

        /*----- CALENDAR ------*/
        CalendarUtils.selectedDate = LocalDate.now(); //get today's date
        setWeekView();

        /*-------- LISTENERS ------------*/
        buttonBack.setOnClickListener(v -> { //go to previous week
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
        });

        buttonNext.setOnClickListener(v -> { // go to next week
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();
        });

        buttonNewEvent.setOnClickListener(v -> {
            //TODO make it a popup window, not a new activity
            startActivity(new Intent(this, EditEventActivity.class));
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    private void setWeekView() {
        // makes the week view with the correct days visible and sets events in the recycler view
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate)); //outputs month and year
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter(); //events made visible
    }

    private void setEventAdapter() {
        // takes the day's events from the list and outputs them
        EventVM eventVM = ViewModelProviders.of(this).get(EventVM.class);
        eventVM.init(CalendarUtils.selectedDate);
        eventVM.getEvents().observe(this, this::setRecyclerView);
    }

    private void setRecyclerView(List<EventInApp> eventList) {
        ArrayList<EventInApp> dailyEventInApps = new ArrayList<EventInApp>(eventList);

        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEventInApps);
        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
}
