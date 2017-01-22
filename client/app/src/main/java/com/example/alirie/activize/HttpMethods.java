package com.example.alirie.activize;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by john on 1/21/17.
 */

public class HttpMethods {
    private OkHttpClient client = new OkHttpClient();
    private String postResponse;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public HttpMethods(){}

    public String httpGET(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /* OKHttp Methods */
    void post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                postResponse = responseBody.string();
                responseBody.close();
            }
        });

    }

    void delete(String url) throws  IOException{
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("testing", "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
            }
        });
    }

    //get all events
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
                    //get JSONArray of events
                    JSONArray events = new JSONArray(response.body().string());
                    response.close();
                    //TODO: something with the arrya
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
                    //TODO:something with the event object
                }catch (Exception e) {
                    e.printStackTrace();}
            }
        };
        client.newCall(req).enqueue(callback);
    }

    public String httpPOST(URL url) throws IOException{
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public String readFromStream(InputStream inputStream) throws IOException {
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

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("Error--->", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private Event JSONtoEvent(String eventJSON) {
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
    private ArrayList<Event> JSONtoEventList(String eventJSON) {
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



