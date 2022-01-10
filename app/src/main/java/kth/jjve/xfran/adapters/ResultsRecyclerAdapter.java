package kth.jjve.xfran.adapters;
/*
Adapter for Results list
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.utils.ResultUtils;
import kth.jjve.xfran.utils.WorkoutUtils;

public class ResultsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();
    /*------ LISTENERS ------*/
    private final ResultsRecyclerAdapter.ListItemClickListener mItemClickListener;
    private final ResultsRecyclerAdapter.PlanButtonClickListener mPlanClickListener;
    private final ResultsRecyclerAdapter.SaveButtonClickListener mViewClickListener;
    private Context mContext;
    private List<Result> mResults = new ArrayList<>();
    /*
    ArrayList saves a boolean for each result item (false-->collapsed view/ true-->expanded view)
    The position on the array corresponds to the position on the mResults list
     */
    private ArrayList<Boolean> mExpandedStatus = new ArrayList<>();

    public ResultsRecyclerAdapter(Context context, List<Result> results,
                                  ResultsRecyclerAdapter.ListItemClickListener onItemClickListener,
                                  ResultsRecyclerAdapter.PlanButtonClickListener onPlanButtonClickListener,
                                  ResultsRecyclerAdapter.SaveButtonClickListener onViewButtonClickListener) {
        mResults = results;
        mContext = context;
        //set all result items to collapsed view when adapter is constructed
        mExpandedStatus.clear();
        try {
            mExpandedStatus.addAll(Collections.nCopies(mResults.size(), true));
        } catch (Exception e) {
            Toast.makeText(AppCtx.getContext(), "No results available", Toast.LENGTH_SHORT).show();
        }

        this.mItemClickListener = onItemClickListener;
        this.mPlanClickListener = onPlanButtonClickListener;
        this.mViewClickListener = onViewButtonClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_item, viewGroup, false);
        return new ResultsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

        ((ViewHolder) vh).mName.setText(mResults.get(i).getWorkout().getTitle());
        ((ViewHolder) vh).mDescription.setText(mResults.get(i).getWorkout().getDescription());
        ((ViewHolder) vh).mExercises.setText(WorkoutUtils.buildExerciseString(mResults.get(i).getWorkout()));
        ((ViewHolder) vh).mScaled.setText(ResultUtils.scalingString(mResults.get(i)));
        ((ViewHolder) vh).mFeelScore.setText(ResultUtils.feelScoreString(mResults.get(i)));
        ((ViewHolder) vh).mComments.setText(ResultUtils.commentsString(mResults.get(i)));
        ((ViewHolder) vh).mDate.setText(ResultUtils.dateString(mResults.get(i)));
        ((ViewHolder) vh).mScore.setText(mResults.get(i).getScore()); // Todo: add score interpreter

    }

    @Override
    public int getItemCount() {
        try {
            return mResults.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public interface ListItemClickListener {
        //interface to expand result item
        void onListItemClick(int position);
    }

    public interface PlanButtonClickListener {
        void onPlanButtonClick(int position);
    }

    public interface SaveButtonClickListener {
        void onSaveButtonClick(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        /*------ HOOKS ------*/
        private TextView mName, mDescription, mExercises, mScaled, mFeelScore, mComments, mDate, mScore;
        private View mExpandedView;
        private Button planButton, viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*------ HOOKS ------*/
            mName = itemView.findViewById(R.id.workout_name);
            mDescription = itemView.findViewById(R.id.workout_description);
            mExpandedView = itemView.findViewById(R.id.item_expanded);
            mExercises = itemView.findViewById(R.id.workout_exercises);
            mScaled = itemView.findViewById(R.id.result_scaling);
            mFeelScore = itemView.findViewById(R.id.result_feel_score);
            mComments = itemView.findViewById(R.id.result_comments);
            mDate = itemView.findViewById(R.id.result_date);
            mScore = itemView.findViewById(R.id.result_score);
            planButton = itemView.findViewById(R.id.button_plan_wod);
            viewButton = itemView.findViewById(R.id.button_view_results);

            mExpandedView.setVisibility(View.GONE);

            /*------ LISTENERS ------*/
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

            viewButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                mViewClickListener.onSaveButtonClick(position);
            });
        }
    }
}
