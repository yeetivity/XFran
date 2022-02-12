package kth.jjve.xfran.repositories;
/*
Function: repository for userprofile
Used by:
Jitse van Esch, Mariah Sabioni & Elisa Perini
 */

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kth.jjve.xfran.models.UserProfile;

public class UserRepo {
    private static UserRepo instance;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    private UserProfile userProfile = new UserProfile();
    private MutableLiveData<UserProfile> up;
    private MutableLiveData<String> un;

    public static UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    public MutableLiveData<UserProfile> getUserProfile() {
        up = new MutableLiveData<>();
        initFirebase();

        if (userProfile.checkEmpty() || !userID.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
            if (firebaseAuth.getCurrentUser() != null) {
                //authenticate user
                userID = firebaseAuth.getCurrentUser().getUid();

                //get reference to user data
                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                documentReference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        // fill user profile with firestore data
                        try {
                            // First try to fill the complete profile
                            //noinspection ConstantConditions
                            userProfile = new UserProfile(
                                    document.getString("firstName"),
                                    document.getString("lastName"),
                                    document.getString("email"),
                                    document.getDouble("weight"),
                                    Objects.requireNonNull(document.getLong("height")).intValue());
                            up.setValue(userProfile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                // Try to fill the profile with only sign up values
                                userProfile = new UserProfile(
                                        Objects.requireNonNull(document.getString("fullName")),
                                        document.getString("email"));
                                up.setValue(userProfile);
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                // fill user profile with guest data
                Log.i("UserRepo", "Use a guest account");
                userProfile = new UserProfile("Guest", "1", "guest@email.com", 0.0, 0);
                up.setValue(userProfile);
            }
        }
        up.setValue(userProfile);

        return up;
    }

    public MutableLiveData<String> getUserName(){
        un = new MutableLiveData<>();
        initFirebase();

        if (firebaseAuth.getCurrentUser() != null){
            //authenticate user
            userID = firebaseAuth.getCurrentUser().getUid();

            //get reference to user data
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    // get username
                    try {
                        un.setValue(document.getString("firstName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("UserRep", "No firstName found");
                    }
                }
            });
        } else {
            un.setValue("not logged in");
        }
        return un;
    }

    private void initFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void setUserProfile(String fn, String ln, String e, double w, int l) {
        userProfile = new UserProfile(fn, ln, e, w, l);
        if (up != null) up.setValue(userProfile);   //Add the user profile to the mutable data list

        //Save the data to the database
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            userID = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            Map<String, Object> user = new HashMap<>();
            user.put("fullName", userProfile.getFullName());
            user.put("firstName", userProfile.getFirstName());
            user.put("lastName", userProfile.getLastName());
            user.put("weight", userProfile.getWeight());
            user.put("height", userProfile.getHeight());
            documentReference.update(user).addOnSuccessListener(aVoid -> Log.d("UserRepo", "onSuccess: user profile is updated for " + userID));
        } else {
            Log.i("UserRepo", "User is not logged in");
        }
    }
}
