package kth.jjve.xfran;

/*
Activity to set out the weekly calendar view and output the events
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.calendar.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import kth.jjve.xfran.adapters.CalendarAdapter;
import kth.jjve.xfran.adapters.MontlyCalendarAdapter;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.calendar.CalendarUtils;

public class MontlyCalendarActivity extends BaseActivity implements MontlyCalendarAdapter.OnItemListener {

    /*_________ VIEW _________*/
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    /*_________ INTENT _________*/
    private Integer position;
    private Workout mWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_calendar_month, contentFrameLayout);
        navigationView.setCheckedItem(R.id.nav_calendar);

        /*------ HOOKS ------*/
        ImageButton buttonBack = findViewById(R.id.buttonBack_month);
        ImageButton buttonNext = findViewById(R.id.buttonNext_month);
        monthYearText = findViewById(R.id.monthYearTV_month);
        calendarRecyclerView = findViewById(R.id.calendarMonthRecyclerView);

        /*----- CALENDAR ------*/
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        /*------ INTENT ------*/


        /*-------- LISTENERS ------------*/
        buttonBack.setOnClickListener(v -> { //go to previous week
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
            setMonthView();

        });

        buttonNext.setOnClickListener(v -> { // go to next week
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            setMonthView();
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    private void setMonthView() {
        // makes the week view with the correct days visible and sets events in the recycler view
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate)); //outputs month and year
        ArrayList<String> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate);

        MontlyCalendarAdapter montlyCalendarAdapter = new MontlyCalendarAdapter(daysInMonth, this);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(lm);
        calendarRecyclerView.setAdapter(montlyCalendarAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")){
            String message = "Selected Date " + dayText + " " + monthYearFromDate(CalendarUtils.selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
