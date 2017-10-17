package com.utn.mobile.myapplication;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Imagen;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.ActorService;
import com.utn.mobile.myapplication.service.PeliculaService;
import com.utn.mobile.myapplication.service.SingleActorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


public class ActorFragment extends Fragment {
    private static final String ARG_ID = "id";

    private int mId;
    private int i = 1;
    private View mRootView;




    public ActorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ActorFragment newInstance(int id) {
        ActorFragment fragment = new ActorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            i = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_actor, container, false);
        new FindActor().execute();
        return mRootView;
    }

    private class FindActor extends AsyncTask<Object, Object, Integer> {
        Actor actor;

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                actor = SingleActorService.get().getOne(mId);
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

                setearViews(actor, activity);
                createRecyclerView(actor.getPeliculas());

            }
        }

    }

    private void setearViews(final Actor actor, MainActivity activity)
    {
        TextView nombreTV = (TextView) activity.findViewById(R.id.actor_name);
        nombreTV.setText(actor.getNombre());

        final TextView bioTV = (TextView) activity.findViewById(R.id.actor_bio);
        bioTV.setText(actor.getBiografia());

        final ImageView imagenIV = (ImageView) activity.findViewById(R.id.biografia_actor_image);

        fetchNextImage(imagenIV, actor, 0);


        imagenIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchNextImage(imagenIV, actor, i);
                i ++;
            }
        });

        final Button botonMas = (Button) activity.findViewById(R.id.button_bio);
        final Button botonMenos = (Button) activity.findViewById(R.id.button_bio_less);


        botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioTV.setMaxLines(1000);
                botonMas.setVisibility(View.GONE);
                botonMenos.setVisibility(View.VISIBLE);
            }
        });

        botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioTV.setMaxLines(16);
                botonMas.setVisibility(View.VISIBLE);
                botonMenos.setVisibility(View.GONE);
            }
        });


        ScrollView scrollViewActor = (ScrollView) activity.findViewById(R.id.scrollActor);
        scrollViewActor.setVisibility(View.VISIBLE);
    }

    private void fetchNextImage(ImageView imagenIV, Actor actor, int i)
    {
        List<Imagen> imagenes = actor.getImagenes();
        int cant_imagenes = imagenes.size();

        if(i>=cant_imagenes)
        {
            fetchNextImage(imagenIV, actor, (i-cant_imagenes));
        }
        else
        {
            String url_imagen = imagenes.get(i).getUrl();
            Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w500"+url_imagen).into(imagenIV);
        }
    }

    private class PeliViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;
        TextView movieName;
        CardView movieContainer;

        public PeliViewHolder(View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.actor_movie_poster);
            movieName = (TextView) itemView.findViewById(R.id.actor_movie_title);
            movieContainer = (CardView) itemView.findViewById(R.id.peliContainer);
        }
    }

    private class PRVAdapter extends RecyclerView.Adapter<PeliViewHolder>{

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
            return movies.size();
        }

        @Override
        public PeliViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor_movie_layout, viewGroup, false);
            PeliViewHolder pvh = new PeliViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PeliViewHolder pvh, int i) {
            final Pelicula item = movies.get(i);

            pvh.movieName.setText(item.getNombre());
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w342"+item.getImg_poster()).into(pvh.moviePoster);

            pvh.movieContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = item.getId();
                    PeliculaFragment peliculaFragment = PeliculaFragment.newInstance(id);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, peliculaFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    private void createRecyclerView(Collection<Pelicula> movies) {
        RecyclerView recyclerViewPeliculas = (RecyclerView) mRootView.findViewById(R.id.pelisRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPeliculas.setLayoutManager(layoutManager);

        PRVAdapter adapterPeliculas = new PRVAdapter(movies);
        recyclerViewPeliculas.setAdapter(adapterPeliculas);

    }

}
