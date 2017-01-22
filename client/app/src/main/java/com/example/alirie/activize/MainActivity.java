package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
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
                "Smash the Fash",
                "Free the Nipple",
                "Flash Mob",
                "No DAPL",
                "Red Umbrella Day"
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

    //get a single event by ID and open page
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
                    openNewEventPage(createEventObject(event));
                }catch (Exception e) {
                    e.printStackTrace();}
            }
        };
        client.newCall(req).enqueue(callback);
    }

    private void openNewEventPage(Event eventToOpen){
        Intent i = new Intent(getApplicationContext(), EventPage.class);
        i.putExtra("name", eventToOpen.getName());
        i.putExtra("description", eventToOpen.getDescription());
        i.putExtra("latlng", eventToOpen.getLatLng());
        i.putExtra("dateTime", eventToOpen.getDateTime());
        i.putExtra("address", eventToOpen.getAddress());
        startActivity(i);
    }

    private Event createEventObject(JSONObject event){
        try{
            String name = event.getString("name");
            String description = event.getString("description");
            String latlng = event.getString("latlng");
            String address = event.getString("address");
            String time = event.getString("startTime");
            return new Event(name, description, time, latlng, address);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public void createEvent(View v){
        Intent i = new Intent(getApplicationContext(), CreateEvent.class);
        startActivity(i);
    }

}
