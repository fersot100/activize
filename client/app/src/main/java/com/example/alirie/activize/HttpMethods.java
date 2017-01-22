package com.example.alirie.activize;
import android.net.http.RequestQueue;

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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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

    void export(Event event) {
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("name", event.getName());
            eventJSON.put("description", event.getDescription());
            eventJSON.put("latlng", event.getLatLng());
            eventJSON.put("startTime", event.getDateTime());
            eventJSON.put("address", event.getAddress());
        }catch (JSONException e){
            e.printStackTrace();
        }
        String json = eventJSON.toString();
        try{
            post("http://ec2-35-165-244-31.us-west-2.compute.amazonaws.com/events", json);
        }catch (IOException e){

        }
    }


}



