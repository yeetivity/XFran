package kth.jjve.xfran.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Workout;

/**
 * Adapter for Workouts tab
 */

public class WorkoutsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();
    private List<Workout> mWorkouts = new ArrayList<>();
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public WorkoutsRecyclerAdapter(Context context, List<Workout> workouts, ListItemClickListener onClickListener) {
        mWorkouts = workouts;
        mContext = context;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

        // Set the name of the 'Workout'
        ((ViewHolder) vh).mName.setText(mWorkouts.get(i).getTitle());
        //Set the details of the 'Workout'
        ((ViewHolder) vh).mDescription.setText(mWorkouts.get(i).getType());

        //TODO make this a method
        boolean expanded = mWorkouts.get(i).isExpanded();
        ((ViewHolder) vh).mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
        mWorkouts.get(i).setExpanded(!expanded);
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName;
        private TextView mDescription;
        private View mExpandedView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.workout_name);
            mDescription = itemView.findViewById(R.id.workout_description);
            mExpandedView = itemView.findViewById(R.id.item_expanded);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
            Log.d(LOG_TAG, "clicked: "+position);

            //TODO make this a method (same thing as before)
            boolean expanded = mWorkouts.get(position).isExpanded();
            mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
            mWorkouts.get(position).setExpanded(!expanded);
        }

    }
}