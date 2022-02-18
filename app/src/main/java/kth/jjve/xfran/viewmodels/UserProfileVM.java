package kth.jjve.xfran.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import kth.jjve.xfran.models.UserProfile;
import kth.jjve.xfran.repositories.UserRepo;

public class UserProfileVM extends AndroidViewModel {
    private MutableLiveData<UserProfile> mUserProfile;
    private UserRepo mRepo;

    public UserProfileVM(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if(mUserProfile != null){
            mUserProfile = mRepo.getUserProfile();
            return;
        }
        mRepo = UserRepo.getInstance();
        mUserProfile = mRepo.getUserProfile();

    }

    public LiveData<UserProfile> getUserProfile(){
        return mUserProfile;
    }

    public void setUserProfile(String fn, String ln, String e, double w, int l){
        mRepo.setUserProfile(fn, ln, e, w, l);
    }


}
