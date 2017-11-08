package com.utn.mobile.myapplication;

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

import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ActoresFavoritosFragment extends Fragment {

    private View mRootView;

    public ActoresFavoritosFragment() {
    }

    public static ActoresFavoritosFragment newInstance(String param1, String param2) {
        ActoresFavoritosFragment fragment = new ActoresFavoritosFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_actores_favoritos, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        List<Actor> actoresFav = mainActivity.getmActoresFav();
        createRecyclerView(actoresFav);
        return mRootView;
    }

    private class ActorViewHolder extends RecyclerView.ViewHolder {

        TextView actorName1;
        CardView actorContainer1;
        ImageView actorImage1;
        TextView actorName2;
        CardView actorContainer2;
        ImageView actorImage2;

        public ActorViewHolder(View itemView) {
            super(itemView);

            actorName1 = (TextView) itemView.findViewById(R.id.actor_fav_name1);
            actorContainer1 = (CardView) itemView.findViewById(R.id.actorFavContainer);
            actorImage1 = (ImageView) itemView.findViewById(R.id.actor_fav_image1);
            actorName2 = (TextView) itemView.findViewById(R.id.actor_fav_name2);
            actorContainer2 = (CardView) itemView.findViewById(R.id.actorFavContainer2);
            actorImage2 = (ImageView) itemView.findViewById(R.id.actor_fav_image2);
        }
    }

    private class ARVAdapter extends RecyclerView.Adapter<ActorViewHolder> {

        List<Actor> actors;

        public ARVAdapter(Collection<Actor> actors) {
            //Nuevas instancias para evitar modificar listas originales referenciadas
            this.actors = new ArrayList(actors);
        }

        public void update(Collection<Actor> actors) {
            this.actors.clear();
            this.actors.addAll(actors);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if ((actors.size() % 2) == 0)
            {
                return actors.size() / 2;
            }
            else
            {
                return actors.size() / 2 + 1;
            }
        }

        @Override
        public ActorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor_fav_item_layout, viewGroup, false);
            ActorViewHolder avh = new ActorViewHolder(v);
            return avh;
        }

        @Override
        public void onBindViewHolder(ActorViewHolder avh, int i) {

            final Actor item1 = actors.get(i * 2);

            avh.actorName1.setText(item1.getNombre());

            String imgPath1 = item1.getImagenes().get(0).getUrl();
            if (imgPath1.equals("null"))
            {
                Picasso.with(getContext()).load(R.drawable.batman_300).into(avh.actorImage1);
            }
            else
            {
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+imgPath1).into(avh.actorImage1);
            }
            avh.actorContainer1.setOnClickListener(new View.OnClickListener() {
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

            if (i * 2 + 1 < actors.size()) {
                final Actor item2 = actors.get(i * 2 + 1);

                avh.actorContainer2.setVisibility(View.VISIBLE);

                avh.actorName2.setText(item2.getNombre());

                String imgPath2 = item2.getImagenes().get(0).getUrl();
                if (imgPath2.equals("null"))
                {
                    Picasso.with(getContext()).load(R.drawable.batman_300).into(avh.actorImage2);
                }
                else
                {
                    Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+imgPath2).into(avh.actorImage2);
                }

                avh.actorContainer2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = item2.getId();
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

        private void createRecyclerView(Collection<Actor> actors) {
            RecyclerView recyclerViewActoresFav = (RecyclerView) mRootView.findViewById(R.id.actoresFavRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewActoresFav.setLayoutManager(layoutManager);

            ARVAdapter adapterPeliculas = new ARVAdapter(actors);
            recyclerViewActoresFav.setAdapter(adapterPeliculas);

        }

    }
