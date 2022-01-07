package kth.jjve.xfran.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kth.jjve.xfran.R;
import kth.jjve.xfran.adapters.MonthlyCalendarAdapter;

public class MontlyCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView dayOfMonth;
    public final ImageView dotOfDay;
    private final MonthlyCalendarAdapter.OnItemListener onItemListener;

    public MontlyCalendarViewHolder(@NonNull View itemView, MonthlyCalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.month_cellDayText);
        dotOfDay = itemView.findViewById(R.id.month_dot);
        this.onItemListener = onItemListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
