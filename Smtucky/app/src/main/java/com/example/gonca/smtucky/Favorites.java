package com.example.gonca.smtucky;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Favorites extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Routes routes = null;

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    protected void updateUI(Context context) {


        //mAdapter = new ItemViewAdapter(new ArrayList<>(listOfRoutes));
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(Favorites.this, RouteActivity.class);
                        startActivity(i);


                    }
                })
        );

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //routes = ViewModelProviders.of(this).get(Routes.class);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        //ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //tabLayout.setupWithViewPager(viewPager);

        setupRecycler(Favorites.this);

    }

    private void setupRecycler(Context context) {
        Resources res = getResources();
        String[] mockPlanetsData = res.getStringArray(R.array.mock_data_for_recycler_view);

        mAdapter = new ItemViewAdapter(new ArrayList<>(Arrays.asList(mockPlanetsData)));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(Favorites.this, RouteActivity.class);
                        startActivity(i);


                    }
                })
        );
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Favorites.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Favorites.this.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }
}





