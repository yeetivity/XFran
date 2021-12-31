package kth.jjve.xfran;

/*
Activity to set out the weekly calendar view and output the events
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.weeklycalendar.CalendarUtils.daysInWeekArray;
import static kth.jjve.xfran.weeklycalendar.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;

import kth.jjve.xfran.adapters.EventAdapter;
import kth.jjve.xfran.models.Event;
import kth.jjve.xfran.adapters.CalendarAdapter;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class CalendarViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, NavigationView.OnNavigationItemSelectedListener {

    /*_________ VIEW _________*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklycalendar);

        /*------ HOOKS ------*/
        drawerLayout = findViewById(R.id.drawer_layout_weeklycalendar);
        navigationView = findViewById(R.id.nav_view_weeklycalendar);
        toolbar = findViewById(R.id.weeklycalendar_toolbar);
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonNext = findViewById(R.id.buttonNext);
        Button buttonNewEvent = findViewById(R.id.newEvent);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);

        /*------ INIT ------*/
        setSupportActionBar(toolbar);   // Initialise toolbar
        initNavMenu();                  // Initialise nav menu

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
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }


    //TODO can this one be public in one of the classes?
    private void initNavMenu(){
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // menu
        int id = menuItem.getItemId();
        if (id == R.id.nav_home){
            finish();
        } else if (id == R.id.nav_settings){
            //Todo: create something towards settings
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
}
