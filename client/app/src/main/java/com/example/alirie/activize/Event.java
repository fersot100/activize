package com.example.alirie.activize;

public class Event {

    String eventName, eventTime, eventDescription;

    public Event(String name, String dateTime, String description){
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

}
