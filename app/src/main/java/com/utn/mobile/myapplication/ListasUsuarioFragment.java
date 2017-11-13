package com.utn.mobile.myapplication;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Lista;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.ActorService;
import com.utn.mobile.myapplication.service.ListService;
import com.utn.mobile.myapplication.service.PeliculaService;
import com.utn.mobile.myapplication.service.SingleActorService;

import java.util.ArrayList;
import java.util.List;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


public class ListasUsuarioFragment extends Fragment {

    MainActivity activity;
    private View mRootView;
    private String listName;
    private LRVAdapter adapterListas;

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
        new PopulateListTask().execute();
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
                        Pelicula peli = new Pelicula();
                        peli.setNombre(listName);
                        new AddList(peli).execute();
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

    private class LRVAdapter extends RecyclerView.Adapter<ListViewHolder>{

        List<Lista> listas;

        public LRVAdapter(List<Lista> listas) {
            //Nuevas instancias para evitar modificar listas originales referenciadas
            this.listas = new ArrayList(listas);
        }

        public void update(Lista lista) {
            this.listas.add(lista);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return listas.size();
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout, viewGroup, false);
            ListViewHolder lvh = new ListViewHolder(v);
            return lvh;
        }

        @Override
        public void onBindViewHolder(ListViewHolder lvh, int i) {
            final Lista item = listas.get(i);

            lvh.itemName.setText(item.getNombre());

            lvh.itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    ListaFragment listaFragment = ListaFragment.newInstance( gson.toJson(item.getPeliculas()));
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, listaFragment);
                    transaction.addToBackStack("pelicula_fragment_lista");
                    transaction.commit();
                }
            });

            lvh.itemContent.setText(getPeliculasFormat(item.getPeliculas()));


        }

        public String getPeliculasFormat (List<Pelicula> pelis)
        {
            String ret = "";
            //List<Pelicula> pelis = lista.getPeliculas();



            if(pelis.size() != 0) {
                ret = ret + pelis.get(0).getNombre();

                for (int i = 1; i < pelis.size(); i++) {
                    ret = ret + ", " + pelis.get(i).getNombre();
                }
            }
            else{
                ret = "No posee películas";
            }

            return ret;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    private void createRecyclerView(List<Lista> listas) {
        RecyclerView recyclerViewListas = (RecyclerView) mRootView.findViewById(R.id.userListRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewListas.setLayoutManager(layoutManager);

        adapterListas = new LRVAdapter(listas);
        recyclerViewListas.setAdapter(adapterListas);


    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        CardView itemContainer;
        TextView itemName;
        TextView itemContent;

        public ListViewHolder(View itemView) {
            super(itemView);

            itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
            itemName = (TextView) itemView.findViewById(R.id.listItemName);
            itemContent = (TextView) itemView.findViewById(R.id.listItemContent);
        }

    }

    private class PopulateListTask extends AsyncTask<Object, Object, Integer> {
        List<Lista> listas;

        @Override
        protected void onPreExecute() {
            MainActivity activity = (MainActivity) getActivity();
            activity.showLoading();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                MainActivity activity = (MainActivity) getActivity();
                listas = ListService.get().getAll();
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
                activity.hideLoading();
                activity.setListas(listas);
                createRecyclerView(listas);
            }
        }

    }

    private class AddList extends AsyncTask<Object, Object, Integer> {
        Pelicula pelicula;

        public AddList(Pelicula peli)
        {
            pelicula = peli;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                String response = ListService.get().addOne(pelicula);
                if(response!=null)
                {
                    MainActivity activity = (MainActivity) getActivity();
                    return TASK_RESULT_OK;
                }
                else
                {
                    return TASK_RESULT_ERROR;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return TASK_RESULT_ERROR;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == TASK_RESULT_OK) {
                MainActivity activity = (MainActivity) getActivity();
                Lista lista = new Lista(pelicula.getNombre());
                activity.addLista(lista);
                adapterListas.update(lista);
                if (activity == null) return;
            }
            else
            {
                MainActivity activity = (MainActivity) getActivity();
                Toast.makeText(getContext(),"Ocurrió un problema al crear la lista", Toast.LENGTH_LONG).show();
                //activity.removeActorDeMFavs(actor);
            }
        }

    }



}
