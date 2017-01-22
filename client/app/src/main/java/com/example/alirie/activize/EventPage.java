package com.example.alirie.activize;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;

import java.util.Map;

public class EventPage extends AppCompatActivity {
    MapView mapView;
    Layer locationLayer;
    MapOptions StreetsBaseMap;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        uri = "http://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer";

        Intent i = getIntent();
        String latlng = i.getStringExtra("latlng");
        String [] parts = latlng.split(" ");
        String lat = parts[0];
        String lng = parts[1];
        Log.i("lat", lat);
        Log.i("long", lng);
        StreetsBaseMap = new MapOptions(MapOptions.MapType.STREETS, Double.parseDouble(lat), Double.parseDouble(lng), 9);
        mapView = new MapView(EventPage.this, StreetsBaseMap);
        locationLayer = new GraphicsLayer();
        mapView.addLayer(locationLayer);

        ViewGroup viewGroup = (RelativeLayout) findViewById(R.id.activity_event_page);
        int heightDp = getResources().getDisplayMetrics().heightPixels / 3;

        mapView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDp));

        viewGroup.addView(mapView);
    }
}
