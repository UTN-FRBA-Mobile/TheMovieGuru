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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.ActorEnPelicula;
import com.utn.mobile.myapplication.domain.Imagen;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.ActorService;
import com.utn.mobile.myapplication.service.PeliculaService;
import com.utn.mobile.myapplication.service.SingleActorService;
import com.utn.mobile.myapplication.service.SingleMovieService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


public class PeliculaFragment extends Fragment {
    private static final String ARG_ID = "id";

    private int mId;
    private View mRootView;




    public PeliculaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PeliculaFragment newInstance(int id) {
        PeliculaFragment fragment = new PeliculaFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_pelicula, container, false);
        new FindMovie().execute();
        return mRootView;
    }

    private class FindMovie extends AsyncTask<Object, Object, Integer> {
        Pelicula peli;

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

                setearViews(peli, activity);
                createRecyclerView(peli.getCast());

            }
        }

    }

    private void setearViews(final Pelicula peli, MainActivity activity)
    {
        TextView nombreTV = (TextView) activity.findViewById(R.id.movie_name);
        nombreTV.setText(peli.getNombre());

        TextView taglineTV = (TextView) activity.findViewById(R.id.movie_tagline);
        taglineTV.setText(peli.getTagline());

        TextView plotTV = (TextView) activity.findViewById(R.id.movie_plot);
        plotTV.setText(peli.getOverview());

        final ImageView imagenIV = (ImageView) activity.findViewById(R.id.movie_image);
        Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w500"+peli.getImg_poster()).into(imagenIV);

        ImageView backIV = (ImageView) activity.findViewById(R.id.back_image);
        Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w1280"+peli.getImg_backdrop()).into(backIV);

        ScrollView scrollViewPeli = (ScrollView) activity.findViewById(R.id.scrollMovie);
        scrollViewPeli.setVisibility(View.VISIBLE);
    }

    private class ActorViewHolder extends RecyclerView.ViewHolder {

        TextView actorName;
        TextView actorCharacter;
        CardView actorContainer;

        TextView actorName2;
        TextView actorCharacter2;
        CardView actorContainer2;

        public ActorViewHolder(View itemView) {
            super(itemView);

            actorName = (TextView) itemView.findViewById(R.id.movie_actor_name);
            actorCharacter = (TextView) itemView.findViewById(R.id.movie_actor_character);
            actorContainer = (CardView) itemView.findViewById(R.id.actorContainer);
            actorName2 = (TextView) itemView.findViewById(R.id.movie_actor_name2);
            actorCharacter2 = (TextView) itemView.findViewById(R.id.movie_actor_character2);
            actorContainer2 = (CardView) itemView.findViewById(R.id.actorContainer2);
        }
    }

    private class ARVAdapter extends RecyclerView.Adapter<ActorViewHolder>{

        List<ActorEnPelicula> actors;

        public ARVAdapter(Collection<ActorEnPelicula> actors) {
            //Nuevas instancias para evitar modificar listas originales referenciadas
            this.actors = new ArrayList(actors);
        }

        public void update(Collection<ActorEnPelicula> actors) {
            this.actors.clear();
            this.actors.addAll(actors);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return actors.size()/2;
        }

        @Override
        public ActorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.peli_actor_layout, viewGroup, false);
            ActorViewHolder avh = new ActorViewHolder(v);
            return avh;
        }

        @Override
        public void onBindViewHolder(ActorViewHolder avh, int i) {

                final ActorEnPelicula item1 = actors.get(i * 2);

                avh.actorName.setText(item1.getNombre());
                avh.actorCharacter.setText(item1.getCharacter());
                avh.actorContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = item1.getId();
                        ActorFragment peliculaFragment = ActorFragment.newInstance(id);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, peliculaFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

            if (i * 2 + 1 <= actors.size()) {

                final ActorEnPelicula item2 = actors.get(i * 2 + 1);
                avh.actorName2.setText(item2.getNombre());
                avh.actorCharacter2.setText(item2.getCharacter());
                avh.actorContainer2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = item2.getId(); //usarlo para el get, hacer como el actor fragment
                        ActorFragment peliculaFragment = ActorFragment.newInstance(id);
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

    private void createRecyclerView(Collection<ActorEnPelicula> actors) {
        RecyclerView recyclerViewPeliculas = (RecyclerView) mRootView.findViewById(R.id.actorsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPeliculas.setLayoutManager(layoutManager);

        ARVAdapter adapterPeliculas = new ARVAdapter(actors);
        recyclerViewPeliculas.setAdapter(adapterPeliculas);

    }

}
