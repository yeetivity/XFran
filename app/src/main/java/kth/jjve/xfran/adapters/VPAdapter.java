package kth.jjve.xfran.adapters;


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
            return new ResultListFragment();
        }
        return new ResultStatisticsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
