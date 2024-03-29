package com.example.gonca.smtucky;

import android.arch.lifecycle.ViewModelProviders;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Routes routes = null;
    ArrayList<String> listOfRoutes = new ArrayList<String>();

    CurrentDataModel current_viewmodel = null;
    private UserDB user_db;
    private RouteDB route_db;
    private String currentUserEmail;
    private ArrayList<String> favorites;
    private RecyclerItemClickListener listTouchListener;


    public static final String PREFS_NAME = "MyPrefs";

    private class APIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //>handle the received broadcast message
            String success = intent.getStringExtra("success");

            Log.v("stuff-startup","Connected to API? :"+success);
            if(success.equals("yes")) {
                String value1 = intent.getStringExtra("response");
                try {
                    JSONObject stuff = new JSONObject(value1);
                    JSONArray dataarray = new JSONArray(stuff.get("data").toString());


                    for (int i = 0; i < dataarray.length(); i++) {


                        Route rt = new Route(dataarray.getJSONObject(i).get("route_official").toString(), dataarray.getJSONObject(i).get("route_name").toString(), Integer.parseInt(dataarray.getJSONObject(i).get("id").toString()));


                        ArrayList<Route> listOfRoutes = new ArrayList<>();


                        JSONArray dataarray2 = (JSONArray) dataarray.getJSONObject(i).get("hours");
                        ArrayList<String> times = new ArrayList<>();


                        for (int j = 0; j < dataarray2.length(); j++) {
                            times.add(dataarray2.getJSONObject(j).get("time").toString());
                        }


                        JSONArray dataarray3 = (JSONArray) dataarray.getJSONObject(i).get("points");
                        ArrayList<String> stops = new ArrayList<>();

                        for (int j = 0; j < dataarray3.length(); j++) {
                            stops.add(((JSONArray) dataarray.getJSONObject(i).get("points")).getString(j));
                        }


                        rt.setStops(stops);
                        rt.setTimes(times);


                        rt.setFrom(((JSONArray) dataarray.getJSONObject(i).get("points")).getString(0));
                        rt.setTo(((JSONArray) dataarray.getJSONObject(i).get("points")).getString(((JSONArray) dataarray.getJSONObject(i).get("points")).length() - 1));


                        try {
                            route_db.routeDAO().insert(rt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        routes.getRoutes().add(rt);

                    }


                    updateUIwithRoutes(context);
                    Log.v("stuff-startup","Loaded Routes from the API :"+routes.getRoutes().size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                    Log.v("stuff", "Can't connect to server");
                     updateUIwithRoutes(context);
            }



        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    protected void updateUIwithWarnings(Context context) {

        user_db = Room.databaseBuilder(getApplicationContext(),UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();
        List<User> listOfUsers = user_db.UserDAO().getUsers();
        current_viewmodel = ViewModelProviders.of(this).get(CurrentDataModel.class);
        current_viewmodel.setUsers((ArrayList<User>) listOfUsers);
        currentUserEmail=getIntent().getStringExtra("email");

        for (int j=0;j<current_viewmodel.getUsers().size();j++){
            if(current_viewmodel.getUsers().get(j).getMail().equals(currentUserEmail)){
                current_viewmodel.setUser(current_viewmodel.getUsers().get(j));
                favorites = current_viewmodel.getUser().getFavorites();
            }

        }


        List<Warning> listOfWarnings = user_db.WarningDao().getWarningsByUser(getUserId());
        current_viewmodel.setWarnings((ArrayList<Warning>) listOfWarnings);

        Log.v("stuff-debugging alarms",""+listOfWarnings.size());

        mAdapter = new ItemViewAdapter(new ArrayList<>(listOfWarnings));
        mRecyclerView.setAdapter(mAdapter);
        setListeners(1);

    }

    protected void updateUIwithFavorites(Context context) {

        user_db = Room.databaseBuilder(getApplicationContext(),UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();
        List<User> listOfUsers = user_db.UserDAO().getUsers();
        current_viewmodel = ViewModelProviders.of(this).get(CurrentDataModel.class);
        current_viewmodel.setUsers((ArrayList<User>) listOfUsers);
        currentUserEmail=getIntent().getStringExtra("email");

        for (int j=0;j<current_viewmodel.getUsers().size();j++){
            if(current_viewmodel.getUsers().get(j).getMail().equals(currentUserEmail)){
                current_viewmodel.setUser(current_viewmodel.getUsers().get(j));
                favorites = current_viewmodel.getUser().getFavorites();
            }

        }

        ArrayList<String> favoritess = new ArrayList<String>();
        ArrayList<Routes> a =  new ArrayList<>();


        for (int i = 0; i < routes.getRoutes().size(); i++) {
            for(int j=0;j < favorites.size(); j++) {
                String route = ""+routes.getRoutes().get(i).getId();
                if(route.equals(favorites.get(j))){
                    if (!routes.getRoutes().get(i).getRoute_name().equals("Desconhecido")) {

                        favoritess.add(routes.getRoutes().get(i).getRoute_name() + " - " + routes.getRoutes().get(i).getFrom() + " → " + routes.getRoutes().get(i).getTo());
                    } else {
                        favoritess.add(routes.getRoutes().get(i).getFrom() + " → " + routes.getRoutes().get(i).getTo());
                    }
                }
            }


        }
        mAdapter = new ItemViewAdapter(new ArrayList<>(favoritess));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.removeOnItemTouchListener(listTouchListener);

        listTouchListener=new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                current_viewmodel.getUser().getFavorites();

                int positionInGlobal= Integer.parseInt(current_viewmodel.getUser().getFavorites().get(position));



                Intent intent = new Intent(MainActivity.this, RouteActivity.class);
                intent.putExtra("email", currentUserEmail );

                Bundle b = new Bundle();

                if(positionInGlobal%2==0){
                    b.putSerializable("routeFrom", routes.getRoutes().get(positionInGlobal));
                    b.putSerializable("routeTo", routes.getRoutes().get(positionInGlobal+1));


                }
                else{
                    b.putSerializable("routeFrom", routes.getRoutes().get(positionInGlobal));
                    b.putSerializable("routeTo", routes.getRoutes().get(positionInGlobal-1));

                }

                intent.putExtras(b); //Put your id to your next Intent
                MainActivity.this.startActivity(intent);




            }
        });

        mRecyclerView.addOnItemTouchListener(listTouchListener);

    }

    protected void updateUIwithRoutes(Context context) {


        listOfRoutes = new ArrayList<String>();
        for (int i = 0; i < routes.getRoutes().size(); i++) {
            String route=routes.getRoutes().get(i).getRoute_name();

            if (!route.equals("Desconhecido")) {

                listOfRoutes.add(route+" - "+routes.getRoutes().get(i).getFrom()+" → "+routes.getRoutes().get(i).getTo());
            }
            else{
                listOfRoutes.add(routes.getRoutes().get(i).getFrom()+" → "+routes.getRoutes().get(i).getTo());
            }



        }
        mAdapter = new ItemViewAdapter(new ArrayList<>(listOfRoutes));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.removeOnItemTouchListener(listTouchListener);

        listTouchListener=new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
                Intent intent = new Intent(MainActivity.this, RouteActivity.class);
                intent.putExtra("email", currentUserEmail );

                Bundle b = new Bundle();

                if(position%2==0){
                    b.putSerializable("routeFrom", routes.getRoutes().get(position));
                    b.putSerializable("routeTo", routes.getRoutes().get(position+1));


                }
                else{
                    b.putSerializable("routeFrom", routes.getRoutes().get(position));
                    b.putSerializable("routeTo", routes.getRoutes().get(position-1));

                }

                intent.putExtras(b); //Put your id to your next Intent
                MainActivity.this.startActivity(intent);




            }
        });

        mRecyclerView.addOnItemTouchListener(listTouchListener);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("stuff","resuming");
        Log.v("stuff-startup","Populating ViewModel");

        user_db = Room.databaseBuilder(getApplicationContext(),UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();


        List<User> listOfUsers = user_db.UserDAO().getUsers();
        current_viewmodel = ViewModelProviders.of(this).get(CurrentDataModel.class);


        current_viewmodel.setUsers((ArrayList<User>) listOfUsers);


        currentUserEmail=getIntent().getStringExtra("email");



        for (int j=0;j<current_viewmodel.getUsers().size();j++){
            if(current_viewmodel.getUsers().get(j).getMail().equals(currentUserEmail)){
                current_viewmodel.setUser(current_viewmodel.getUsers().get(j));
                favorites = current_viewmodel.getUser().getFavorites();
            }
        }

        saveUserId();


        List<Warning> listOfWarnings = user_db.WarningDao().getWarningsByUser(current_viewmodel.getUser().getId());




        user_db.close();


        current_viewmodel.setWarnings((ArrayList<Warning>) listOfWarnings);



        Log.v("stuff-startup","Currently there are "+current_viewmodel.getUsers().size()+" users in the Room");
        Log.v("stuff-startup","Currently there are "+current_viewmodel.getWarnings().size()+" warnings in the Room");





        route_db = Room.databaseBuilder(getApplicationContext(),RouteDB.class, "routesxgxsassaaa").allowMainThreadQueries().build();


        ArrayList<Route> routesInDb = null;




        routesInDb = (ArrayList<Route>) route_db.routeDAO().getRoutes();



        Log.v("stuff-startup","Loaded Routes from the Room: " + routesInDb.size());

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        Log.v("stuff-startup","Loaded Routes from the Room: " + routesInDb.size());

        routes = ViewModelProviders.of(this).get(Routes.class);





        if(routesInDb.size()==0){

            IntentFilter filter = new IntentFilter();
            filter.addAction("action");
            registerReceiver(new APIReceiver(), filter);


            Intent intent = new Intent(this, ConnectAPI.class);
            intent.putExtra("decision", "getRoutes");

            startService(intent);//not startActivity!
        } else {
            routes.setRoutes(routesInDb);
        }

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),
        // MainActivity.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        final PageAdapter adapter = new PageAdapter (getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(0);

        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    viewPager.setCurrentItem(tab.getPosition());
                    updateUIwithRoutes(MainActivity.this);
                    adapter.refreshFragment(tab.getPosition());

                } else if (tab.getPosition() == 1) {
                    //viewPager.setCurrentItem(tab.getPosition());
                    //adapter.refreshFragment(tab.getPosition());
                    updateUIwithWarnings(MainActivity.this);



                } else if (tab.getPosition() == 2) {
                    //viewPager.setCurrentItem(tab.getPosition());
                    //adapter.refreshFragment(tab.getPosition());

                    //String[] mockPlanetsData = res.getStringArray(R.array.mock_data_for_recycler_view);

                    updateUIwithFavorites(MainActivity.this);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {                }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        setupRecycler();
        updateUIwithRoutes(this);


    }



    @Override
    public void onResume() {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onResume();
        Log.v("stuff","resuming");
        Log.v("stuff-startup","Populating ViewModel");

        user_db = Room.databaseBuilder(getApplicationContext(),UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();



        List<User> listOfUsers = user_db.UserDAO().getUsers();

        current_viewmodel = ViewModelProviders.of(this).get(CurrentDataModel.class);


        current_viewmodel.setUsers((ArrayList<User>) listOfUsers);

        currentUserEmail=getIntent().getStringExtra("email");

        for (int j=0;j<current_viewmodel.getUsers().size();j++){
            if(current_viewmodel.getUsers().get(j).getMail().equals(currentUserEmail)){
                current_viewmodel.setUser(current_viewmodel.getUsers().get(j));
                favorites = current_viewmodel.getUser().getFavorites();
            }
        }

        saveUserId();

        List<Warning> listOfWarnings = user_db.WarningDao().getWarnings();


        user_db.close();

        current_viewmodel.setWarnings((ArrayList<Warning>) listOfWarnings);



        Log.v("stuff-startup","Currently there are "+current_viewmodel.getUsers().size()+" users in the Room");
        Log.v("stuff-startup","Currently there are "+current_viewmodel.getWarnings().size()+" warnings in the Room");





        route_db = Room.databaseBuilder(getApplicationContext(),RouteDB.class, "routesxgxsassaaa").allowMainThreadQueries().build();


        ArrayList<Route> routesInDb = null;




        routesInDb = (ArrayList<Route>) route_db.routeDAO().getRoutes();



        Log.v("stuff-startup","Loaded Routes from the Room: " + routesInDb.size());

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        Log.v("stuff-startup","Loaded Routes from the Room: " + routesInDb.size());

        routes = ViewModelProviders.of(this).get(Routes.class);





        if(routesInDb.size()==0){

            IntentFilter filter = new IntentFilter();
            filter.addAction("action");
            registerReceiver(new APIReceiver(), filter);


            Intent intent = new Intent(this, ConnectAPI.class);
            intent.putExtra("decision", "getRoutes");

            startService(intent);//not startActivity!
        } else {
            routes.setRoutes(routesInDb);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        int tabposition=tabLayout.getSelectedTabPosition();

        if (tabposition == 0) {
            updateUIwithRoutes(MainActivity.this);

        } else if (tabposition== 1) {
            //viewPager.setCurrentItem(tab.getPosition());
            //adapter.refreshFragment(tab.getPosition());
            updateUIwithWarnings(MainActivity.this);



        } else if (tabposition == 2) {
            //viewPager.setCurrentItem(tab.getPosition());
            //adapter.refreshFragment(tab.getPosition());

            //String[] mockPlanetsData = res.getStringArray(R.array.mock_data_for_recycler_view);

            updateUIwithFavorites(MainActivity.this);
        }





        setupRecycler();


    }


    private void setListeners(int tab) {
        mRecyclerView.removeOnItemTouchListener(listTouchListener);
        switch(tab) {
            case 0:
                listTouchListener=new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, RouteActivity.class);
                        Bundle b = new Bundle();

                        if(position%2==0){
                            b.putSerializable("routeFrom", routes.getRoutes().get(position));
                            b.putSerializable("routeTo", routes.getRoutes().get(position+1));


                        }
                        else{
                            b.putSerializable("routeFrom", routes.getRoutes().get(position));
                            b.putSerializable("routeTo", routes.getRoutes().get(position-1));

                        }

                        intent.putExtras(b); //Put your id to your next Intent
                        MainActivity.this.startActivity(intent);
                    }
                });
                break;
            case 1:
                listTouchListener=new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                        intent.putExtra("alarm", user_db.WarningDao().getWarningsByUser(getUserId()).get(position));
                        intent.putExtra("isEditable", true);
                        MainActivity.this.startActivity(intent);
                    }
                });
                break;
            case 2:
                break;
        }
        mRecyclerView.addOnItemTouchListener(listTouchListener);
    }

    private void saveUserId() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putInt("userId", current_viewmodel.getUser().getId()).commit();
    }

    public int getUserId() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt("userId", 0);
    }

    private void setupRecycler() {
        Resources res = getResources();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mRecyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.logout:
                LoginManager.getInstance().logOut();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
                return true;


            case R.id.add_alarm:

                intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                intent.putExtra("email", currentUserEmail );
                MainActivity.this.startActivity(intent);
                return true;

            case R.id.about:
                View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

                // When linking text, force to always use default color. This works
                // around a pressed color state bug.

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher_round);
                builder.setTitle(R.string.app_name);
                builder.setView(messageView);
                builder.create();
                builder.show();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}





