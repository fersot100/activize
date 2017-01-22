package com.example.alirie.activize;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by john on 1/22/17.
 */

public class Utils {
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("Error--->", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public static Event JSONtoEvent(String eventJSON) {
        try {
            JSONObject event = new JSONObject(eventJSON);

            String eventName = event.getString("eventName");
            String description = event.getString("description");
            String date = event.getString("dateTime");
            String latlng = event.getString("latlng");
            String address = event.getString("address");
            // Create a new {@link Event} object
            return new Event(eventName, description, date, latlng, address);
        } catch (JSONException e) {
            Log.e("Error--->", "Problem parsing the JSON results", e);
        }
        return null;
    }
    public static ArrayList<Event> JSONtoEventList(String eventJSON) {
        try {
            ArrayList<Event> events = new ArrayList<>();
            JSONArray eventList = new JSONArray(eventJSON);

            // If there are results in the features array
            if (eventList.length() > 0) {
                for(int i = 0; i < eventList.length(); i++) {
                    JSONObject JSONEvent = eventList.getJSONObject(i);
                    Event javaEvent = new Event();

                    // Extract out the title, time, and tsunami values
                    javaEvent.eventName = JSONEvent.getString("eventName");
                    javaEvent.eventDescription = JSONEvent.getString("description");
                    javaEvent.eventTime = JSONEvent.getString("eventTime");
                    events.add(javaEvent);
                }

                // Create a new {@link Event} object
                return events;
            }
        } catch (JSONException e) {
            Log.e("Error--->", "Problem parsing the JSON results", e);
        }
        return null;
    }
}
