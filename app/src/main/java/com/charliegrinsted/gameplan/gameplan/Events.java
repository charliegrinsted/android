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
    private String eventLatitude;
    private String eventLongitude;

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

    public String getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(String eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public String getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(String eventLongitude) {
        this.eventLongitude = eventLongitude;
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
