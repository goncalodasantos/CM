package com.example.gonca.smtucky;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class WarningsFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int page;

    public static WarningsFragment newInstance(int page) {
        Bundle args = new Bundle();
        Log.d("DEBUG", ""+page);
        args.putInt(ARG_PAGE, page);
        WarningsFragment fragment = new WarningsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 2;
    }
    public void updateFragment(){
        Resources res = getResources();
        String[] mockPlanetsData = res.getStringArray(R.array.mock_data_for_recycler_view);
        if (getView() != null) {
            mRecyclerView = getView().findViewById(R.id.my_recycler_view);
            layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            Log.d("replaicing array!!!", "debug");
            mAdapter = new ItemViewAdapter(new ArrayList<>(Arrays.asList(mockPlanetsData)));
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warnings, container, false);
        return view;
    }
}