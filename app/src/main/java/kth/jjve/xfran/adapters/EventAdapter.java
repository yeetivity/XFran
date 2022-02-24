package kth.jjve.xfran.adapters;

/*
Function:
Used by: EventActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.R;
import kth.jjve.xfran.models.EventInApp;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AddResultButtonClickListener mPlanClickListener;
    private List<EventInApp> mEvents = new ArrayList<>();

    public EventAdapter(@NonNull Context context, List<EventInApp> eventInApps, AddResultButtonClickListener mPlanClickListener) {

        mEvents = eventInApps;
        this.mPlanClickListener = mPlanClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_event, viewGroup, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        String s_event = mEvents.get(position).getName() + " " +  mEvents.get(position).getStartTime() + " - " + mEvents.get(position).getEndTime();
        ((ViewHolder) vh).eventCellTV.setText(s_event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventCellTV;
        Button saveResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            /* ----- HOOKS ------ */
            eventCellTV = itemView.findViewById(R.id.eventCellTV);
            saveResult = itemView.findViewById(R.id.add_result);

            saveResult.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mPlanClickListener.onAddResultButtonClick(position);
            });
        }
    }

    public interface AddResultButtonClickListener {
        void onAddResultButtonClick(int position);
    }


}
