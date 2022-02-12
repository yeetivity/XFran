package kth.jjve.xfran.adapters;
/*
Function: Adapter to show the recycler view with the list of workouts
Used by: WorkoutListActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
*/

//Todo: see if we can make all the recyclerview adapters to one class or a base class with extensions

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
import kth.jjve.xfran.utils.WorkoutUtils;

public class WorkoutsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();
    /*------ LISTENERS ------*/
    private final ListItemClickListener mItemClickListener;
    private final PlanButtonClickListener mPlanClickListener;
    private final SaveButtonClickListener mSaveClickListener;
    private final ViewButtonClickListener mViewClickListener;
    private Context mContext;
    private List<Workout> mWorkouts = new ArrayList<>();
    /*
    ArrayList saves a boolean for each workout item (false-->collapsed view/ true-->expanded view)
    The position on the array corresponds to the position on the mWorkouts list
     */
    private ArrayList<Boolean> mExpandedStatus = new ArrayList<>();

    public WorkoutsRecyclerAdapter(Context context, List<Workout> workouts,
                                   ListItemClickListener onItemClickListener,
                                   PlanButtonClickListener onPlanButtonClickListener,
                                   SaveButtonClickListener onSaveButtonClickListener,
                                   ViewButtonClickListener onViewButtonClickListener) {
        mWorkouts = workouts;
        mContext = context;
        //set all workout items to collapsed view when adapter is constructed
        mExpandedStatus.clear();
        mExpandedStatus.addAll(Collections.nCopies(mWorkouts.size(), true));

        this.mItemClickListener = onItemClickListener;
        this.mPlanClickListener = onPlanButtonClickListener;
        this.mSaveClickListener = onSaveButtonClickListener;
        this.mViewClickListener = onViewButtonClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_workout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

        ((ViewHolder) vh).mName.setText(mWorkouts.get(i).getTitle());
        ((ViewHolder) vh).mDescription.setText(mWorkouts.get(i).getDescription());
        String exercises = WorkoutUtils.buildExerciseString(mWorkouts.get(i));
        ((ViewHolder) vh).mExercises.setText(exercises);

    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    public interface ListItemClickListener {
        //interface to expand workout item
        void onListItemClick(int position);
    }

    public interface PlanButtonClickListener {
        void onPlanButtonClick(int position);
    }

    public interface SaveButtonClickListener {
        void onSaveButtonClick(int position);
    }

    public interface ViewButtonClickListener {
        void onViewButtonClick(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        /*------ HOOKS ------*/
        private TextView mName, mDescription, mExercises;
        private View mExpandedView;
        private Button planButton, saveButton, viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*------ HOOKS ------*/
            mName = itemView.findViewById(R.id.workout_name);
            mDescription = itemView.findViewById(R.id.workout_description);
            mExpandedView = itemView.findViewById(R.id.item_expanded);
            mExercises = itemView.findViewById(R.id.workout_exercises);
            planButton = itemView.findViewById(R.id.button_plan_wod);
            saveButton = itemView.findViewById(R.id.button_save_wod);
            viewButton = itemView.findViewById(R.id.button_view_wod_results);

            mExpandedView.setVisibility(View.GONE);

            /*------ LISTENERS ------*/
            //listener for item click
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mItemClickListener.onListItemClick(position);
                Log.i(LOG_TAG, "clicked: " + position);
                //update UI and save new expanded status
                boolean expanded = mExpandedStatus.get(position);
                mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
                mExpandedStatus.set(position, (!expanded));
            });

            planButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mPlanClickListener.onPlanButtonClick(position);
            });

            saveButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mSaveClickListener.onSaveButtonClick(position);
            });

            viewButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mViewClickListener.onViewButtonClick(position);
            });
        }
    }
}