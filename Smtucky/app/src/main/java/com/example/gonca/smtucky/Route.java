package com.example.gonca.smtucky;

import java.util.ArrayList;

/**
 * Created by gonca on 26/12/2017.
 */

public class Route {

    private String official_name;
    private String route_name;
    private int id;
    private String from;
    private String to;
    private ArrayList<String> times;


    public Route(String official_name, String route_name, int id, String from, String to, ArrayList<String> times) {
        this.official_name = official_name;
        this.route_name = route_name;
        this.id = id;
        this.from = from;
        this.to = to;
        this.times = times;
    }

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
}
