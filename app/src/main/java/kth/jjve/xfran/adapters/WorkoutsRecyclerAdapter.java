package kth.jjve.xfran.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Workout;

/**
 * Adapter for Workouts tab
 */

public class WorkoutsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();
    private List<Workout> mWorkouts = new ArrayList<>();
    private ArrayList<Boolean> mExpandedStatus = new ArrayList<>();
    private Context mContext;
    final private ListItemClickListener mItemClickListener;
    final private PlanButtonClickListener mPlanClickListener;
    final private SaveButtonClickListener mSaveClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public interface PlanButtonClickListener{
        void onPlanButtonClick(int position);
    }

    public interface SaveButtonClickListener{
        void onSaveButtonClick(int position);
    }

    public WorkoutsRecyclerAdapter(Context context, List<Workout> workouts,
                                   ListItemClickListener onClickListener, PlanButtonClickListener onPlanButtonClickListener,
                                   SaveButtonClickListener onSaveButtonClickListener) {
        mWorkouts = workouts;
        mContext = context;
        //reset the ArrayList that saves the expanded status of the items
        mExpandedStatus.clear();
        mExpandedStatus.addAll(Collections.nCopies(mWorkouts.size(), false));
        Log.i(LOG_TAG, "mExpandedStatus is: "+mExpandedStatus);
        //initialize onclicklisteners
        this.mItemClickListener = onClickListener;
        this.mPlanClickListener = onPlanButtonClickListener;
        this.mSaveClickListener = onSaveButtonClickListener;
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
        //Set the exercises of the 'Workout'
        String exercises = "";
        ArrayList<String> exercisesArray = mWorkouts.get(i).getExercises();
        for (String s : exercisesArray){
            exercises += s + "\n";
        }
        ((ViewHolder) vh).mExercises.setText(exercises);

        boolean expanded = mExpandedStatus.get(i);
        ((ViewHolder) vh).mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
        mExpandedStatus.set(i, (!expanded));
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;
        private View mExpandedView;
        private TextView mExercises;
        private Button planButton;
        private Button saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.workout_name);
            mDescription = itemView.findViewById(R.id.workout_description);
            mExpandedView = itemView.findViewById(R.id.item_expanded);
            mExercises = itemView.findViewById(R.id.workout_exercises);

            planButton = itemView.findViewById(R.id.button_plan_wod);
            saveButton = itemView.findViewById(R.id.button_save_wod);


            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mItemClickListener.onListItemClick(position);
                    Log.d(LOG_TAG, "clicked: " + position);

                    boolean expanded = mExpandedStatus.get(position);
                    mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
                    mExpandedStatus.set(position, (!expanded));
                }
            });

            planButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mPlanClickListener.onPlanButtonClick(position);
                    Log.i(LOG_TAG, "you clicked plan in WOD "+position);
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mSaveClickListener.onSaveButtonClick(position);
                    Log.i(LOG_TAG, "you clicked save in WOD " +position);
                }
            });
        }

    }
}