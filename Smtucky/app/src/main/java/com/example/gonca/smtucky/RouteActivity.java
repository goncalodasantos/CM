package com.example.gonca.smtucky;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class RouteActivity extends AppCompatActivity {

    private Route routeFrom, routeTo;
    private RecyclerView mRecyclerView1, mRecyclerView2;
    private RecyclerView.Adapter mAdapter1, mAdapter2;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        routeFrom = (Route) getIntent().getExtras().getSerializable("routeFrom");
        routeTo = (Route) getIntent().getExtras().getSerializable("routeTo");





        ((TextView) findViewById(R.id.textView7)).setText(routeFrom.getRoute_name());
        ((TextView) findViewById(R.id.textView9)).setText(routeFrom.getFrom());
        ((TextView) findViewById(R.id.textView10)).setText(routeFrom.getTo());

        setupRecyclers();
    }

    private void setupRecyclers() {


        if(state == 0) {
            mAdapter1 = new ItemViewAdapter(new ArrayList<>(routeFrom.getTimes()));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            mRecyclerView1 = findViewById(R.id.recyclerView1);

            mRecyclerView1.setLayoutManager(layoutManager1);

            mRecyclerView1.setAdapter(mAdapter1);

            // Configurando um dividr entre linhas, para uma melhor visualização.
            mRecyclerView1.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
            mAdapter2 = new ItemViewAdapter(new ArrayList<>(routeFrom.getStops()));

            mRecyclerView2 = findViewById(R.id.recyclerView2);

            mRecyclerView2.setLayoutManager(layoutManager2);
            mRecyclerView2.setAdapter(mAdapter2);

            // Configurando um dividr entre linhas, para uma melhor visualização.
            mRecyclerView2.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        } else {
            mAdapter1 = new ItemViewAdapter(new ArrayList<>(routeTo.getTimes()));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            mRecyclerView1 = findViewById(R.id.recyclerView1);

            mRecyclerView1.setLayoutManager(layoutManager1);

            mRecyclerView1.setAdapter(mAdapter1);

            // Configurando um dividr entre linhas, para uma melhor visualização.
            mRecyclerView1.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
            mAdapter2 = new ItemViewAdapter(new ArrayList<>(routeTo.getStops()));

            mRecyclerView2 = findViewById(R.id.recyclerView2);

            mRecyclerView2.setLayoutManager(layoutManager2);
            mRecyclerView2.setAdapter(mAdapter2);

            // Configurando um dividr entre linhas, para uma melhor visualização.
            mRecyclerView2.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        }
    }

    public void tradeRoutes(View view) {
        if(state == 0) {
            state = 1;
            ((TextView) findViewById(R.id.textView7)).setText(routeTo.getRoute_name());
            ((TextView) findViewById(R.id.textView9)).setText(routeTo.getFrom());
            ((TextView) findViewById(R.id.textView10)).setText(routeTo.getTo());
        } else {
            state = 0;
            ((TextView) findViewById(R.id.textView7)).setText(routeFrom.getRoute_name());
            ((TextView) findViewById(R.id.textView9)).setText(routeFrom.getFrom());
            ((TextView) findViewById(R.id.textView10)).setText(routeFrom.getTo());
        }
        setupRecyclers();
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
                intent = new Intent(RouteActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RouteActivity.this.startActivity(intent);
                return true;


            case R.id.add_alarm:
                intent = new Intent(RouteActivity.this, AddAlarmActivity.class);
                RouteActivity.this.startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
