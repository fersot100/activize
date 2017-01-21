package com.example.alirie.activize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //get list of events
    }
}
