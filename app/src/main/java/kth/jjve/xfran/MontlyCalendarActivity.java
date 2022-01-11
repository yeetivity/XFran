package kth.jjve.xfran;

/*
Activity to set out the weekly calendar view and output the events
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import static kth.jjve.xfran.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import kth.jjve.xfran.adapters.MonthlyCalendarAdapter;
import kth.jjve.xfran.utils.CalendarUtils;
import kth.jjve.xfran.viewmodels.CalendarVM;

public class MontlyCalendarActivity extends BaseActivity implements MonthlyCalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    private ArrayList<Integer> workoutDays;
    private ArrayList<Integer> feelScores;
    private CalendarVM calendarVM;

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
        calendarVM = ViewModelProviders.of(this).get(CalendarVM.class);
        setMonthView(LocalDate.now());
        calendarVM.getWorkoutDays().observe(this, results -> {
            workoutDays = results.get(0);
            feelScores = results.get(1);
            setMonthView(CalendarUtils.selectedDate);
        });

        /*-------- LISTENERS ------------*/
        buttonBack.setOnClickListener(v -> setMonthView(CalendarUtils.selectedDate.minusMonths(1)));
        buttonNext.setOnClickListener(v -> setMonthView(CalendarUtils.selectedDate.plusMonths(1)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    private void setMonthView(LocalDate date) {
        calendarVM.init(date);

        CalendarUtils.selectedDate = date; // update the selected date
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate)); // shows user month and year

        // find which days are in the month, and when empty cells need to be published
        ArrayList<String> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate);

        // use a grid layout with width 7
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
        // set the adapter based on days in the month
        calendarRecyclerView.setAdapter(new MonthlyCalendarAdapter(daysInMonth, this, workoutDays, feelScores));
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(CalendarUtils.selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
