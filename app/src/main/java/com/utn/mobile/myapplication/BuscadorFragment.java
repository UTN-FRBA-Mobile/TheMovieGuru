package com.utn.mobile.myapplication;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;


import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.ActorService;
import com.utn.mobile.myapplication.service.PeliculaService;
import com.utn.mobile.myapplication.utils.GlobalConstants;
import com.utn.mobile.myapplication.utils.Keywords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.utn.mobile.myapplication.utils.GlobalConstants.*;


public class BuscadorFragment extends Fragment {

    private View mRootView;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_buscador, container, false);
        setTabInfo(mRootView);
        new PopulateListTask().execute();
        return mRootView;
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



    private void createRecyclerView(List<Actor> actors, List<Pelicula> movies) {
        RecyclerView recyclerViewActores = (RecyclerView) mRootView.findViewById(R.id.actorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewActores.setLayoutManager(layoutManager);

        RecyclerView recyclerViewPeliculas = (RecyclerView) mRootView.findViewById(R.id.peliculaRecyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerViewPeliculas.setLayoutManager(layoutManager2);


        ARVAdapter adapterActores = new ARVAdapter(actors);
        recyclerViewActores.setAdapter(adapterActores);

        MRVAdapter adapterPeliculas = new MRVAdapter(movies);
        recyclerViewPeliculas.setAdapter(adapterPeliculas);

    }

    private void openActorDescription(Actor actor) {
        //Intent intent = new Intent(getContext(), ActorDescriptionActivity.class);
        //intent.putExtra(Keywords.ACTOR, actor);
        //startActivity(intent);
    }

    public void onSubmitSearchQuery(SearchView searchView, String query) {
        MainActivity activity = (MainActivity) getActivity();
        //List<Actor> actors = activity.getPoints();
        Actor actor = null;

        //hacer la query con el input
    }

    public void onFilterActivityCallback(Intent data) {
        new PopulateListTask().execute();
    }

    private class PopulateListTask extends AsyncTask<Object, Object, Integer> {
        List<Actor> actors;
        List<Pelicula> movies;

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                //List<SearchCondition> searchConditions = getActivity().getSearchConditions();
                MainActivity activity = (MainActivity) getActivity();
                String query = activity.getQuery();
                actors = ActorService.get().getAll(query);
                movies = PeliculaService.get().getAll(query);
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
                activity.setActors(actors);
                createRecyclerView(actors, movies);
            }
        }

    }

    private class ActorViewHolder extends RecyclerView.ViewHolder {

        CardView itemContainer;
        ImageView itemImage;
        TextView itemName;
        TextView itemContent;

        public ActorViewHolder(View itemView) {
            super(itemView);

            itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
            itemName = (TextView) itemView.findViewById(R.id.listItemName);
            itemContent = (TextView) itemView.findViewById(R.id.listItemContent);
            itemImage = (ImageView) itemView.findViewById(R.id.listItemImage);
        }

    }

    private class PeliculaViewHolder extends RecyclerView.ViewHolder {

        CardView itemContainer;
        TextView itemName;
        TextView itemContent;

        public PeliculaViewHolder(View itemView) {
            super(itemView);

            itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
            itemName = (TextView) itemView.findViewById(R.id.listItemName);
            itemContent = (TextView) itemView.findViewById(R.id.listItemContent);
        }
    }

    private class ARVAdapter extends RecyclerView.Adapter<ActorViewHolder>{

        List<Actor> actors;

        public ARVAdapter(List<Actor> actors) {
            //Nuevas instancias para evitar modificar listas originales referenciadas
            this.actors = new ArrayList(actors);
        }

        public void update(List<Actor> actors) {
            this.actors.clear();
            this.actors.addAll(actors);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return actors.size();
        }

        @Override
        public ActorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor_item_layout, viewGroup, false);
            ActorViewHolder avh = new ActorViewHolder(v);
            return avh;
        }

        @Override
        public void onBindViewHolder(ActorViewHolder avh, int i) {
            final Actor item = actors.get(i);

            avh.itemName.setText(item.getNombre());

            avh.itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActorFragment actorFragment = ActorFragment.newInstance(item.getId());
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, actorFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            avh.itemContent.setText(getMovies(item));

            String imagen = item.getImagenes().get(0).getUrl();

            if (imagen != null && !imagen.isEmpty())
            {
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w154"+imagen).into(avh.itemImage);
            }
            else
            {
                Picasso.with(getContext()).load(R.drawable.batman).into(avh.itemImage);
            }

            // pvh.itemContent.setText(item.getBiografia());

            /*
            pvh.itemContainer.setOnClickListener(new View.OnClickListener() {@Override
                public void onClick(View v) {
                    BuscadorFragment.this.openActorDescription(item);
                }
            });
            */

        }

        public String getMovies (Actor actor)
        {
            String ret = "";
            List<Pelicula> pelis = actor.getPeliculas();

            ret = ret + pelis.get(0).getNombre();

            for(int i=1; i<pelis.size(); i++)
            {
                ret= ret+", "+pelis.get(i).getNombre();
            }

            return ret;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    private class MRVAdapter extends RecyclerView.Adapter<PeliculaViewHolder>{

        List<Pelicula> movies;

        public MRVAdapter(List<Pelicula> movies) {
            //Nuevas instancias para evitar modificar listas originales referenciadas
            this.movies = new ArrayList(movies);
        }

        public void update(List<Pelicula> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        @Override
        public PeliculaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.peli_item_layout, viewGroup, false);
            PeliculaViewHolder pvh = new PeliculaViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PeliculaViewHolder pvh, int i) {
            final Pelicula item = movies.get(i);

            pvh.itemName.setText(item.getNombre());

            pvh.itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PeliculaFragment peliculaFragment = PeliculaFragment.newInstance(item.getId());
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, peliculaFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            // pvh.itemContent.setText(item.getBiografia());

            /*
            pvh.itemContainer.setOnClickListener(new View.OnClickListener() {@Override
                public void onClick(View v) {
                    BuscadorFragment.this.openActorDescription(item);
                }
            });
            */

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

}
