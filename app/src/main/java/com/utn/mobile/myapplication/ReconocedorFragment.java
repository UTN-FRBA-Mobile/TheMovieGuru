package com.utn.mobile.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.SingleMovieService;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


public class ReconocedorFragment extends Fragment {

    private Integer mId;
    private ImageView movie_poster;
    private Pelicula peli = new Pelicula();
    private ProgressBar spinner;
    MainActivity main;

    public ReconocedorFragment() {
    }

    public static ReconocedorFragment newInstance(String param1, String param2) {
        ReconocedorFragment fragment = new ReconocedorFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mId = bundle.getInt("url");
        }
    }

    private class GetMovieInfo extends AsyncTask<Object, Object, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                peli = SingleMovieService.get().getOne(mId);
                return TASK_RESULT_OK;
            } catch (Exception ex) {
                ex.printStackTrace();
                return TASK_RESULT_ERROR;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == TASK_RESULT_OK) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null) return;

                setMovieInfo();
                main.hideLoading();

            }
        }

    }

    public void setMovieInfo(){
        ImageView imagen = (ImageView) getActivity().findViewById(R.id.imagen_reconocedor);
        Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w500"+ peli.getImg_poster()).into(imagen);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = peli.getId();
                PeliculaFragment peliculaFragment = PeliculaFragment.newInstance(id);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, peliculaFragment);
                transaction.addToBackStack("fragment_container");
                transaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reconocedor, container, false);
        main = (MainActivity) getActivity();
        main.showLoading();
        new GetMovieInfo().execute();
        return view;
    }

}
