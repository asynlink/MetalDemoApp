package com.metaplay.demo.user;

import com.google.gson.Gson;

public class UserProfile {
    public String given_name;
    public String family_name;
    public String picture_url;
    public String id;
    public String created_at;
    public String updated_at;
    public boolean location_enabled;
    public int suggestion_radius;

    public static UserProfile fromJson(String data) {
        Gson gson = new Gson();
        return new Gson().fromJson(data, UserProfile.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
