package com.metaplay.demo.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.metaplay.demo.GenresViewModel;
import com.metaplay.demo.UserProfileViewModel;

public class ParentFragment extends Fragment {
    GenresViewModel mModel;
    UserProfileViewModel mUserModel;

    public void replaceFragment(Fragment destFragment, int containerId)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.replace(containerId, destFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
