package com.example.gonca.smtucky;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.Login;
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
    private JSONArray j;
    private List<String> listContents;

    private class APIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent){
        //>handle the received broadcast message

            String value1 = intent.getStringExtra("routenumber");
            //Toast.makeText(context, value1, Toast.LENGTH_SHORT).show();
            Log.d("Rogerio", value1);

            try {
                //String route;
                //j =  new JSONArray(value1);
                JSONObject jsonObj = new JSONObject(value1);
                JSONArray data = jsonObj.getJSONArray("data");
                int length = data.length();
                listContents = new ArrayList<String>(length);
                String aux= "aux";
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    String route = c.getString("route_name");
                    if(!aux.equals(route)){
                        if(!route.equals("Desconhecido")){
                            Log.d("Bus number: ", route);
                            listContents.add(route);
                        }
                        aux=route;
                    }


                }
                mAdapter = new ItemViewAdapter(new ArrayList<>((listContents)));
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setupRecycler();
    }

    private void setupRecycler() {
        Resources res = getResources();
        //aqui
        //String[] mockPlanetsData = a;
        //String[] mockPlanetsData = res.getStringArray(mock_data_for_recycler_view);

        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.


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
        switch (item.getItemId()) {
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}


