package com.metaplay.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int GO_TO_GENRES_PAGE = 1;

    Handler mHandlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mHandlerThread = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GO_TO_GENRES_PAGE){
                    finish();
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandlerThread.sendEmptyMessageDelayed(GO_TO_GENRES_PAGE, 3000);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.removeMessages(GO_TO_GENRES_PAGE);
    }
}
