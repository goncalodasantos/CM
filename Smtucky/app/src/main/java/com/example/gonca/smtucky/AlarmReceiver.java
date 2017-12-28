package com.example.gonca.smtucky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by filipemendes on 28/12/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        Toast.makeText(k1, "Alarm received!", Toast.LENGTH_LONG).show();

    }

}
