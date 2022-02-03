package kth.jjve.xfran;

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

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_result, contentFrameLayout);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.result_viewpager);

        VPAdapter adapter = new VPAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Result List");
                    } else {
                        tab.setText("Statistics");
                    }
                }).attach();
    }
}