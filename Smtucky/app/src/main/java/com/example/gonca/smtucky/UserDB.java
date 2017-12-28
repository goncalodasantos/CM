package com.example.gonca.smtucky;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by gonca on 27/12/2017.
 */

@Database(entities = { Warning.class, User.class},version = 1)
@TypeConverters({UserDAO.Converters.class})
public abstract class UserDB extends RoomDatabase {
    public abstract UserDAO UserDAO();
    public abstract WarningDao WarningDao();
}
