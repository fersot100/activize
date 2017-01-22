package com.example.alirie.activize;

public class Event {

    String eventName, eventTime, eventDescription, eventLatLng, eventAddress;

    public Event(String name, String dateTime, String description, String latlng, String address){
        eventName = name;
        eventTime = dateTime;
        eventDescription = description;
    }
    public Event(){
    }

    public String getName(){
        return eventName;
    }
    public String getDateTime(){
        return eventTime;
    }
    public String getDescription(){
        return eventDescription;
    }
    public String getLatLng(){
        return eventLatLng;
    }
    public String getAddress(){
        return eventAddress;
    }
}
