package kth.jjve.xfran;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import kth.jjve.xfran.models.UserProfile;
import kth.jjve.xfran.viewmodels.UserProfileVM;

public class ProfileActivity extends BaseActivity {
    /*_________ VIEW _________*/
    private TextView mName, mEmail, mWeight, mHeight;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_profile, contentFrameLayout);
        navigationView.setCheckedItem(R.id.nav_profile);

        /*----- HOOKS -----*/
        mName = findViewById(R.id.profile_username);
        mEmail = findViewById(R.id.profile_useremail);
        mWeight = findViewById(R.id.profile_weight);
        mHeight = findViewById(R.id.profile_height);
        ImageButton edit = findViewById(R.id.profile_Edit);

        /*-----  VM  -----*/
        UserProfileVM mUserProfileVM = ViewModelProviders.of(this).get(UserProfileVM.class);
        mUserProfileVM.init();
        mUserProfileVM.getUserProfile().observe(this, this::setViews);

        /*--- OBSERVER ---*/
        edit.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    @SuppressLint("SetTextI18n")
    private void setViews(UserProfile userProfile){
        if (!userProfile.checkEmpty()){
            mName.setText(userProfile.getFullName());
            mEmail.setText(userProfile.getEmail());
            mWeight.setText(Double.toString(userProfile.getWeight()));
            mHeight.setText(Integer.toString(userProfile.getHeight()));
        }
    }
    //Todo: make login invisible
}