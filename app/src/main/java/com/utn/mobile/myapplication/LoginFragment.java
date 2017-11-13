package com.utn.mobile.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.utn.mobile.myapplication.domain.Usuario;
import com.utn.mobile.myapplication.service.SesionService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


public class LoginFragment extends Fragment {

    String nombreDeUsuario;
    String contraseña;
    Usuario usuarioLoggeado;
    Button iniciarSesion;
    MainActivity activity;
    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            i = 1;
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        iniciarSesion = (Button) view.findViewById(R.id.login_button);


        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    nombreDeUsuario = ((EditText) view.findViewById(R.id.login_username_edittext)).getText().toString();
                    contraseña = ((EditText) view.findViewById(R.id.login_password_edittext)).getText().toString();

                    if (nombreDeUsuario.isEmpty() && contraseña.isEmpty()){
                       throw new Exception(getString(R.string.empty_strings));
                    }

                    // mandar parametros a API

                    //SharedPreferences sharedPref = context.getSharedPreferences("session_data",Context.MODE_PRIVATE);
                    //int idUser = sharedPref.getInt("user_id", 0);


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
                new FindUsuario().execute();
                // Toast.makeText(getContext(), "ID usuario: " + usuarioLoggeado.getUserId(), Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }


    private class FindUsuario extends AsyncTask<Object, Object, Integer> {

        Usuario usuario;

        @Override
        protected void onPreExecute() {
            MainActivity activity = (MainActivity) getActivity();
            activity.showLoading();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                usuario = SesionService.get().login(nombreDeUsuario,contraseña);
                return TASK_RESULT_OK;
            } catch (Exception ex) {
                ex.printStackTrace();
                return TASK_RESULT_ERROR;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == TASK_RESULT_OK) {

                activity = (MainActivity) getActivity();
                if (activity == null) return;

                // setearViews(actor, activity);
                // createRecyclerView(actor.getPeliculas());
                activity.hideLoading();
                usuarioLoggeado = usuario;
                String token = PreferenceManager.getDefaultSharedPreferences(MovieGuruApplication.getAppContext()).getString("user-token", null);
                activity.changeDrawer(true);
                activity.findFavsForUser(usuario.getUserId(),true);
                setFragment(new RecomendacionesFragment(), false, null);
            }
        }

        public void setFragment(Fragment fragment, boolean toBackStack, String name ) {
            MainActivity mainActivity = (MainActivity) getActivity();
            FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            if(toBackStack) {
                ft.addToBackStack(name);
            }
            ft.commitAllowingStateLoss();
        }

    }

}
