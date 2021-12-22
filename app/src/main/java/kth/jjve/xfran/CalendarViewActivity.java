package kth.jjve.xfran;

import static kth.jjve.xfran.weeklycalendar.CalendarUtils.daysInWeekArray;
import static kth.jjve.xfran.weeklycalendar.CalendarUtils.monthYearFromDate;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kth.jjve.xfran.weeklycalendar.CalendarAdapter;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class CalendarViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, NavigationView.OnNavigationItemSelectedListener {

    /*_________ VIEW _________*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CalendarView mCalendarView;

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

        /*------ INIT ------*/
        setSupportActionBar(toolbar);   // Initialise toolbar
        initNavMenu();                  // Initialise nav menu

        /*----- CALENDAR ------*/
        CalendarUtils.selectedDate = LocalDate.now(); //get today's date

        // Get a reference for the week view in the layout.
        mCalendarView = findViewById(R.id.calendarView);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        setWeekView();
    }


    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        //setEventAdapter();
    }
/*
    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    } */

    //TODO implement methods to go forward and backwards in calendar
    //TODO check sunday/monday



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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
}
