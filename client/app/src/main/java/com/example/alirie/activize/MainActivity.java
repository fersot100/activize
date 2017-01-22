package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

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
        /*String events[] = {
                "memes",
                "dreams",
                "creams",
                "queens",
                "peens"
        };
        ArrayAdapter<String> eventAdapter =
                new ArrayAdapter<String>(this,
                        R.layout.event_card,
                        R.id.event,
                        events
                );
        ListView eventList = new ListView(this);
        setContentView(eventList);
        eventList.setAdapter(eventAdapter);*/
    }


    public void getEvents(String url) throws IOException{

    }

    public void createEvent(View v){
        Intent i = new Intent(getApplicationContext(), CreateEvent.class);
        startActivity(i);
    }

}
