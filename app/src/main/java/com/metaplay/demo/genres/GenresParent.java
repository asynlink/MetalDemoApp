package com.metaplay.demo.genres;

import com.google.gson.Gson;

public class GenresParent {
    //public Genres[] genres;

    public static GenresParent fromJson(String data) {
        Gson gson = new Gson();
        return new Gson().fromJson(data, GenresParent.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
