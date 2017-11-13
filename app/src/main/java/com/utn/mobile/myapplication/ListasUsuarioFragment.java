package com.utn.mobile.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ListasUsuarioFragment extends Fragment {

    MainActivity activity;
    private View mRootView;

    public ListasUsuarioFragment() {
    }

    public static ListasUsuarioFragment newInstance(String param1, String param2) {
        ListasUsuarioFragment fragment = new ListasUsuarioFragment();
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

    @Override
    public void onResume() {
        activity.hideToolbarButton();
        activity.setToolbarTitle(R.string.event_list_title);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        activity.setToolbarButtonDefault();
        activity.setToolbarTitle(R.string.app_name);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_listas_usuario, container, false);
        activity = (MainActivity) getActivity();
        return mRootView;
    }

}
