package com.example.alirie.activize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.android.map.MapView;

public class CreateEvent extends AppCompatActivity {
    MapView mapView;
    GraphicsLayer locationLayer;
    Point locationLayerPoint;
    String locationLayerPointString;
    Boolean isMapLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mapView = (MapView) findViewById(R.id.map);
        locationLayer = new GraphicsLayer();
        mapView.addLayer(locationLayer);
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if ((source == mapView) && (status == STATUS.INITIALIZED)) {
                    isMapLoaded = true;
                }
            }
        });
    }

}
