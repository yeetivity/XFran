package kth.jjve.xfran;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.PopupMenu;

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
        setContentView(R.layout.activity_base);

        /*------ HOOKS ------*/
        toolbar = findViewById(R.id.toolbar_layout);
        bottomNav = findViewById(R.id.bottomNav);

        /*------ INIT ------*/
        setSupportActionBar(toolbar);
        setBottomNavigation();

        /*----- FBASE -----*/
        fAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigation(){
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    break;
                case R.id.nav_results:
                    startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                    break;
                case R.id.nav_add:
                    PopupMenu popup = new PopupMenu(getApplicationContext(), findViewById(R.id.nav_add));
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.add_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(item1 -> {
                        switch (item1.getItemId()){
                            case R.id.nav_add_event:
                                //Todo: Make sure that we can directly go to planning (now null error)
                                startActivity(new Intent(getApplicationContext(), EventPlanActivity.class));
                                break;
                            case R.id.nav_add_result:
                                //Todo: Make sure that we can direclty go to logging result
                                startActivity(new Intent(getApplicationContext(), WorkoutsListActivity.class));
                                break;
                        }
                        return false;
                    });
                    popup.show();
                    break;
                case R.id.nav_calendar:
                    startActivity(new Intent(getApplicationContext(), EventActivity.class));
                    break;
                case R.id.nav_profile:
                    if (fAuth.getCurrentUser() != null){
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), ProfileLoginActivity.class));
                    }
                    break;
            }
            return false;
        });
    }
}

