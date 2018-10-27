package com.metaplay.demo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.metaplay.demo.genres.Genres;
import com.metaplay.demo.genres.Movie;


public class GenresViewModel extends ViewModel {
    private final MutableLiveData<Genres[]> mGenresList = new MutableLiveData<Genres[]>();
    private final MutableLiveData<Movie> selected = new MutableLiveData<Movie>();

    public void setSelected(Movie item) {
        selected.setValue(item);
    }

    public LiveData<Movie> getSelected() {
        return selected;
    }

    public void setFullList(Genres[] fullList) {
        mGenresList.setValue(fullList);
    }

    public LiveData<Genres[]> getFullList() {
        return mGenresList;
    }
}
