package com.metaplay.demo.genres;

import com.google.gson.Gson;

public class Genres {
    public String name;
    public String created_at;
    public String updated_at;
    public String id;

    public Movie[] movies;

    public static Genres[] fromJson(String data) {
        Gson gson = new Gson();
        return new Gson().fromJson(data, Genres[].class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
