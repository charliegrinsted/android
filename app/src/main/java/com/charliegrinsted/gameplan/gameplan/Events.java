package com.charliegrinsted.gameplan.gameplan;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

class Events {

    private String eventTitle;
    private String eventInfo;
    private String eventID;
    private String eventDistance;
    private String startTime;
    private String endTime;
    private Integer numberOfPlayers;
    private Integer numberOfSpaces;
    private Location eventLocation;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Integer getNumberOfSpaces() {
        return numberOfSpaces;
    }

    public void setNumberOfSpaces(Integer numberOfSpaces) {
        this.numberOfSpaces = numberOfSpaces;
    }

    public Location getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(Location eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDistance() {
        return eventDistance;
    }

    public void setEventDistance(String eventDistance) {
        this.eventDistance = eventDistance;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public Events(){


    }
}
