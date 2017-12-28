package com.example.gonca.smtucky;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Created by gonca on 27/12/2017.
 */

public class CurrentDataModel extends ViewModel {
    private User user=null;
    private ArrayList<User> users=null;
    private ArrayList<Warning> warnings=null;


    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user=user;
    }


    public ArrayList<User> getUsers() {
        if(users==null){
            users=new ArrayList<User>();
        }
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Warning> getWarnings() {

        if(warnings==null){
            warnings=new ArrayList<Warning>();
        }
        return warnings;
    }

    public void setWarnings(ArrayList<Warning> warnings) {
        this.warnings = warnings;
    }
}
