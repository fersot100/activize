package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //load splash page
        //get list of events
        //populate UI with event cards
        //hide splash, display events
        //toolbar w/ "sort by" and "create event"
        //clicking on an event brings up event page with map
    }


    public void getEvents(String url) throws IOException{

    }

    public void createEvent(View v){
        Intent i = new Intent(getApplicationContext(), CreateEvent.class);
        startActivity(i);
    }
}
