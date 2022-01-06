package kth.jjve.xfran.adapters;

/*
Function:
Activity: CalendarViewActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
based on: https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import kth.jjve.xfran.R;
import kth.jjve.xfran.models.EventInApp;
import kth.jjve.xfran.weeklycalendar.CalendarUtils;

public class EventAdapter extends ArrayAdapter<EventInApp> {


    public EventAdapter(@NonNull Context context, List<EventInApp> eventInApps) {
        super(context, 0, eventInApps);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventInApp eventInApp = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        String eventTitle = eventInApp.getName() + " " + CalendarUtils.cleanTime(eventInApp.getStartTime()) + " - " + CalendarUtils.cleanTime(eventInApp.getEndTime());
        eventCellTV.setText(eventTitle); // output in recyclerview
        return convertView;
    }

}
