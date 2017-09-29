package com.utn.mobile.myapplication;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;

import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.service.ActorService;
import com.utn.mobile.myapplication.utils.GlobalConstants;
import com.utn.mobile.myapplication.utils.Keywords;

import java.util.ArrayList;
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



    private void createRecyclerView(List<Actor> actors) {
        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.actorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        RVAdapter adapter = new RVAdapter(actors);
        recyclerView.setAdapter(adapter);
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

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                //List<SearchCondition> searchConditions = getActivity().getSearchConditions();
                actors = ActorService.get().getAll("John");
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
                createRecyclerView(actors);
            }
        }

    }

    private class ActorViewHolder extends RecyclerView.ViewHolder {

        CardView itemContainer;
        TextView itemName;
        TextView itemContent;

        public ActorViewHolder(View itemView) {
            super(itemView);

            itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
            itemName = (TextView) itemView.findViewById(R.id.listItemName);
            itemContent = (TextView) itemView.findViewById(R.id.listItemContent);
        }
    }

    private class RVAdapter extends RecyclerView.Adapter<ActorViewHolder>{

        List<Actor> actors;

        public RVAdapter(List<Actor> actors) {
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
            ActorViewHolder pvh = new ActorViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(ActorViewHolder pvh, int i) {
            final Actor item = actors.get(i);

            pvh.itemName.setText(item.getNombre());
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
