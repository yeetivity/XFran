package kth.jjve.xfran.repositories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import kth.jjve.xfran.models.UserProfile;

public class UserRepo {
    private static UserRepo instance;
    private UserProfile userProfile = new UserProfile();
    private MutableLiveData<UserProfile> up;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    public static UserRepo getInstance(){
        if(instance == null){
            instance = new UserRepo();
        }
        return instance;
    }

    public MutableLiveData<UserProfile> getUserProfile(){
        if (userProfile.checkEmpty()){
            fillUserProfile();
        }
        up = new MutableLiveData<>();
        up.setValue(userProfile);

        return up;
    }

    private void fillUserProfile(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            //authenticate user
            userID = firebaseAuth.getCurrentUser().getUid();

            //get reference to user data
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    // fill user profile with firestore data
                    try {
                        //noinspection ConstantConditions
                        userProfile = new UserProfile(
                                document.getString("firstName"),
                                document.getString("lastName"),
                                document.getString("email"),
                                document.getDouble("weight"),
                                Objects.requireNonNull(document.getLong("length")).intValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        try{
                            userProfile = new UserProfile(
                                    Objects.requireNonNull(document.getString("fullName")),
                                    document.getString("email"));
                        } catch (Exception ee){
                            ee.printStackTrace();
                        }
                    }
                }
            });
        } else {
            //fill user profile with guest data
            Log.i("UserRepo", "Use a guest account");
            userProfile = new UserProfile("Guest", "1", "guest@email.com", 0.0, 0);
        }
    }

    public void setUserProfile(String fn, String ln, String e, double w, int l){
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
            user.put("length", userProfile.getLength());
            documentReference.update(user).addOnSuccessListener(aVoid -> Log.d("UserRepo", "onSuccess: user profile is updated for "+ userID));
        }else{
            Log.i("UserRepo", "User is not logged in");
        }
    }
}
