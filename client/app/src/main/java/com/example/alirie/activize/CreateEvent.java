package com.example.alirie.activize;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class CreateEvent extends AppCompatActivity {
    GraphicsLayer locationLayer;
    static final int LOCATION_REQUEST = 1;
    static final int REQUEST_OK = 2;
    String timeForExport, AM_PM, address, latlng;
    int eventDay, eventMonth, eventYear, year, month, day;
    int minute, hour;
    Boolean isMapLoaded;
    EditText timeElement, dateElement, locationElement, nameElement, descriptionElement;
    Time now = new Time();
    Time eventTime = new Time();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        dateElement = (EditText) findViewById(R.id.input_date);
        timeElement = (EditText) findViewById(R.id.input_time);
        locationElement = (EditText) findViewById(R.id.input_location);
        nameElement = (EditText) findViewById(R.id.input_event_name);
        descriptionElement = (EditText) findViewById(R.id.input_event_description);
        now.setToNow();
        year = now.year;
        month = now.month;
        day = now.monthDay;
    }

    public void pickPlace(View view){
        Intent i = new Intent(getApplicationContext(), PlacePicker.class);
        startActivityForResult(i, LOCATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_OK){
            //set edittext to address
            latlng = (data.getStringExtra("Result_LATLNG"));
            address = data.getStringExtra("Result_ADDRESS");
            locationElement.setText(address);
            locationElement.append(latlng);
            try {
                JSONObject locationJSON = new JSONObject();
                locationJSON.put("LatLng", latlng);
                locationJSON.put("Address", address);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

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

    public void createNewEvent(View v){
        String name = nameElement.getText().toString();
        String date = dateElement.getText().toString();
        String time = timeElement.getText().toString();
        String description = descriptionElement.getText().toString();
        Event newEvent = new Event(name, date + " " + time, description, latlng, address);
        Intent i = new Intent(this, EventPage.class);
        i.putExtra("name", newEvent.getName());
        i.putExtra("dateTime", newEvent.getDateTime());
        i.putExtra("description", newEvent.getDescription());
        i.putExtra("latlng", newEvent.getLatLng());
        i.putExtra("address", newEvent.getAddress());
        startActivity(i);
    }

}
