package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;

public class EventPage extends AppCompatActivity {
    MapView mapView;
    final MapOptions StreetsBaseMap = new MapOptions(MapOptions.MapType.STREETS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        mapView = (MapView) findViewById(R.id.eventMap);
        Intent i = getIntent();
        Bundle extras = getIntent().getExtras();
        String latlng = extras.getString("latlng");
        String [] parts = latlng.split(" ");
        String lat = parts[0];
        String lng = parts[1];
        StreetsBaseMap.setCenter(Double.parseDouble(lat), Double.parseDouble(lng));
        mapView.setMapOptions(StreetsBaseMap);
    }
}
