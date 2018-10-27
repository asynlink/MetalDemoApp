package com.metaplay.demo.http;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkConnector extends AsyncTask<ServerReponse, Void, ServerReponse> {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 10000;

    private String mServerUrl;
    private String mRequest;
    private String mMethod;

    private NetworkCallback mCallback;

    public NetworkConnector(Context ctxt, String url, String method, String request, NetworkCallback callback) {
        mServerUrl = url;
        mRequest = request;
        mCallback = callback;
        mMethod = method;
    }

    public void setCallback(NetworkCallback callback) {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ServerReponse doInBackground(ServerReponse... unused) {
        BufferedReader reader = null;
        ServerReponse reponse = null;

        try {
            URL urlObj = new URL(mServerUrl);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod(mMethod);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            if (mMethod.equals(METHOD_POST) && mRequest != null) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream out = conn.getOutputStream();
                out.write(mRequest.getBytes());
                out.close();
            }

            int status = ((HttpURLConnection)conn).getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream response = conn.getInputStream();
                String data = convertStreamToString(response);
                reponse = new ServerReponse(status, data);
            } else {
                InputStream response = conn.getErrorStream();
                String data = convertStreamToString(response);
                reponse = new ServerReponse(status, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new ServerReponse(HttpsURLConnection.HTTP_INTERNAL_ERROR, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return reponse;
    }

    @Override
    public void onPostExecute(ServerReponse response) {
        if (mCallback != null) {
            mCallback.onResult(response);
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
