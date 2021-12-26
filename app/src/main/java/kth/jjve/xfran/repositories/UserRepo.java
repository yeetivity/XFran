package kth.jjve.xfran.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
        findUserProfile();
        up = new MutableLiveData<>();
        up.setValue(userProfile);

        return up;
    }

    private void findUserProfile(){
        //Todo: get user profile from firebase
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
