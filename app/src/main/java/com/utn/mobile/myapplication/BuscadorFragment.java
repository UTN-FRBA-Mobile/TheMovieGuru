package com.utn.mobile.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


public class BuscadorFragment extends Fragment {

    public BuscadorFragment() {
    }

    public static BuscadorFragment newInstance(String param1, String param2) {
        BuscadorFragment fragment = new BuscadorFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //args
        }
    }

    public void setTabInfo(View view){
        TabHost tabHost = (TabHost) view.findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Peliculas");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Actores");

        tab1.setIndicator("Peliculas");
        tab1.setContent(R.id.peliculas);

        tab2.setIndicator("Actores");
        tab2.setContent(R.id.actores);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscador, container, false);
        setTabInfo(view);
        return view;
    }

}
