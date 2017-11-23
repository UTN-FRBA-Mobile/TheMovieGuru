package com.utn.mobile.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.domain.ActorEnPelicula;
import com.utn.mobile.myapplication.domain.Lista;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.ImageService;
import com.utn.mobile.myapplication.service.ListService;
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
    private List<Lista> mListas = new ArrayList<>();




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
        new CargarListasDelUsuario().execute();
        return mRootView;
    }

    private class FindMovie extends AsyncTask<Object, Object, Integer> {
        Pelicula peli;

        @Override
        protected void onPreExecute() {
            MainActivity activity = (MainActivity) getActivity();
            activity.showLoading();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                peli = SingleMovieService.get().getOne(mId);
                MainActivity activity = (MainActivity) getActivity();
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
                activity.setPeliculaActual(peli);
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
        if(peli.getImg_poster().equals("null"))
        {
            Picasso.with(this.getContext()).load(R.drawable.batman_300).into(imagenIV);
        }
        else
        {
            Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w500" + peli.getImg_poster()).into(imagenIV);
        }

        ImageView backIV = (ImageView) activity.findViewById(R.id.back_image);
        Picasso.with(this.getContext()).load("https://image.tmdb.org/t/p/w1280"+peli.getImg_backdrop()).into(backIV);

        ScrollView scrollViewPeli = (ScrollView) activity.findViewById(R.id.scrollMovie);
        scrollViewPeli.setVisibility(View.VISIBLE);

        final int user_id =  PreferenceManager.getDefaultSharedPreferences(MovieGuruApplication.getAppContext()).getInt("user-id", -1);

        if(user_id>=0) {
            Button btnLista = (Button) activity.findViewById(R.id.lista_button);
            btnLista.setVisibility(View.VISIBLE);
            btnLista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    DialogoSeleccion dialogo = new DialogoSeleccion();
                    Bundle args = new Bundle();
                    args.putString("mId",String.valueOf(peli.getId()));
                    args.putString("nombre_pelicula", peli.getNombre());
                    dialogo.setArguments(args);
                    dialogo.show(fragmentManager, "tagSeleccion");
                }
            });
        }

    }

    public static class DialogoSeleccion extends DialogFragment {


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            MainActivity mainActivity = (MainActivity) getActivity();

            Bundle args = getArguments();
            final String mId = args.getString("mId");
            final String nombrePelicula = args.getString("nombre_pelicula");
            final List<Lista> listas = mainActivity.getmListas();
/*
            final String[] items = {"Español", "Inglés", "Francés"};
            final boolean[] valores     = {true, false, true};
*/
            final String[] nombres = new ListasAdapter(mainActivity,listas).getListNombreListas();
            final boolean[] valores = new ListasAdapter(mainActivity,listas).getBoolsPerteneceALista(Integer.parseInt(mId));


            String url = String.format(getContext().getString(R.string.url_listas_quitar_pelicula),
                    getContext().getString(R.string.base_url),
                    getContext().getString(R.string.url_listas_quitar_pelicula));

            //new ListasAdapter(mainActivity,mainActivity.getmListas());


            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setTitle("Agregar a Lista...")
                    .setMultiChoiceItems(nombres, valores,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                    //Log.i("Dialogos", "Opción elegida: " + items[item]);
                                    MainActivity mainActivity = (MainActivity) getActivity();

                                    if (isChecked) {

                                        new AgregarPeliculaALista(listas.get(position), mainActivity.getPeliculaActual()).execute();

                                        mainActivity.getmListas().get(position).addPelicula(mainActivity.getPeliculaActual());
                                        Toast.makeText(getContext(),"Agregado: " + nombrePelicula + " a "+ listas.get(position).getNombre(),Toast.LENGTH_LONG).show();

                                    } else {

                                        new QuitarPeliculaDeLaLista(listas.get(position), mainActivity.getPeliculaActual()).execute();

                                        mainActivity.getmListas().get(position).removePelicula(mainActivity.getPeliculaActual());
                                        Toast.makeText(getContext(),"Quitado: " + nombrePelicula + " de "+ listas.get(position).getNombre(),Toast.LENGTH_LONG).show();
                                    }


                                }
                            });

            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
/*
                            ListView list = ((android.support.v7.app.AlertDialog) dialog).getListView();
                            //ListView has boolean array like {1=true, 3=true}, that shows checked items
                            //list.get
                            Log.d("aca",list.toString());*/
                        }
                    });
/*
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //((TextView) myFilesActivity.findViewById(R.id.text)).setText("Click here to open Dialog");
                        }
                    });
*/
            return builder.create();
        }
    }

    private class ActorViewHolder extends RecyclerView.ViewHolder {

        TextView actorName;
        TextView actorCharacter;
        CardView actorContainer;
        ImageView actorImage;

        public ActorViewHolder(View itemView) {
            super(itemView);

            actorName = (TextView) itemView.findViewById(R.id.movie_actor_name);
            actorCharacter = (TextView) itemView.findViewById(R.id.movie_actor_character);
            actorContainer = (CardView) itemView.findViewById(R.id.actorContainer);
            actorImage = (ImageView) itemView.findViewById(R.id.movie_actor_image);
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
            return actors.size();
        }

        @Override
        public ActorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.peli_actor_layout, viewGroup, false);
            ActorViewHolder avh = new ActorViewHolder(v);
            return avh;
        }

        @Override
        public void onBindViewHolder(ActorViewHolder avh, int i) {

                final ActorEnPelicula item = actors.get(i);

                avh.actorName.setText(item.getNombre());
                avh.actorCharacter.setText(item.getCharacter());

                new FindImage(item.getId(),avh).execute();

                avh.actorContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = item.getId();
                        ActorFragment peliculaFragment = ActorFragment.newInstance(id);
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


        private class FindImage extends AsyncTask<Object, Object, Integer> {
            String img_url;
            ActorViewHolder avh;
            int id;

            public FindImage(int ID, ActorViewHolder AVH)
            {
                id = ID;
                avh = AVH;
            }

            @Override
            protected Integer doInBackground(Object... params) {
                try {
                    img_url = ImageService.get().getOne(id);
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
                    if(img_url.equals(""))
                    {
                        Picasso.with(getContext()).load(R.drawable.batman_300).into(avh.actorImage);
                    }
                    else
                    {
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w342" + img_url).into(avh.actorImage);
                    }
                }
            }

        }
    }

    private void createRecyclerView(Collection<ActorEnPelicula> actors) {
        RecyclerView recyclerViewPeliculas = (RecyclerView) mRootView.findViewById(R.id.actorsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPeliculas.setLayoutManager(layoutManager);

        ARVAdapter adapterPeliculas = new ARVAdapter(actors);
        recyclerViewPeliculas.setAdapter(adapterPeliculas);

    }

    private class CargarListasDelUsuario extends AsyncTask<Object, Object, Integer> {

        List<Lista> listas;

        @Override
        protected Integer doInBackground(Object... params) {
            try {

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
                activity.setListas(listas);

            }
        }

    }

    private static class AgregarPeliculaALista extends AsyncTask<Object, Object, Integer> {

        Lista lista;
        String respuesta;
        Pelicula pelicula;

        public AgregarPeliculaALista(Lista lista, Pelicula pelicula){

            this.lista = lista;
            this.pelicula = pelicula;

        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {

                //respuesta = ListService.get().addMovie(lista,pelicula);
                return TASK_RESULT_OK;
            } catch (Exception ex) {
                ex.printStackTrace();
                return TASK_RESULT_ERROR;
            }
        }


    }

    private static class QuitarPeliculaDeLaLista extends AsyncTask<Object, Object, Integer> {

        Lista lista;
        String respuesta;
        Pelicula pelicula;

        public QuitarPeliculaDeLaLista(Lista lista, Pelicula pelicula){

            this.lista = lista;
            this.pelicula = pelicula;

        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {

                // respuesta = ListService.get().addOne(new Pelicula(null,mId));
                return TASK_RESULT_OK;
            } catch (Exception ex) {
                ex.printStackTrace();
                return TASK_RESULT_ERROR;
            }
        }


    }
}
