package kth.jjve.xfran;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import kth.jjve.xfran.adapters.ResultsRecyclerAdapter;
import kth.jjve.xfran.models.Result;
import kth.jjve.xfran.viewmodels.ResultVM;

public class ResultListFragment extends Fragment implements ResultsRecyclerAdapter.ListItemClickListener{

    public ResultListFragment() {
        // Required empty constructor
    }

    private final String LOG_TAG = getClass().getSimpleName();

    /*------ VIEW MODEL ------*/
    private ResultVM mResultVM;
    private ResultsRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /*------ INTENT ------*/
    public static String WORKOUT_ID = "Workout ID";
    public static String WORKOUT_OBJ = "Workout Obj";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate the view model
        mResultVM = ViewModelProviders.of(this).get(ResultVM.class);
        mResultVM.init();
        mResultVM.getResults().observe(this, this::resetRecyclerView);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_list, container, false);

        // Create RecyclerView
        mRecyclerView = view.findViewById(R.id.recyclerViewResultList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(new ResultsRecyclerAdapter(
                mResultVM.getResults().getValue(), this, this::onPlan, this::onView));

        return view;
    }

    private void resetRecyclerView(List<Result> resultList){
        // Update recyclerView with fed data
        mRecyclerView.setAdapter(new ResultsRecyclerAdapter(
                resultList, this, this::onPlan, this::onView));
        Log.d(LOG_TAG, "new adapter set for new resultlist");
    }

    public void onPlan(int position) {
        //start activity to plan workout
        Intent intent = new Intent(getActivity(), EventPlanActivity.class);
        intent.putExtra(WORKOUT_ID, position);
        intent.putExtra(WORKOUT_OBJ, Objects.requireNonNull(Objects.requireNonNull(mResultVM.getResults().getValue()).get(position).getWorkout()));
        Log.i(LOG_TAG, "workout sent: " + mResultVM.getResults().getValue().get(position).getWorkout());
        startActivity(intent);
    }

    public void onView(int position) {
        //Todo: Do something here
        Toast.makeText(getActivity(), "Under construction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClick(int position) {

    }
}