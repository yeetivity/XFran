package kth.jjve.xfran;
/*
This activity is the view of the homepage of XFran.

Made by:
Jitse van Esch
Elisa Perini
Mariah Sabioni
 */

import android.os.Bundle;
import android.widget.FrameLayout;

public class HomeScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_home, contentFrameLayout);

        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }
}