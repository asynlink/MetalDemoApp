package com.metaplay.demo.http;

public class ServerReponse {
    private int status;
    private String result;

    public ServerReponse(int status, String result) {
        this.status = status;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }
    public String getResult() {
        return result;
    }
}
