package kth.jjve.xfran.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kth.jjve.xfran.models.UserProfile;

public class UserRepo {
    private static UserRepo instance;
    private UserProfile userProfile = new UserProfile();

    public static UserRepo getInstance(){
        if(instance == null){
            instance = new UserRepo();
        }
        return instance;
    }

    public MutableLiveData<UserProfile> getUserProfile(){
        findUserProfile();
        MutableLiveData<UserProfile> up = new MutableLiveData<>();
        up.setValue(userProfile);

        return up;
    }

    private void findUserProfile(){
        //Todo: get user profile from firebase
    }
}
