package kth.jjve.xfran.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kth.jjve.xfran.R;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.models.Workout;
import kth.jjve.xfran.utils.ResultUtils;
import kth.jjve.xfran.utils.WorkoutUtils;

public class ResultsWODRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();

    private Context mContext;
    private List<Result> mResults = new ArrayList<>();

    public ResultsWODRecyclerAdapter(Context context, List<Result> results) {
        mResults = results;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_wod_item, viewGroup, false);
        return new ResultsWODRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

        ((ViewHolder) vh).mScaled.setText(ResultUtils.scalingString(mResults.get(i)));
        ((ViewHolder) vh).mFeelScore.setText(ResultUtils.feelScoreString(mResults.get(i)));
        ((ViewHolder) vh).mComments.setText(ResultUtils.commentsString(mResults.get(i)));
        ((ViewHolder) vh).mDate.setText(ResultUtils.dateString(mResults.get(i)));
        ((ViewHolder) vh).mScore.setText(mResults.get(i).getScore()); // Todo: add score interpreter

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        /*------ HOOKS ------*/
        private TextView mDescription, mExercises, mScaled, mFeelScore, mComments, mDate, mScore;
        private View mExpandedView;
        private Button planButton, viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*------ HOOKS ------*/
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

        }
    }
}
