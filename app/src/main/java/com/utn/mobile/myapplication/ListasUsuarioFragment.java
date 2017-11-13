package com.utn.mobile.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class ListasUsuarioFragment extends Fragment {

    MainActivity activity;
    private View mRootView;
    private String listName;

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
        FloatingActionButton btnAddList = (FloatingActionButton) mRootView.findViewById(R.id.newListButton);
        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Nueva lista");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.new_list_dialog_layout, (ViewGroup) getView(), false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input_text);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listName = input.getText().toString();
                        Toast.makeText(activity, listName, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return mRootView;
    }

}
