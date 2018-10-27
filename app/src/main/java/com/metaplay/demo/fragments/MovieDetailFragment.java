package com.metaplay.demo.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metaplay.demo.GenresViewModel;
import com.metaplay.demo.R;
import com.metaplay.demo.genres.Cast;
import com.metaplay.demo.genres.Movie;
import com.metaplay.demo.http.ImageDownloaderTask;
import com.metaplay.demo.http.NetworkConnector;

public class MovieDetailFragment extends ParentFragment {

    Movie mSelectedMovie;

    View mRootView;

    NetworkConnector mConnector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(GenresViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        mRootView = rootView;

        mSelectedMovie = mModel.getSelected().getValue();

        if (mSelectedMovie != null) {
            ((TextView)rootView.findViewById(R.id.imdb_score)).setText(""+mSelectedMovie.imdb_score);
            ((TextView)rootView.findViewById(R.id.rt_score)).setText(""+mSelectedMovie.rt_score);
            ((TextView) rootView.findViewById(R.id.synopsis_detail)).setText(mSelectedMovie.synopsis);
            onPopulateCastNCrew(inflater, rootView);

            onSwitchLikeState();

            ((ImageButton)mRootView.findViewById(R.id.liked)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedMovie.liked = !mSelectedMovie.liked;
                    onSwitchLikeState();
                }
            });

            ((TextView)rootView.findViewById(R.id.movie_title)).setText(mSelectedMovie.title.toUpperCase());
            ((ImageView)rootView.findViewById(R.id.visitWeb)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShare();
                }
            });

            ((ImageButton)rootView.findViewById(R.id.webview)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewFragment fragment = new WebViewFragment();
                    replaceFragment(fragment, R.id.webview_detail_container);
                }
            });
        }

        return rootView;
    }

    private void onPopulateCastNCrew(LayoutInflater inflater, View rootView) {
        if (mSelectedMovie != null && mSelectedMovie.cast != null && mSelectedMovie.cast.length > 0) {
            LinearLayout gallery = (LinearLayout)rootView.findViewById(R.id.crewGallery);
            for(Cast cast : mSelectedMovie.cast) {
                View movieView = inflater.inflate(R.layout.cast_crew_layout, gallery, false);

                ((TextView)movieView.findViewById(R.id.cast_name)).setText(cast.given_name.toUpperCase() + " " + cast.family_name.toUpperCase());
                ImageView castImge = (ImageView)movieView.findViewById(R.id.cast_image);
                    new ImageDownloaderTask(castImge, cast.id).execute(cast.picture_url);

                gallery.addView(movieView);
            }
        }
    }

    private void onSwitchLikeState() {
        if (mSelectedMovie != null) {
            if (mSelectedMovie.liked) {
                ((ImageButton)mRootView.findViewById(R.id.liked)).setBackgroundResource(R.drawable.circle_shape_on);
                ((ImageButton)mRootView.findViewById(R.id.liked)).setImageResource(R.drawable.heart_on);
            } else {
                ((ImageButton)mRootView.findViewById(R.id.liked)).setBackgroundResource(R.drawable.circle_shape_off);
                ((ImageButton)mRootView.findViewById(R.id.liked)).setImageResource(R.drawable.heart_off);
            }
        }
    }

    private void onShare() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String message = "Hey check out " + mSelectedMovie.title + "!";
        if (mSelectedMovie != null && mSelectedMovie.cast[0] != null && mSelectedMovie.cast[0].given_name != null &&
                mSelectedMovie.cast[0].family_name != null) {
            message = "Hey check out " + mSelectedMovie.title + "!" +
                    " It has a " + mSelectedMovie.imdb_score + " on IMDB and starts " + mSelectedMovie.cast[0].given_name + "  " +
                    mSelectedMovie.cast[0].family_name +".";
        } else {
            message = "Hey check out " + mSelectedMovie.title + "!" +
                    " It has a " + mSelectedMovie.imdb_score + " on IMDB.";
        }

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
}
