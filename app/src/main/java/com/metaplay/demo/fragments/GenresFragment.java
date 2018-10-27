package com.metaplay.demo.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.metaplay.demo.GenresViewModel;
import com.metaplay.demo.ImageCache;
import com.metaplay.demo.R;
import com.metaplay.demo.UserProfileViewModel;
import com.metaplay.demo.genres.Genres;
import com.metaplay.demo.genres.Movie;
import com.metaplay.demo.http.ImageDownloaderTask;
import com.metaplay.demo.http.NetworkCallback;
import com.metaplay.demo.http.NetworkConnector;
import com.metaplay.demo.http.NetworkStatus;
import com.metaplay.demo.http.ServerReponse;
import com.metaplay.demo.user.UserProfile;

import javax.net.ssl.HttpsURLConnection;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class GenresFragment extends ParentFragment {

    NetworkConnector mConnector;

    LayoutInflater mLayoutInflater;
    View mRootView;

    ProgressBar mProgressBar;

    public GenresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(GenresViewModel.class);
        mUserModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_genres, container, false);

        OverScrollDecoratorHelper.setUpOverScroll((ScrollView)rootView.findViewById(R.id.genresPage));

        mLayoutInflater = inflater;
        mRootView = rootView;

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progress_loader);

        if (mModel.getFullList().getValue() != null) {
            onDisplay();
        } else {
            initSearch();
        }

        return rootView;
    }

    private void initSearch() {
        if (NetworkStatus.getInstance(getContext()).isOnline()) {
            mConnector = new NetworkConnector(getContext(),
                    getResources().getString(R.string.genres_url),
                    NetworkConnector.METHOD_GET,
                    null,
                    new MyNetworkCallback());
            mConnector.execute();
            mProgressBar.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getContext(), "Please check your network connection!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class MyNetworkCallback implements NetworkCallback {
        public void onResult(ServerReponse response) {
            mProgressBar.setVisibility(View.GONE);
            if (response != null) {
                if (response.getStatus() == HttpsURLConnection.HTTP_OK && response.getResult() != null) {
                    Genres[] genresList = Genres.fromJson(response.getResult());
                    if (genresList != null && genresList.length > 0) {
                        mModel.setFullList(genresList);
                        onDisplay();
                    }

                } else {
                    Toast.makeText(getContext(), response.getResult(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Dynamically allocate the genres list!
    private void onDisplay() {
         Genres[] genresData = mModel.getFullList().getValue();

         LinearLayout galleryView = (LinearLayout)mRootView.findViewById(R.id.genresParentView);
         if (genresData != null && genresData.length > 0) {
             mRootView.findViewById(R.id.buttonsSection).setVisibility(View.VISIBLE);
             for (Genres genres : genresData) {
                 View genresListView = LayoutInflater.from(getContext())
                         .inflate(R.layout.genres_layout, galleryView, false  );

                 ((TextView)genresListView.findViewById(R.id.genresTitle)).setText(genres.name.toUpperCase());

                 LinearLayout itemListView = (LinearLayout)genresListView.findViewById(R.id.genresGallery);

                 for (Movie movie : genres.movies) {
                     View movieView = mLayoutInflater.inflate(R.layout.movie_button_layout, itemListView, false);
                     TextView text = movieView.findViewById(R.id.movie_text);
                     text.setText(movie.title.toUpperCase());

                     ImageView img = (ImageView)movieView.findViewById(R.id.movie_image);
                     img.setTag(movie);
                     img.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Movie movie = (Movie)v.getTag();
                             if (movie != null) {
                                 mModel.setSelected(movie);

                                 MovieDetailFragment fragment = new MovieDetailFragment();
                                 replaceFragment(fragment, R.id.movie_detail_container);
                             }
                         }
                     });

                     itemListView.addView(movieView);
                     Bitmap bitMap = ImageCache.getInstance().getBitmapFromMemCache(movie.id);
                     if (bitMap != null) {
                         img.setImageBitmap(bitMap);
                     } else {
                         new ImageDownloaderTask(img, movie.id).execute(movie.thumbnail_url);
                     }
                  }

                 galleryView.addView(genresListView);
             }

             ((LinearLayout)mRootView.findViewById(R.id.profileSection)).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (mUserModel.getUseProfile() == null) {
                        getUserProfile();
                     } else {
                        PersonDetailFragment fragment = new PersonDetailFragment();
                        replaceFragment(fragment, R.id.profile_detail_container);
                     }
                 }
             });
         }
    }

    private void getUserProfile() {
        if (NetworkStatus.getInstance(getContext()).isOnline()) {
            mConnector = new NetworkConnector(getContext(),
                    getResources().getString(R.string.profile_url),
                    NetworkConnector.METHOD_GET,
                    null,
                    new UserProfileNetworkCallback());
            mConnector.execute();
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), "Please check your network connection!",
                    Toast.LENGTH_LONG).show();
        }
    }

        private class UserProfileNetworkCallback implements NetworkCallback {
        public void onResult(ServerReponse response) {
            mProgressBar.setVisibility(View.GONE);
            if (response != null) {
                if (response.getStatus() == HttpsURLConnection.HTTP_OK && response.getResult() != null) {
                    UserProfile profile = UserProfile.fromJson(response.getResult());
                    if (profile != null) {
                        mUserModel.setUserProfile(profile);
                        PersonDetailFragment fragment = new PersonDetailFragment();
                        replaceFragment(fragment, R.id.profile_detail_container);
                    }
                } else {
                    Toast.makeText(getContext(), response.getResult(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
