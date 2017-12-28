package com.example.gonca.smtucky;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Created by gonca on 27/12/2017.
 */

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mail;
    private ArrayList<String> favorites;



    public User(){

    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
