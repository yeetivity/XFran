package kth.jjve.xfran.adapters;

/*
Function: adapter for the weekly calendar
Activity: CalendarViewActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
based on: https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kth.jjve.xfran.R;
import kth.jjve.xfran.viewholders.MontlyCalendarViewHolder;

public class MonthlyCalendarAdapter extends RecyclerView.Adapter<MontlyCalendarViewHolder>{

    private final ArrayList<String> daysOfMonth;
    private final ArrayList<Integer> workoutDays, feelScores;
    private final OnItemListener onItemListener;

    public MonthlyCalendarAdapter(ArrayList<String> days, OnItemListener onItemListener, ArrayList<Integer> workoutdays, ArrayList<Integer> feelScores) {
        this.daysOfMonth = days;
        this.onItemListener = onItemListener;
        this.workoutDays = workoutdays;
        this.feelScores = feelScores;
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

        if (!daysOfMonth.get(position).equals("")){
            if (workoutDays != null){
                // Check if the day is in the list of workout days --> display dot
                if (workoutDays.contains(Integer.parseInt(daysOfMonth.get(position)))) {
                    holder.dotOfDay.setVisibility(View.VISIBLE);

                    // Check the feelScore for the planned workout day
                    int index = workoutDays.indexOf(Integer.parseInt(daysOfMonth.get(position)));
                    int feelScore = feelScores.get(index);

                    Drawable dot = holder.dotOfDay.getBackground();

                    // Alter the bg color of the dot based on the feelScore
                    if (feelScore == 0){ // planned activity --> grey
                        ((GradientDrawable) dot).setColor(Color.parseColor("#808080"));
                    } else if (feelScore == 1 || feelScore == 2){ // bad workout --> red
                        ((GradientDrawable) dot).setColor(Color.parseColor("#530000"));
                    } else if (feelScore == 3){ // mediocre workout --> yellow
                        ((GradientDrawable) dot).setColor(Color.parseColor("#FFCC00"));
                    } else if (feelScore == 4 || feelScore == 5){ // good workout --> green
                        ((GradientDrawable) dot).setColor(Color.parseColor("#006400"));
                    } else {
                        Log.i("MonthAdapter", "Wrong feelscore given");
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface  OnItemListener {
        void onItemClick(int position, String dayText);
    }
}
