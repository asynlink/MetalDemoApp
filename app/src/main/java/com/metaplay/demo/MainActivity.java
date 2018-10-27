package com.metaplay.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.metaplay.demo.fragments.GenresFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            GenresFragment fragment = new GenresFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.genres_detail_container, fragment)
                    .commit();
        }
    }
}
