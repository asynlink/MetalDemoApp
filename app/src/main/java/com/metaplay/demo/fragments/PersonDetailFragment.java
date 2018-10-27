package com.metaplay.demo.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.metaplay.demo.R;
import com.metaplay.demo.UserProfileViewModel;
import com.metaplay.demo.http.ImageDownloaderTask;
import com.metaplay.demo.user.UserProfile;


public class PersonDetailFragment extends ParentFragment {
    UserProfile mProfile;

    public PersonDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_profile, container, false);
        mProfile = mUserModel.getUseProfile();

        if (mProfile != null) {
            ((TextView)rootView.findViewById(R.id.userName)).setText(mProfile.given_name.toUpperCase() + " "
                   + mProfile.family_name.toUpperCase());

            ImageView profilePic = (ImageView)rootView.findViewById(R.id.profileImage);
            new ImageDownloaderTask(profilePic, mProfile.id).execute(mProfile.picture_url);

            Switch switchButton = (Switch)rootView.findViewById(R.id.locationSwitch);
            switchButton.setChecked(mProfile.location_enabled);
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        mProfile.location_enabled = true;
                    } else {
                        mProfile.location_enabled = false;
                    }
                }
            });

            SeekBar seekBar = (SeekBar)rootView.findViewById(R.id.seekBar);
            seekBar.setProgress(mProfile.suggestion_radius);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mProfile.suggestion_radius = progress;
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }
            });

        }

        ((LinearLayout)rootView.findViewById(R.id.trailerSection)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Wilbur", "sfasdfasdfsdfafds");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        ((LinearLayout)rootView.findViewById(R.id.profileSection)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Wilbur", "sfasdfasdfsdfafd1111s");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        ((ImageView)rootView.findViewById(R.id.profile)).setImageResource(R.drawable.profile_on);
        ((ImageView)rootView.findViewById(R.id.trailer)).setImageResource(R.drawable.trailer_off);
        ((TextView)rootView.findViewById(R.id.profileText)).setTextColor(getResources().getColor(R.color.orangedark, null));
        ((TextView)rootView.findViewById(R.id.trailerText)).setTextColor(getResources().getColor(R.color.black, null));

        return rootView;
    }
}
