package kth.jjve.xfran.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import kth.jjve.xfran.MontlyCalendarActivity;
import kth.jjve.xfran.R;
import kth.jjve.xfran.adapters.CalendarAdapter;
import kth.jjve.xfran.adapters.MontlyCalendarAdapter;

public class MontlyCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView dayOfMonth;
    private final MontlyCalendarAdapter.OnItemListener onItemListener;

    public MontlyCalendarViewHolder(@NonNull View itemView, MontlyCalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
