package com.example.gonca.smtucky;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gonca on 26/12/2017.
 */

public class Routes extends ViewModel {
    private ArrayList<Route> routes;


    public  ArrayList<Route> getRoutes() {
        if (routes == null) {
            routes = new ArrayList<Route>();
        }
        return routes;
    }

    void loadRoutes(ArrayList<Route> routes) {
        if (this.routes == null) {
            routes = new ArrayList<Route>();
        }




    }



    void addRoute(Route route) {
        if (this.routes == null) {
            routes = new ArrayList<Route>();
        }

        routes.add(route);

    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
