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

}



