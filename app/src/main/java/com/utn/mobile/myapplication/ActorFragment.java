package com.utn.mobile.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.ActorService;
import com.utn.mobile.myapplication.service.PeliculaService;
import com.utn.mobile.myapplication.service.SingleActorService;

import java.util.List;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mId;
    private String mParam2;



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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_actor, container, false);
        new FindActor().execute();
        return v;
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

                TextView nombre = (TextView) activity.findViewById(R.id.actor_name);
                nombre.setText(actor.getNombre());

                ScrollView scrollViewActor = (ScrollView) activity.findViewById(R.id.scrollActor);
                scrollViewActor.setVisibility(View.VISIBLE);
            }
        }

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
