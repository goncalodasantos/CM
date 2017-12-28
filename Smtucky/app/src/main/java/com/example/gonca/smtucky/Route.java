package com.example.gonca.smtucky;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gonca on 26/12/2017.
 */

@Entity(tableName = "route")
public class Route implements Serializable{

    @PrimaryKey
    private int id;


    private String official_name;


    private String route_name;


    private String from;


    private String to;


    private ArrayList<String> stops;

    private ArrayList<String> times;

    public Route() {

    }

    @Ignore
    public Route(String official_name, String route_name, int id) {
        this.official_name = official_name;
        this.route_name = route_name;
        this.id = id;

    }



    public String getOfficial_name() {
        return official_name;
    }

    public void setOfficial_name(String official_name) {
        this.official_name = official_name;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }


    public ArrayList<String> getStops() {
        return stops;
    }

    public void setStops(ArrayList<String> stops) {
        this.stops = stops;
    }

    @Override
    public String toString() {
        if(getRoute_name().equals("Desconhecido")) {
            return getFrom() + " - " + getTo();
        } else {
            return getRoute_name() + " : " + getFrom() + " - " + getTo();
        }
    }

}
