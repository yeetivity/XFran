package kth.jjve.xfran;

import static kth.jjve.xfran.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import kth.jjve.xfran.adapters.MonthlyCalendarAdapter;
import kth.jjve.xfran.utils.CalendarUtils;
import kth.jjve.xfran.viewmodels.CalendarVM;


public class ResultStatisticsFragment extends Fragment implements MonthlyCalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    private ArrayList<Integer> workoutDays;
    private ArrayList<Integer> feelScores;
    private CalendarVM calendarVM;

    public ResultStatisticsFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_statistics, container, false);

        /*------ HOOKS ------*/
        ImageButton buttonBack = view.findViewById(R.id.buttonBack_month);
        ImageButton buttonNext = view.findViewById(R.id.buttonNext_month);
        monthYearText = view.findViewById(R.id.monthYearTV_month);
        calendarRecyclerView = view.findViewById(R.id.calendarMonthRecyclerView);

        /*---- LISTENERS ----*/
        buttonBack.setOnClickListener(v -> setMonthView(CalendarUtils.selectedDate.minusMonths(1)));
        buttonNext.setOnClickListener(v -> setMonthView(CalendarUtils.selectedDate.plusMonths(1)));

        /*--- CALENDAR VM --*/
        calendarVM = ViewModelProviders.of(this).get(CalendarVM.class);
        setMonthView(LocalDate.now());
        calendarVM.getWorkoutDays().observe(getViewLifecycleOwner(), results -> {
            workoutDays = results.get(0);
            feelScores = results.get(1);
            setMonthView(CalendarUtils.selectedDate);
        });

        return view;
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(CalendarUtils.selectedDate);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void setMonthView(LocalDate date) {
        calendarVM.init(date);

        CalendarUtils.selectedDate = date; // update the selected date
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate)); // shows user month and year

        // find which days are in the month, and when empty cells need to be published
        ArrayList<String> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate);

        // use a grid layout with width 7
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        // set the adapter based on days in the month
        calendarRecyclerView.setAdapter(new MonthlyCalendarAdapter(daysInMonth, this, workoutDays, feelScores));
    }
}