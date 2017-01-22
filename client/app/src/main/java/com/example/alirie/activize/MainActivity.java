package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
        String events[] = {
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
        ListView eventList = (ListView) findViewById(R.id.list);
        eventList.setAdapter(eventAdapter);
    }
    //get all events and populate UI with textviews for each
    public void getEvents(String url) throws IOException{
        final Request request = new Request.Builder()
                .url(url)
                .build();

        final Callback callback = new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray events = new JSONArray(response.body().string());
                    response.close();
                }catch (Exception e) {
                    e.printStackTrace();}
            }
        };
        client.newCall(request).enqueue(callback);
    }

    //get a single event by ID
    public void getEventById(String url, String id) throws IOException{
        String uri = url + "/events/" + id;
        final Request req = new Request.Builder()
                .url(uri)
                .build();
        final Callback callback = new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject event = new JSONObject(response.body().string());
                    response.close();
                }catch (Exception e) {
                    e.printStackTrace();}
            }
        };
        client.newCall(req).enqueue(callback);
    }


    public void createEvent(View v){
        Intent i = new Intent(getApplicationContext(), CreateEvent.class);
        startActivity(i);
    }

}
