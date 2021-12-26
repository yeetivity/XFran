package kth.jjve.xfran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import kth.jjve.xfran.viewmodels.UserProfileVM;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText mFirstName, mLastName, mWeight, mLength;
    private TextView mEmail;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /*----- HOOKS -----*/
        drawerLayout = findViewById(R.id.drawer_layout_editprofile);
        navigationView = findViewById(R.id.nav_view_editprofile);
        toolbar = findViewById(R.id.editprofile_toolbar);

        mFirstName = findViewById(R.id.editProfileFirstName);
        mLastName = findViewById(R.id.editProfileLastName);
        mWeight = findViewById(R.id.editProfileWeight);
        mLength = findViewById(R.id.editProfileLength);
        mEmail = findViewById(R.id.editProfileEmail);
        ImageButton save = findViewById(R.id.saveProfile);

        /*-----  VM  -----*/
        //Todo: check if we can somehow inherit this between editprofile and normal profile
        UserProfileVM mUserProfileVM = ViewModelProviders.of(this).get(UserProfileVM.class);
        mUserProfileVM.init();
        mUserProfileVM.getUserProfile().observe(this, uP -> {
            if (!uP.checkEmpty()){
                mFirstName.setText(uP.getFirstName());
                mLastName.setText(uP.getLastName());
                mEmail.setText(uP.getEmail());
                mWeight.setText(Double.toString(uP.getWeight()));
                mLength.setText(Integer.toString(uP.getLength()));
            }
        });

        /*--- OBSERVER ---*/
        save.setOnClickListener(v -> {
            // Since a double/int can't be directly read from a edittext,
            // it is first read as string and then parsed
            try {
                String w = mWeight.getText().toString();
                String l = mLength.getText().toString();
                double weight = Double.parseDouble(w);
                int length = Integer.parseInt(l);

                mUserProfileVM.setUserProfile(
                        mFirstName.getText().toString(),
                        mLastName.getText().toString(),
                        mEmail.getText().toString(),
                        weight,
                        length);

                Toast.makeText(getApplicationContext(), "User profile saved", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Edittexts cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        /*----- INIT -----*/
        setSupportActionBar(toolbar);
        initNavMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initNavMenu() {
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(EditProfileActivity.this, HomeScreenActivity.class));
            finish();
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(EditProfileActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_logout) {
            //Todo: make sure that logout works everywhere
            Toast.makeText(EditProfileActivity.this, "Log out only in homescreen", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditProfileActivity.this, HomeScreenActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Todo: make login invisible
}