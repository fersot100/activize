package com.example.alirie.activize;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.Popup;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.Map;

public class EventPage extends AppCompatActivity {
    MapView mapView;
    Layer locationLayer;
    MapOptions StreetsBaseMap;
    String uri;
    TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        uri = "http://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer";

        Intent i = getIntent();
        String latlng = i.getStringExtra("latlng");
        String eventName = i.getStringExtra("name");
        String [] parts = latlng.split(" ");
        String lat = parts[0];
        String lng = parts[1];
        Log.i("lat", lat);
        Log.i("long", lng);
        StreetsBaseMap = new MapOptions(MapOptions.MapType.STREETS, Double.parseDouble(lat), Double.parseDouble(lng), 9);
        mapView = new MapView(EventPage.this, StreetsBaseMap);
        mapView.setId(R.id.eventMap);
        nameView = (TextView) findViewById(R.id.name);
        nameView.setText(eventName);
        ViewGroup viewGroup = (RelativeLayout) findViewById(R.id.activity_event_page);
        int heightDp = getResources().getDisplayMetrics().heightPixels / 3;
        final GraphicsLayer layer = new GraphicsLayer();
        // create a simple marker symbol to be used by our graphic
        SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 5, SimpleMarkerSymbol.STYLE.CIRCLE);
        // create a point geometry that defines the graphic
        Point pnt = new Point(mapView.getX(), mapView.getY());
        // create the graphic using the symbol and point geometry
        Graphic graphic = new Graphic(pnt, sms);
        // add the graphic to a graphics layer
        layer.addGraphic(graphic);
        mapView.addLayer(layer);
        mapView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDp));
        viewGroup.addView(mapView);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LandmarkDialog dialog = new LandmarkDialog();
                dialog.show(getSupportFragmentManager(), "Dialog");
                return true;
            }
        });

    }

    public static class LandmarkDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter landmark type")
                    .setItems(R.array.options_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                dismiss();
                            }else if (which == 1){
                                dismiss();
                            }else if (which == 2){
                                dismiss();
                            }
                        }
                    });
            return builder.create();
        }
    }

}
