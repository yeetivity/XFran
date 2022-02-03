package kth.jjve.xfran;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNav;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        /*------ HOOKS ------*/
        toolbar = findViewById(R.id.toolbar_layout);
        bottomNav = findViewById(R.id.bottomNav);

        /*------ INIT ------*/
        setSupportActionBar(toolbar);
        setBottomNavigation();

        /*----- FBASE -----*/
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) { // check if user is already logged in
            //Todo: Check something
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigation(){
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                    break;
                case R.id.nav_results:
                    startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                    break;
                case R.id.nav_add:
                    Toast.makeText(getApplicationContext(), "Under construction", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_calendar:
                    startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    break;
            }
            return false;
        });
    }
}

