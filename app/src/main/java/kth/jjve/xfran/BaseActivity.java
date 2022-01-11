package kth.jjve.xfran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu profileMenu;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        /*------ HOOKS ------*/
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.base_drawer_layout);
        toolbar = findViewById(R.id.toolbar_layout);

        /*------ INIT ------*/
        setSupportActionBar(toolbar);

        profileMenu = navigationView.getMenu();
        profileMenu.findItem(R.id.nav_logout).setVisible(false);
        profileMenu.findItem(R.id.nav_profile).setVisible(false);

        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        /*----- FBASE -----*/
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) { // check if user is already logged in
            profileMenu.findItem(R.id.nav_login).setVisible(false);
            profileMenu.findItem(R.id.nav_profile).setVisible(true);
            profileMenu.findItem(R.id.nav_logout).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_workouts:
                startActivity(new Intent(getApplicationContext(), WorkoutsListActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_results:
                startActivity(new Intent(getApplicationContext(), ResultsListActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_calendar:
                startActivity(new Intent(getApplicationContext(), MontlyCalendarActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_plan_workout:
                startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_settings:
                Toast.makeText(getApplicationContext(), "Still under construction", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_logout:
                fAuth.signOut();
                profileMenu.findItem(R.id.nav_logout).setVisible(false);
                profileMenu.findItem(R.id.nav_profile).setVisible(false);
                profileMenu.findItem(R.id.nav_login).setVisible(true);
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}

