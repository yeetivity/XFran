package kth.jjve.xfran;
/*
Activity to let the user register
Todo: See if an MVVM structure is really needed here
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_register);

        /*------------ HOOKS ------------*/
        mFullName = findViewById(R.id.reg_fullName);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mRegisterBtn = findViewById(R.id.reg_btnRegister);
        mLoginBtn = findViewById(R.id.reg_login);
        progressBar = findViewById(R.id.progressBar);

        /*------------ FBASE ------------*/
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
            finish();
        }
        /*------------ REGISTER ------------*/
        mRegisterBtn.setOnClickListener(v -> {
            //Todo: check where this needs to be in MVVM structure
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required");
                return;
            }

            if (password.length() < 6) {
                mPassword.setError("Password must be at least 6 characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // register the user in firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Error !" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        // send the user to login
        mLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));

    }
}