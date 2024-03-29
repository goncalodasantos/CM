package com.example.gonca.smtucky;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.Update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gonca on 28/12/2017.
 */

@Dao
public interface WarningDao {


    @Query("SELECT * FROM warning")
    List<Warning> getWarnings();

    @Query("SELECT * FROM warning WHERE userId = :userId")
    List<Warning> getWarningsByUser(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Warning warning);

    @Update
    void update(Warning warning);

    @Delete
    void delete(Warning warning);

    public class Converters {
        @TypeConverter
        public static ArrayList<String> fromString(String value) {
            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }
        @TypeConverter
        public static String fromArrayLisr(ArrayList<String> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }
}
