package com.example.gonca.smtucky;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by filipemendes on 26/12/17.
 */


public class AddAlarmActivity extends AppCompatActivity implements ISelectedData, OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;
    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ((TextView) findViewById(R.id.textView2)).setText(getActualTime());

        setupRoutesSpinner();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_LOCATION);
    }

    private void setupRoutesSpinner() {
        // TODO: values to text showed up
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mock_data_for_recycler_view, android.R.layout.simple_spinner_item);
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
        Boolean[] daysToDrop = readValuesFromCheckbox();
        String name = ((TextInputLayout)findViewById(R.id.textInputLayout2)).getEditText().getText().toString();
        String route = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
        String time = ((TextView) findViewById(R.id.textView2)).getText().toString();

        Log.d("addAlarm", time);
/*
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        cal.setTime(sdf.parse("Mon Mar 14 16:02:37 GMT 2011"));// all done

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);
*/

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
