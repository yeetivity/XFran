package kth.jjve.xfran.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kth.jjve.xfran.models.UserProfile;
import kth.jjve.xfran.repositories.UserRepo;

public class UserProfileVM extends ViewModel {
    private MutableLiveData<UserProfile> mUserProfile;
    private UserRepo mRepo;

    public void init(){
        if(mUserProfile != null){
            return;
        }
        mRepo = UserRepo.getInstance();
        mUserProfile = mRepo.getUserProfile();
    }

    public LiveData<UserProfile> getUserProfile(){
        return mUserProfile;
    }


}
