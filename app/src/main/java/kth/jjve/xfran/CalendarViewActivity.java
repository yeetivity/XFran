package kth.jjve.xfran;

/*
Activity to set out the weekly calendar view and output the events
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.WorkoutsListActivity.WORKOUT_ID;
import static kth.jjve.xfran.WorkoutsListActivity.WORKOUT_OBJ;
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
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.viewmodels.EventVM;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class CalendarViewActivity extends BaseActivity implements CalendarAdapter.OnItemListener {

    /*_________ VIEW _________*/
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    /*_________ INTENT _________*/
    private Integer position;
    private Workout mWorkout;

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
        setWeekView(LocalDate.now());

        /*------ INTENT ------*/
        Intent intentIn = getIntent();
        position = intentIn.getIntExtra(WORKOUT_ID,1);
        mWorkout = (Workout) intentIn.getSerializableExtra(WORKOUT_OBJ);

        /*-------- LISTENERS ------------*/
        buttonBack.setOnClickListener(v -> { //go to previous week
            setWeekView(CalendarUtils.selectedDate.minusWeeks(1));
        });

        buttonNext.setOnClickListener(v -> { // go to next week
            setWeekView(CalendarUtils.selectedDate.plusWeeks(1));
        });

        buttonNewEvent.setOnClickListener(v -> {
            startActivity(new Intent(this, PlanEventActivity.class));
            Intent intent = new Intent(this, PlanEventActivity.class);
            intent.putExtra(WORKOUT_ID, position);
            intent.putExtra(WORKOUT_OBJ, mWorkout);
            startActivity(intent);
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        setWeekView(date);
    }

    private void setWeekView(LocalDate date) {
        // makes the week view with the correct days visible and sets events in the recycler view

        CalendarUtils.selectedDate = date; // update the selected date
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate)); // shows user current month and year

        // find which days are in the week the user is looking at
        ArrayList<LocalDate> daysInWeekArray = daysInWeekArray(CalendarUtils.selectedDate);
        // use a grid layout with width 7 (7 days in a week)
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
        // set the adapter using the days that are in the particular week
        calendarRecyclerView.setAdapter(new CalendarAdapter(daysInWeekArray, this));

        // clear the list of planned workouts
        eventListView.setAdapter(new EventAdapter(getApplicationContext(), new ArrayList<>()));

        // using the view model
        EventVM eventVM = ViewModelProviders.of(this).get(EventVM.class);
        eventVM.init(CalendarUtils.selectedDate);
        eventVM.getEvents().observe(this, this::setRecyclerView);
    }

    private void setRecyclerView(List<EventInApp> eventList) {
        // convert the list into an arraylist
        ArrayList<EventInApp> dailyEventInApps = new ArrayList<>(eventList);

        // set the adapter, using a list of planned events
        eventListView.setAdapter(new EventAdapter(getApplicationContext(), dailyEventInApps));
    }


}
