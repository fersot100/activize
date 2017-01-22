package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;

public class EventPage extends AppCompatActivity {
    MapView mapView;
    Layer locationLayer;
    MapOptions StreetsBaseMap = new MapOptions(MapOptions.MapType.STREETS);
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        uri = "http://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer";

        mapView = (MapView) findViewById(R.id.eventMap);
        Intent i = getIntent();
        String latlng = i.getStringExtra("latlng");
        String [] parts = latlng.split(" ");
        String lat = parts[0];
        String lng = parts[1];
        Log.i("lat", lat);
        Log.i("long", lng);
        StreetsBaseMap.setCenter(Double.parseDouble(lat), Double.parseDouble(lng));
        StreetsBaseMap.setZoom(16);
        mapView.setMapOptions(StreetsBaseMap);
        locationLayer = new GraphicsLayer();
        mapView.addLayer(locationLayer);

    }
}
