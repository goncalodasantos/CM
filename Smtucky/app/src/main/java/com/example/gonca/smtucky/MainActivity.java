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
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        setupRecycler();
    }

    private void setupRecycler() {
        Resources res = getResources();
        String[] mockPlanetsData = res.getStringArray(R.array.mock_data_for_recycler_view);

        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new ItemViewAdapter(new ArrayList<>(Arrays.asList(mockPlanetsData)));
        mRecyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }




}


