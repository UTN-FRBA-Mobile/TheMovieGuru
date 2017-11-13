package com.utn.mobile.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.PeliculaService;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


public class ListaFragment extends Fragment {

    private View mRootView;
    private List<Pelicula> peliculas = new ArrayList<>();

    public ListaFragment() {
    }

    public static ListaFragment newInstance(String param1) {
        ListaFragment fragment = new ListaFragment();
        Bundle args = new Bundle();
        args.putString("peliculitas", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String pelis = getArguments().getString("peliculitas");
            Gson gson = new Gson();
            peliculas = gson.fromJson(pelis, new TypeToken<List<Pelicula>>(){}.getType());//args
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        createRecyclerView(peliculas);
        return mRootView;
    }

    private class PeliViewHolder extends RecyclerView.ViewHolder {

        TextView movieName1;
        CardView movieContainer1;
        ImageView moviePoster1;
        TextView movieName2;
        CardView movieContainer2;
        ImageView moviePoster2;

        public PeliViewHolder (View itemView) {
            super(itemView);

            movieName1 = (TextView) itemView.findViewById(R.id.reco_title1);
            movieContainer1 = (CardView) itemView.findViewById(R.id.recoContainer1);
            moviePoster1 = (ImageView) itemView.findViewById(R.id.reco_poster1);
            movieName2 = (TextView) itemView.findViewById(R.id.reco_title2);
            movieContainer2 = (CardView) itemView.findViewById(R.id.recoContainer2);
            moviePoster2 = (ImageView) itemView.findViewById(R.id.reco_poster2);
        }
    }

    private class PRVAdapter extends RecyclerView.Adapter<PeliViewHolder> {

        List<Pelicula> movies;

        public PRVAdapter(Collection<Pelicula> movies) {
            //Nuevas instancias para evitar modificar listas originales referenciadas
            this.movies = new ArrayList(movies);
        }

        public void update(Collection<Pelicula> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if ((movies.size() % 2) == 0)
            {
                return movies.size() / 2;
            }
            else
            {
                return movies.size() / 2 + 1;
            }
        }

        @Override
        public PeliViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reco_item_layout, viewGroup, false);
            PeliViewHolder pvh = new PeliViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PeliViewHolder pvh, int i) {

            final Pelicula item1 = movies.get(i * 2);

            //String year = item1.getYear().substring(0,4);
            pvh.movieName1.setText(item1.getNombre());// + " ("+ year +")");

            String imgPath1 = item1.getImg_poster();
            if (imgPath1.equals("null"))
            {
                Picasso.with(getContext()).load(R.drawable.batman_300).into(pvh.moviePoster1);
            }
            else
            {
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+imgPath1).into(pvh.moviePoster1);
            }
            pvh.movieContainer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = item1.getId();
                    PeliculaFragment peliculaFragment = PeliculaFragment.newInstance(id);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, peliculaFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            if (i * 2 + 1 < movies.size()) {
                final Pelicula item2 = movies.get(i * 2 + 1);

                pvh.movieContainer2.setVisibility(View.VISIBLE);

                //String year2 = item1.getYear().substring(0,4);
                pvh.movieName2.setText(item2.getNombre());// + " ("+ year2 +")");

                String imgPath2 = item2.getImg_poster();
                if (imgPath2.equals("null"))
                {
                    Picasso.with(getContext()).load(R.drawable.batman_300).into(pvh.moviePoster2);
                }
                else
                {
                    Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+imgPath2).into(pvh.moviePoster2);
                }

                pvh.movieContainer2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = item2.getId();
                        PeliculaFragment peliculaFragment = PeliculaFragment.newInstance(id);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, peliculaFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    private void createRecyclerView(Collection<Pelicula> movies) {
        RecyclerView recyclerViewRecos = (RecyclerView) mRootView.findViewById(R.id.recoRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRecos.setLayoutManager(layoutManager);

        PRVAdapter adapterPeliculas = new PRVAdapter(movies);
        recyclerViewRecos.setAdapter(adapterPeliculas);
    }

}
