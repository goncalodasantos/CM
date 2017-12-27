package com.example.gonca.smtucky;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by gonca on 27/12/2017.
 */

@Database(entities = {Route.class},version = 1)
@TypeConverters({RouteDAO.Converters.class})
public abstract class RouteDB extends RoomDatabase {
    public abstract RouteDAO routeDAO();

}