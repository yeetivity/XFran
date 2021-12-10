package kth.jjve.xfran.adapters;

import android.content.Context;
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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Workout> mWorkouts = new ArrayList<>();
    private Context mContext;

    public RecyclerAdapter(Context context, List<Workout> workouts) {
        mWorkouts = workouts;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trainingdiary_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

        // Set the name of the 'Workout'
        ((ViewHolder) vh).mName.setText(mWorkouts.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.image_name);
        }
    }
}