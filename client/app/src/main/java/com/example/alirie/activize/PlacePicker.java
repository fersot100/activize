package com.example.alirie.activize;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.geocode.LocatorReverseGeocodeResult;

import java.util.List;
import java.util.Map;

public class PlacePicker extends AppCompatActivity {
    Boolean isMapLoaded;
    MapView mapView;
    GraphicsLayer locationLayer;
    EditText searchBar;
    String locationLayerPointString;
    static Point locationLayerPoint;
    AlertDialog dialogGlobal = null;
    public static final String Result_LATLNG = "Result_LATLNG";
    public static final String Result_ADDRESS = "Result_ADDRESS";
    static final int RESULT_OK = 2;
    static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        mapView = (MapView)findViewById(R.id.map);
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final View searchRef = menu.findItem(R.id.action_search).getActionView();
        searchBar = (EditText) searchRef.findViewById(R.id.searchText);

        searchBar.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    onSearchButtonClicked(searchBar);
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSearchButtonClicked(View view){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        String address = searchBar.getText().toString();
        executeLocatorTask(address);

    }

    private void executeLocatorTask(String address) {
        // Create Locator parameters from single line address string
        LocatorFindParameters findParams = new LocatorFindParameters(address);

        // Use the centre of the current map extent as the find location point
        findParams.setLocation(mapView.getCenter(), mapView.getSpatialReference());

        // Calculate distance for find operation
        Envelope mapExtent = new Envelope();
        mapView.getExtent().queryEnvelope(mapExtent);
        // assume map is in metres, other units wont work, double current envelope
        double distance = (mapExtent != null && mapExtent.getWidth() > 0) ? mapExtent.getWidth() * 2 : 10000;
        findParams.setDistance(distance);
        findParams.setMaxLocations(2);

        // Set address spatial reference to match map
        findParams.setOutSR(mapView.getSpatialReference());

        // Execute async task to find the address
        new LocatorAsyncTask().execute(findParams);
        locationLayerPointString = address;
    }

    private class LocatorAsyncTask extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {
        private Exception mException;

        @Override
        protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
            mException = null;
            List<LocatorGeocodeResult> results = null;
            Locator locator = Locator.createOnlineLocator();
            try {
                results = locator.find(params[0]);

            } catch (Exception e) {
                mException = e;
            }

            return results;
        }
        protected void onPostExecute(List<LocatorGeocodeResult> result) {
            if (mException != null) {
                Log.w("PlaceSearch", "LocatorSyncTask failed with:");
                mException.printStackTrace();
                Toast.makeText(PlacePicker.this, getString(R.string.addressSearchFailed), Toast.LENGTH_LONG).show();
                return;
            }

            if (result.size() == 0) {
                Toast.makeText(PlacePicker.this, getString(R.string.noResultsFound), Toast.LENGTH_LONG).show();
            } else {
                // Use first result in the list
                LocatorGeocodeResult geocodeResult = result.get(0);

                // get return geometry from geocode result
                SpatialReference sp = SpatialReference.create(4326);
                final Point resultPoint = (Point) GeometryEngine.project(geocodeResult.getLocation(), mapView.getSpatialReference(), sp);
                // create marker symbol to represent location
                SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(Color.RED, 16, SimpleMarkerSymbol.STYLE.CROSS);
                // create graphic object for resulting location
                Graphic resultLocGraphic = new Graphic(resultPoint, resultSymbol);
                // add graphic to location layer
                locationLayer.addGraphic(resultLocGraphic);

                // create text symbol for return address
                address = geocodeResult.getAddress();
                TextSymbol resultAddress = new TextSymbol(20, address, Color.BLACK);
                // create offset for text
                resultAddress.setOffsetX(-4 * address.length());
                resultAddress.setOffsetY(10);
                // create a graphic object for address text
                Graphic resultText = new Graphic(resultPoint, resultAddress);
                // add address text graphic to location graphics layer
                locationLayer.addGraphic(resultText);
                locationLayerPoint = resultPoint;

                // Zoom map to geocode result location
                mapView.zoomToResolution(geocodeResult.getLocation(), 2);
                PlacePickerDialog dialog = new PlacePickerDialog();
                dialog.show(getSupportFragmentManager(), "dialog");
                }
            }

        }

    public static class PlacePickerDialog extends DialogFragment {
        public PlacePickerDialog(){}
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.select_prompt)
                    .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Double latitude = locationLayerPoint.getY();
                            Double longitude = locationLayerPoint.getX();
                            Intent sendIntent = new Intent();
                            sendIntent.putExtra(Result_LATLNG, latitude + " " + longitude);
                            sendIntent.putExtra(Result_ADDRESS, address.toString());
                            getActivity().setResult(RESULT_OK, sendIntent);
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    });
            return builder.create();
        }

    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (dialogGlobal!=null && dialogGlobal.isShowing()){
            dialogGlobal.dismiss();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (dialogGlobal!=null && dialogGlobal.isShowing()){
            dialogGlobal.dismiss();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        if (dialogGlobal!=null && dialogGlobal.isShowing()){
            dialogGlobal.dismiss();
        }
    }



    }




