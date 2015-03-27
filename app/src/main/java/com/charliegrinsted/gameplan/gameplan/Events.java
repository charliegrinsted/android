package com.charliegrinsted.gameplan.gameplan;

import android.location.Location;

import java.util.Date;

class Events {

    private String eventTitle;
    private String eventInfo;
    private String eventID;
    private String eventDistance;
    private Date startTime;
    private Date endTime;
    private Integer numberOfPlayers;
    private Integer numberOfSpaces;
    private Location eventLocation;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
