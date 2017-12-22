package com.example.gonca.smtucky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private class APIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent){
        //>handle the received broadcast message

            String value1 = intent.getStringExtra("routenumber");
            Log.d("stuff", value1);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        IntentFilter filter =new IntentFilter();
        filter.addAction("action");
        registerReceiver(new APIReceiver(),filter);


        Intent intent =new Intent(this,ConnectAPI.class);
        intent.putExtra("decision","getRoutes");
        intent.putExtra("routenumber","6");

        startService(intent);//not startActivity!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }




}


