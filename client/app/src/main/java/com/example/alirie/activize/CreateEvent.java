package com.example.alirie.activize;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.android.map.MapView;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;

import java.util.Calendar;
import java.util.List;

public class CreateEvent extends AppCompatActivity {
    MapView mapView;
    GraphicsLayer locationLayer;
    Point locationLayerPoint;
    String locationLayerPointString, timeForExport, AM_PM, id;
    int eventDay, eventMonth, eventYear, year, month, day;
    int minute, hour;
    Boolean isMapLoaded;
    EditText searchBar;
    EditText timeElement, dateElement;
    Time now = new Time();
    Time eventTime = new Time();

/*
    private class LocatorAsyncTask extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {
        private Exception exception;
        @Override
        protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
            exception = null;
            List<LocatorGeocodeResult> results = null;
            Locator locator = Locator.createOnlineLocator();
            try {
                results = locator.find(params[0]);
            } catch (Exception e) {
                exception = e;
            }
            return results;
        }
    }
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateElement = (EditText) findViewById(R.id.input_date);
        timeElement = (EditText) findViewById(R.id.input_time);
        now.setToNow();
        year = now.year;
        month = now.month;
        day = now.monthDay;

//        mapView = (MapView) findViewById(R.id.map);
//        locationLayer = new GraphicsLayer();
//        mapView.addLayer(locationLayer);
//        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
//            public void onStatusChanged(Object source, STATUS status) {
//                if ((source == mapView) && (status == STATUS.INITIALIZED)) {
//                    isMapLoaded = true;
//                }
//            }
//        });
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        View searchRef = menu.findItem(R.id.action_search).getActionView();
//        searchBar = (EditText) searchRef.findViewById(R.id.searchText);
//        searchBar.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if(keyCode == KeyEvent.KEYCODE_ENTER){
//                    onSearchButtonClicked(searchBar);
//                    return true;
//                }
//                return false;
//            }
//        });
//        return true;
//    }
//
//    private void locate(String address){
//        // Create Locator parameters from single line address string
//        LocatorFindParameters findParams = new LocatorFindParameters(address);
//
//        // Use the centre of the current map extent as the find location point
//        findParams.setLocation(mapView.getCenter(), mapView.getSpatialReference());
//
//        // Calculate distance for find operation
//        Envelope mapExtent = new Envelope();
//        mapView.getExtent().queryEnvelope(mapExtent);
//        // assume map is in metres, other units wont work, double current envelope
//        double distance = (mapExtent != null && mapExtent.getWidth() > 0) ? mapExtent.getWidth() * 2 : 10000;
//        findParams.setDistance(distance);
//        findParams.setMaxLocations(2);
//
//        // Set address spatial reference to match map
//        findParams.setOutSR(mapView.getSpatialReference());
//
//        // Execute async task to find the address
//        new LocatorAsyncTask().execute(findParams);
//        locationLayerPointString = address;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if (id == R.id.action_search) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void onSearchButtonClicked(View view){
//        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        String address = searchBar.getText().toString();
//        locate(address);
//    }
//

    //opens date picker dialog
    public void pickDate (View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateElement.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
//                        dateForExport = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        eventDay = dayOfMonth;
                        eventMonth = monthOfYear;
                        eventYear = year;
                    }
                }, year, month, day);
        datePickerDialog.show();

    }
    //opens time picker dialog
    public void pickTime(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        String min;
                        String hr;
                        if( m < 10)
                        {
                            min = "0" + m;
                        }
                        else
                        {
                            min = Integer.toString(m);
                        }
                        if (h > 12){
                            AM_PM = "PM";
                            hr = String.valueOf(h - 12);
                        }
                        else if (h == 0){
                            h = 12;
                            hr = "12";
                            AM_PM = "AM";
                        }
                        else{
                            AM_PM = "AM";
                            hr = String.valueOf(h);
                        }
                        timeForExport = hr + ":" + min + " " + AM_PM;
                        timeElement.setText(timeForExport);
                        eventTime.set(0, m, h, eventDay, eventMonth, eventYear);

                    }
                }, hour, minute, false);
        timePickerDialog.show();
        ;

    }
    //opens place picker

}
