package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();
    ArrayList<Event> listOfEvents = new ArrayList<>();
    ArrayList<String> listOfEventNames = new ArrayList<>();
    ArrayList<Integer> listOfEventIDs = new ArrayList<>();
    int eventID;
    String url = "http://ec2-35-165-244-31.us-west-2.compute.amazonaws.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getEvents(url + "/events");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final ListView eventList = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> eventAdapter =
                    new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.event_card,
                            R.id.event,
                            listOfEventNames
                    );

            eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        getEventById(url, listOfEventIDs.get(position).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            eventList.setAdapter(eventAdapter);


        }
    };


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
                    createEventArray(events);
                    runOnUiThread(runnable);
                }catch (Exception e) {
                    e.printStackTrace();
                }
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
            eventID = event.getInt("id");
            return new Event(name, description, time, latlng, address);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private void createEventArray(JSONArray array){
        try{
            //clear existing list of events
            listOfEvents.clear();
            listOfEventNames.clear();
            listOfEventIDs.clear();
            //loop through JSONArray of events received from server, create an event object for each JSONObject in the JSONArray
            for (int i = 0; i < array.length(); i++) {
                //add new event to ArrayList
                listOfEvents.add(createEventObject(array.getJSONObject(i)));
                listOfEventNames.add(listOfEvents.get(i).getName());
                listOfEventIDs.add(eventID);
                Log.i("number", Integer.toString(eventID));
            }
    }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void createEvent(View v){
        Intent i = new Intent(getApplicationContext(), CreateEvent.class);
        startActivity(i);
    }

}
