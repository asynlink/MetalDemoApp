package com.metaplay.demo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.metaplay.demo.user.UserProfile;

public class UserProfileViewModel extends ViewModel {
    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<UserProfile>();

    public void setUserProfile(UserProfile profile) {
        userProfile.setValue(profile);
    }

    public UserProfile getUseProfile() {
        return userProfile.getValue();
    }
}
