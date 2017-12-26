package com.example.gonca.smtucky;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ana_f on 26/12/2017.
 */

public class Tab extends android.support.v4.app.Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";

    public Tab(){

    }
    public static Tab newInstance(int page) {
        Tab frag = new Tab();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_route, container, false);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
