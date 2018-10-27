package com.metaplay.demo.http;

import com.google.gson.Gson;

public class HttpRequest {
    public int[][] motionZones;
    public long startTimeSec;
    public long endTimeSec;

    public static HttpRequest fromJson(String data) {
        Gson gson = new Gson();
        return new Gson().fromJson(data, HttpRequest.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
