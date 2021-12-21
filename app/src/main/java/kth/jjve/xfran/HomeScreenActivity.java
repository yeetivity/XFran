package kth.jjve.xfran;
/*
This activity is only used for updating the user interface
getting data, handling data and such is all done somewhere else
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /*_________ VIEW _________*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Menu profileMenu;

    /*_________ FBASE _________*/
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        /*------ HOOKS ------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_toolbar);


        /*------ INIT ------*/
        setSupportActionBar(toolbar);   // Initialise toolbar
        initNavMenu();                  // Initialise nav menu

        /*------------ FBASE ------------*/
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            profileMenu.findItem(R.id.nav_login).setVisible(false);
            profileMenu.findItem(R.id.nav_profile).setVisible(true);
            profileMenu.findItem(R.id.nav_logout).setVisible(true);
        }

        /*------------ LISTENERS ------------*/


    }


    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_diary){
            startActivity(new Intent(HomeScreenActivity.this, TrainingDiaryActivity.class));
        } else if (id == R.id.nav_login){
            startActivity(new Intent(HomeScreenActivity.this, RegisterActivity.class));
        } else if (id == R.id.nav_logout){
            firebaseAuth.signOut();
            Toast.makeText(HomeScreenActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            profileMenu.findItem(R.id.nav_logout).setVisible(false);
            profileMenu.findItem(R.id.nav_login).setVisible(true);
            profileMenu.findItem(R.id.nav_profile).setVisible(false);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initNavMenu(){

        //Hide or show items
        profileMenu = navigationView.getMenu();
        profileMenu.findItem(R.id.nav_logout).setVisible(false);
        profileMenu.findItem(R.id.nav_profile).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

}