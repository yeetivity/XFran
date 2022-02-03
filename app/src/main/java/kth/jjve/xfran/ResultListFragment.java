package kth.jjve.xfran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kth.jjve.xfran.adapters.ResultsRecyclerAdapter;
import kth.jjve.xfran.viewmodels.ResultVM;

public class ResultListFragment extends Fragment {

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
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}