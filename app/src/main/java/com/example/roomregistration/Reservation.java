package com.example.roomregistration;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.GregorianCalendar;

public class Reservation implements Serializable {

    public Reservation(long fromTime, long toTime, Object userId, String purpose, int roomId) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.userId = userId;
        this.purpose = purpose;
        this.roomId = roomId;
    }

    /**
     * id : 1
     * fromTime : 1568981908
     * toTime : 1568984908
     * userId : null
     * purpose : Lesson
     * roomId : 1
     */

    private int id;
    private long fromTime;
    private long toTime;
    private Object userId;
    private String purpose;
    private int roomId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    @Override
    public String toString()
    {

        return "From: " + new Date(fromTime * 1000) +
                "\nTo: "+ new Date(toTime * 1000) +
                "\npurpose: " + purpose;
    }

}
