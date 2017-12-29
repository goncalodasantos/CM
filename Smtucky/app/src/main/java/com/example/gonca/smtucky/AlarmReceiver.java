package com.example.gonca.smtucky;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.concurrent.Executor;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Math.abs;

/**
 * Created by filipemendes on 28/12/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private int id = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;
    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private final float RAZOABLE_DISTANCE_TO_DROP = 2000;
    private Warning w;
    @Override
    public void onReceive(Context k1, Intent k2) {

        final Context a = k1;
        final Intent b = k2;

        w = (Warning) k2.getExtras().getBundle("bundle").getSerializable("alarm");
        if(w != null) {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(k1);
            try {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.

                                if (location != null && compareLocation(a, location, w.getLat(), w.getLon())) {
                                    // Logic to handle location object
                                    sendNotification(a, b);
                                }

                            }
                        });

            } catch (SecurityException e) {
                Toast.makeText(a, "Unable to get your current location", Toast.LENGTH_SHORT);
            }
        } 
    }

    private Boolean compareLocation(Context k1, Location l1, double lat, double lon) {

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat-l1.getLatitude());
        double dLng = Math.toRadians(lon-l1.getLongitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(l1.getLatitude())) * Math.cos(Math.toRadians(lat)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        if(dist <= RAZOABLE_DISTANCE_TO_DROP) {
            return true;
        } else {
            return false;
        }
    }

    private void sendNotification(Context k1, Intent k2) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(k1)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("SMTUCky Watch Out!")
                        .setContentText("Pick up your bus!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(k1, AddAlarmActivity.class);

        resultIntent.putExtra("alarm", k2.getExtras().getBundle("bundle").getSerializable("alarm"));
        resultIntent.putExtra("isEditable", false);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(k1);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) k1.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
    }
}
