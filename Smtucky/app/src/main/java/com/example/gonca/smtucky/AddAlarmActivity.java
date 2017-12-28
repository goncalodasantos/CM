package com.example.gonca.smtucky;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by filipemendes on 26/12/17.
 */


public class AddAlarmActivity extends AppCompatActivity implements ISelectedData, OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;
    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    final static int RQS_1 = 1;
    private Routes routes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);

        // TO DO : Change to alarm model
        Calendar b = (Calendar) getIntent().getExtras().getSerializable("alarm");

        if(b != null) {
            setupInfoToEdit(b);
        } else {
            ((TextView) findViewById(R.id.textView2)).setText(getActualTime());
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // TO DO : Change to alarm model
        Calendar b = (Calendar) getIntent().getExtras().getSerializable("alarm");

        if(b != null) {
            setupInfoToEdit(b);
        } else {
            ((TextView) findViewById(R.id.textView2)).setText(getActualTime());
        }

        setupRoutesSpinner();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_LOCATION);
    }

    private void setupInfoToEdit(Calendar alarm) {
/*
        Boolean[] daysToDrop = readValuesFromCheckbox();
        String name = ((TextInputLayout)findViewById(R.id.textInputLayout2)).getEditText().getText().toString();
        String route = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
*/
        Log.d("alarm", new SimpleDateFormat("hh:mm aa").format(alarm.getTime()));
        ((TextView) findViewById(R.id.textView2)).setText(new SimpleDateFormat("hh:mm aa").format(alarm.getTime()));
    }

    private void setupRoutesSpinner() {
        RouteDB route_db = Room.databaseBuilder(getApplicationContext(), RouteDB.class, "routesxgxsassa").allowMainThreadQueries().build();
        ArrayList<Route> routesInDb = null;
        routesInDb = (ArrayList<Route>) route_db.routeDAO().getRoutes();
        routes = ViewModelProviders.of(this).get(Routes.class);
        routes.setRoutes(routesInDb);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Log.d("routes", Integer.toString(routes.getRoutes().size()));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Route> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, routes.getRoutes());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void setupMap() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                AddAlarmActivity.this.location = location;
                                SupportMapFragment mapFragment =
                                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                mapFragment.getMapAsync(AddAlarmActivity.this);
                            }
                        }
                    });
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Unable to get your current location", Toast.LENGTH_SHORT);
        }

    }

    private String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");

        return dateFormat2.format(c.getTime());
    }

    public void openTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putString("time", ((TextView) findViewById(R.id.textView2)).getText().toString());
        newFragment.setArguments(args);

        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onSelectedData(String myValue) {
        Log.d("Picker", myValue);
        ((TextView) findViewById(R.id.textView2)).setText(myValue);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    setupMap();
                    findViewById(R.id.textView).setVisibility(View.INVISIBLE);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission to read location not granted", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);

        map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude())) // Center Set
                .zoom(15.0f)                // Zoom
                .bearing(0)                // Orientation of the camera to east
                .tilt(30)                   // Tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void addAlarm(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("It's loading....");
        progressDialog.setTitle("Adding warning");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run(){
                Warning toAdd = new Warning();

                Boolean[] daysToDrop = readValuesFromCheckbox();

                for(int i = 0; i < daysToDrop.length; i++) {
                    switch(i) {
                        case 0:
                            toAdd.setMonday(daysToDrop[i] ? 1 : 0);
                            break;
                        case 1:
                            toAdd.setTuesday(daysToDrop[i] ? 1 : 0);
                            break;
                        case 2:
                            toAdd.setWednesday(daysToDrop[i] ? 1 : 0);
                            break;
                        case 3:
                            toAdd.setThursday(daysToDrop[i] ? 1 : 0);
                            break;
                        case 4:
                            toAdd.setFriday(daysToDrop[i] ? 1 : 0);
                            break;
                        case 5:
                            toAdd.setSaturday(daysToDrop[i] ? 1 : 0);
                            break;
                        case 6:
                            toAdd.setSunday(daysToDrop[i] ? 1 : 0);
                            break;
                    }
                }

                toAdd.setName(((TextInputLayout)findViewById(R.id.textInputLayout2)).getEditText().getText().toString());
                toAdd.setRoute(((Route)((Spinner) findViewById(R.id.spinner)).getSelectedItem()).getId());
                // TODO
                toAdd.setUserId(1);
                String time = ((TextView) findViewById(R.id.textView2)).getText().toString();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                try {
                    cal.setTime(sdf.parse(time));
                } catch (ParseException e) {

                }

                if(cal.get(Calendar.AM_PM) == 1) {
                    toAdd.setHour(cal.get(Calendar.HOUR) + 12);
                } else {
                    toAdd.setMinute(cal.get(Calendar.MINUTE));
                }

                toAdd.setLat(location.getLatitude());
                toAdd.setLon(location.getLongitude());

        /*
                UserDB user_db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "userxgxssxs").allowMainThreadQueries().build();

                User u = new User();
                u.setMail("oi@gmi");

                user_db.UserDAO().insert(u);
                user_db.close();
                user_db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "userxgxssxs").allowMainThreadQueries().build();
                user_db.WarningDao().insert(toAdd);
        */

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", toAdd);
                intent.putExtra("bundle", bundle);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getBaseContext(), RQS_1, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);

                progressDialog.dismiss();
                AddAlarmActivity.super.onBackPressed();
            }
        });
    }

    public Boolean[] readValuesFromCheckbox() {
        Boolean[] toReturn = new Boolean[7];
        toReturn[0] = ((CheckBox) findViewById(R.id.checkBox5)).isChecked();
        toReturn[1] = ((CheckBox) findViewById(R.id.checkBox3)).isChecked();
        toReturn[2] = ((CheckBox) findViewById(R.id.checkBox4)).isChecked();
        toReturn[3] = ((CheckBox) findViewById(R.id.checkBox)).isChecked();
        toReturn[4] = ((CheckBox) findViewById(R.id.checkBox6)).isChecked();
        toReturn[5] = ((CheckBox) findViewById(R.id.checkBox7)).isChecked();
        toReturn[6] = ((CheckBox) findViewById(R.id.checkBox8)).isChecked();
        return toReturn;
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                LoginManager.getInstance().logOut();
                intent = new Intent(AddAlarmActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AddAlarmActivity.this.startActivity(intent);
                return true;


            case R.id.add_alarm:
                intent = new Intent(AddAlarmActivity.this, AddAlarmActivity.class);
                AddAlarmActivity.this.startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
