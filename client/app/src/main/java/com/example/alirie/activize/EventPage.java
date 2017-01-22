package com.example.alirie.activize;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.Map;

public class EventPage extends AppCompatActivity implements OnSingleTapListener{
    MapView mapView;
    Layer locationLayer;
    MapOptions StreetsBaseMap;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        Intent i = getIntent();
        String latlong = i.getStringExtra("latlng");
        String [] parts = latlong.split(" ");
        String lat = parts[0];
        String lng = parts[1];
        Log.i("lat", lat);
        Log.i("long", lng);
        StreetsBaseMap = new MapOptions(MapOptions.MapType.STREETS, Double.parseDouble(lat), Double.parseDouble(lng), 9);
        mapView = new MapView(EventPage.this, StreetsBaseMap);
        locationLayer = new GraphicsLayer();

        ViewGroup viewGroup = (RelativeLayout) findViewById(R.id.activity_event_page);
        int heightDp = getResources().getDisplayMetrics().heightPixels / 3;

        mapView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDp));
        viewGroup.addView(mapView);
        mapView.addLayer(locationLayer);


    }
    private View popup(){
        TextView text = new TextView(this);
        text.setText("Testing");
        return text;
    }


    @Override
    public void onSingleTap(float x, float y) {
        SimpleMarkerSymbol marker = new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.DIAMOND);

        Point identifyPoint = mapView.toMapPoint(x, y);
        Callout callout = mapView.getCallout();
        callout.show(identifyPoint, popup());

    }
}
