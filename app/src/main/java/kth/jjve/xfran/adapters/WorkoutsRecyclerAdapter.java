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
    private Context mContext;
    //data
    private List<Workout> mWorkouts = new ArrayList<>();
    //ArrayList saves a boolean for each workout item (false-->collapsed view/ true-->expanded view)
    //The position on the array corresponds to the position on the mWorkouts list
    //This is built locally not to interact with the repo (unnecessary mix of concerns)
    private ArrayList<Boolean> mExpandedStatus = new ArrayList<>();

    /*------ LISTENERS ------*/
    //listeners to interact with activity
    //expand/collapse workout item
    private final ListItemClickListener mItemClickListener;
    //plan workout
    private final PlanButtonClickListener mPlanClickListener;
    //save result for the workout
    private final SaveButtonClickListener mSaveClickListener;

    public interface ListItemClickListener{
        //interface to expand workout item
        void onListItemClick(int position);
    }

    public interface PlanButtonClickListener{
        //interface to plan the workout
        void onPlanButtonClick(int position);
    }

    public interface SaveButtonClickListener{
        //interface to save the workout
        void onSaveButtonClick(int position);
    }

    public WorkoutsRecyclerAdapter(Context context, List<Workout> workouts,
                                   ListItemClickListener onItemClickListener,
                                   PlanButtonClickListener onPlanButtonClickListener,
                                   SaveButtonClickListener onSaveButtonClickListener) {
        //adapter constructor receives the workout list and the listeners
        mWorkouts = workouts;
        mContext = context;
        //set all workout items to collapsed view when adapter is constructed (ArrayList of false)
        mExpandedStatus.clear();
        mExpandedStatus.addAll(Collections.nCopies(mWorkouts.size(), false));
        //initialize listeners
        this.mItemClickListener = onItemClickListener;
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

        //Set the name of the 'Workout'
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

        //Set workout item view to collapsed/expanded according to ArrayList value
        boolean expanded = mExpandedStatus.get(i);
        ((ViewHolder) vh).mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
        mExpandedStatus.set(i, (!expanded));
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        /*------ HOOKS ------*/
        private TextView mName;
        private TextView mDescription;
        private View mExpandedView;
        private TextView mExercises;
        private Button planButton;
        private Button saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*------ HOOKS ------*/
            mName = itemView.findViewById(R.id.workout_name);
            mDescription = itemView.findViewById(R.id.workout_description);
            //item_expanded is a layout inside workout item containing the details of the workout
            mExpandedView = itemView.findViewById(R.id.item_expanded);
            mExercises = itemView.findViewById(R.id.workout_exercises);
            planButton = itemView.findViewById(R.id.button_plan_wod);
            saveButton = itemView.findViewById(R.id.button_save_wod);

            /*------ LISTENERS ------*/
            //listener for item click
            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    //get position of clicked item
                    int position = getAdapterPosition();
                    mItemClickListener.onListItemClick(position);
                    Log.i(LOG_TAG, "clicked: " + position);
                    //update UI and save new expanded status
                    boolean expanded = mExpandedStatus.get(position);
                    mExpandedView.setVisibility(expanded ? View.VISIBLE : View.GONE);
                    mExpandedStatus.set(position, (!expanded));
                }
            });

            //listener for plan button click
            planButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mPlanClickListener.onPlanButtonClick(position);
                    Log.i(LOG_TAG, "you clicked plan in WOD "+position);
                }
            });

            //listener for save results click
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