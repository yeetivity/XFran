package kth.jjve.xfran.adapters;

/*
Function: adapter for the weekly calendar
Activity: CalendarViewActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
based on: https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar
 */

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.MontlyCalendarActivity;
import kth.jjve.xfran.R;
import kth.jjve.xfran.calendar.CalendarUtils;
import kth.jjve.xfran.calendar.CalendarViewHolder;
import kth.jjve.xfran.calendar.MontlyCalendarViewHolder;
import kth.jjve.xfran.repositories.ResultRepo;

public class MontlyCalendarAdapter extends RecyclerView.Adapter<MontlyCalendarViewHolder>{

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public MontlyCalendarAdapter(ArrayList<String> days, OnItemListener onItemListener) {
        this.daysOfMonth = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MontlyCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.montly_calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666);
        return new MontlyCalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MontlyCalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        setDot(holder, position); // Decide if the dot has to be shown

    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface  OnItemListener {
        void onItemClick(int position, String dayText);
    }

    public void setDot(@NonNull MontlyCalendarViewHolder holder, int position){
        List<Integer> list = ResultRepo.getFakeDataList();

        if (!daysOfMonth.get(position).equals("")){
            if (list.contains(Integer.parseInt(daysOfMonth.get(position)))){
                holder.dotOfDay.setVisibility(View.VISIBLE);
            }
        }
    }
}
