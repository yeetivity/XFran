package kth.jjve.xfran.adapters;

/*
Function: Adapter to display the two fragments in a slider tab view
Used by: ResultActivity
*/

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kth.jjve.xfran.ResultListFragment;
import kth.jjve.xfran.ResultStatisticsFragment;

public class VPAdapter extends FragmentStateAdapter {
    public VPAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment

        if (position == 0) {
            // This displays the list of results in the left tab (pos =0)
            return new ResultListFragment();
        }
        // This displays the statisticsfragment in the right tab (pos != 0)
        return new ResultStatisticsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
