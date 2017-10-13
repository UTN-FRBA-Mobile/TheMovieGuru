package com.utn.mobile.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.utn.mobile.myapplication.domain.Usuario;
import com.utn.mobile.myapplication.service.SesionService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class LoginFragment extends Fragment {

    public LoginFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            //args
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button iniciarSesion = (Button) view.findViewById(R.id.login_button);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    String nombreDeUsuario = ((EditText) view.findViewById(R.id.login_username_edittext)).getText().toString();
                    String contraseña = ((EditText) view.findViewById(R.id.login_password_edittext)).getText().toString();

                    if (nombreDeUsuario.isEmpty() && contraseña.isEmpty()){
                       throw new Exception(getString(R.string.empty_strings));
                    }

                    // mandar parametros a API
                    Usuario u = SesionService.get().login(nombreDeUsuario,contraseña);



                }catch (Exception e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.login_text)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {}
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                //SharedPreferences sharedPref = context.getSharedPreferences("session_data",Context.MODE_PRIVATE);
                //int idUser = sharedPref.getInt("user_id", 0);
            }
        });
        return view;
    }


}
