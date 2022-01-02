package kth.jjve.xfran;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.text.TextUtils;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import kth.jjve.xfran.models.UserProfile;
import kth.jjve.xfran.viewmodels.UserProfileVM;

public class EditProfileActivity extends BaseActivity {
    private EditText mFirstName, mLastName, mWeight, mHeight;
    private TextView mEmail;
    private UserProfileVM mUserProfileVM;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.act_profile_edit, contentFrameLayout);
        navigationView.setCheckedItem(R.id.nav_profile);

        /*----- HOOKS -----*/
        mFirstName = findViewById(R.id.editProfileFirstName);
        mLastName = findViewById(R.id.editProfileLastName);
        mWeight = findViewById(R.id.editProfileWeight);
        mHeight = findViewById(R.id.editProfileHeight);
        mEmail = findViewById(R.id.editProfileEmail);
        ImageButton save = findViewById(R.id.saveProfile);

        /*-----  VM  -----*/
        mUserProfileVM = ViewModelProviders.of(this).get(UserProfileVM.class);
        mUserProfileVM.init();
        mUserProfileVM.getUserProfile().observe(this, this::setViews);

        /*--- OBSERVER ---*/
        save.setOnClickListener(v -> saveUserProfile());
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    @SuppressLint("SetTextI18n")
    private void setViews(UserProfile userProfile) {
        if (!userProfile.checkEmpty()) {
            mFirstName.setText(userProfile.getFirstName());
            mLastName.setText(userProfile.getLastName());
            mEmail.setText(userProfile.getEmail());
            mWeight.setText(Double.toString(userProfile.getWeight()));
            mHeight.setText(Integer.toString(userProfile.getHeight()));
        }
    }

    private void saveUserProfile() {
        String firstname = mFirstName.getText().toString();
        String lastname = mLastName.getText().toString();

        // Since a double/int can't be directly read from a edittext,
        // it is first read as string and then parsed
        String w = mWeight.getText().toString();
        String l = mHeight.getText().toString();
        double weight = Double.parseDouble(w);
        int height = Integer.parseInt(l);

        if (TextUtils.isEmpty(firstname)) {
            mFirstName.setError("Firstname is required");
            return;
        }

        if (TextUtils.isEmpty(lastname)) {
            mLastName.setError("Lastname is required");
            return;
        }

        try {
            mUserProfileVM.setUserProfile(
                    firstname,
                    lastname,
                    mEmail.getText().toString(),
                    weight,
                    height);

            Toast.makeText(getApplicationContext(), "User profile saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Edittexts cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}