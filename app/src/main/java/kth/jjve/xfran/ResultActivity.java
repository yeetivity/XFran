package kth.jjve.xfran;

/*
Function:   Activity that shows two fragments: result list and result statistics
            Displays all possible results in all possible formats to the user
Uses:       ResultsListFragment, ResultsStatisticsFragment
Jitse van Esch, Mariah Sabioni & Elisa Perini
 */

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import kth.jjve.xfran.adapters.VPAdapter;

public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the result layout in the set frame of the base activity
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_result, contentFrameLayout);

        // Get the hooks needed for the tab layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.result_viewpager); //The VP allows sliding between the tabs

        // Initialise the viewpager adapter
        viewPager2.setAdapter(new VPAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
            //The tab layout mediator allows switching between the fragments and changing of tab text
                    if (position == 0) {
                        tab.setText("Result List");
                    } else {
                        tab.setText("Statistics");
                    }
                }).attach();
    }
}