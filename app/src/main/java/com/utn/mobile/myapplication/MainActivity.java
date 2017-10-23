package com.utn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.utn.mobile.myapplication.component.AsapTextView;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Genero;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.GenreService;
import com.utn.mobile.myapplication.utils.SpinnerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_ERROR;
import static com.utn.mobile.myapplication.utils.GlobalConstants.TASK_RESULT_OK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private List<Actor> mActors = new ArrayList<>();
    private List<Pelicula> mMovies = new ArrayList<>();
    private String mQuery;
    private static List<Genero> mGeneros = new ArrayList<>();
    private int PROFILE_PIC_COUNT;
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 1;
    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        new FindGenres().execute();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        hideButtonsIfNecessary(menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFragment(Fragment fragment){
        setFragment(fragment, false, null);
    }

    public void showLoading(){
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Cargando..");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    public void hideLoading(){
        nDialog.dismiss();
    }

    public void setFragment(Fragment fragment, boolean toBackStack, String name ) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        if(toBackStack) {
            ft.addToBackStack(name);
        }
        ft.commitAllowingStateLoss();
    }

    public void showMenu(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private Fragment getMainFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    /*
    public ImageView setToolbarButton(int id){
        ImageView toolbarButton = (ImageView) findViewById(R.id.changeModeButton);
        toolbarButton.setImageResource(id);
        return toolbarButton;
    }*/

    public void setToolbarTitle(int id) {
        AsapTextView toolbarTxt = (AsapTextView) findViewById(R.id.toolbar_text_view);
        toolbarTxt.setText(id);
    }

    /*
    public void setToolbarButtonDefault(){
        ImageView toolbarButton = (ImageView) findViewById(R.id.changeModeButton);
        setToolbarTitle(R.string.app_name);
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            View parentLayout = findViewById(android.R.id.content);
            setFragment(new BuscadorFragment());
        }
    }

    public void hideButtonsIfNecessary(Menu menu){
        String token = PreferenceManager.getDefaultSharedPreferences(MovieGuruApplication.getAppContext()).getString("user-token", null);
        if(token == null){
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;
        boolean selected = true;

        if (id == R.id.nav_recomendations) {
            selectedFragment = new RecomendacionesFragment();
        } else if (id == R.id.nav_searcher) {
            selectedFragment = new BuscadorFragment();
        } else if (id == R.id.nav_user_lists) {
            selectedFragment = new ListasUsuarioFragment();
        } else if (id == R.id.nav_user_fav_actors) {
            selectedFragment = new ActoresFavoritosFragment();
        } else if (id == R.id.nav_movie_recognizer) {
            showCameraDialog();
            selected = false;
        } else if (id == R.id.nav_login) {
            selectedFragment = new LoginFragment();
        } else if (id == R.id.nav_close_session) {
            //closeSession()
            selected = false;
        }
        else {
            selected = false;
        }

        if(selected) {
            setFragment(selectedFragment);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showCameraDialog(){
        final CharSequence[] items = {"Tomar una foto", "Elegir de la galería", "Cerrar"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Añade una foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Tomar una foto")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Elegir de la galería")) {
                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cerrar")) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Integer getRandomPath(){
        Integer[] arrayPaths = {11, 12, 13, 14, 15, 30, 13475, 223};
        int idx = new Random().nextInt(arrayPaths.length);
        Integer path = (arrayPaths[idx]);
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                //get actual photo
                Integer url_imagen = getRandomPath();
                Bundle arguments = new Bundle();
                arguments.putInt("url", url_imagen);
                ReconocedorFragment reconocedorFragment = new ReconocedorFragment();
                reconocedorFragment.setArguments(arguments);
                setFragment(reconocedorFragment);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
        else if (requestCode == SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                Integer url_imagen = getRandomPath();
                Bundle arguments = new Bundle();
                arguments.putInt("url", url_imagen);
                ReconocedorFragment reconocedorFragment = new ReconocedorFragment();
                reconocedorFragment.setArguments(arguments);
                setFragment(reconocedorFragment);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private class FindGenres extends AsyncTask<Object, Object, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                //List<SearchCondition> searchConditions = getActivity().getSearchConditions();
                mGeneros = GenreService.get().getAll();
                return TASK_RESULT_OK;
            } catch (Exception ex) {
                ex.printStackTrace();
                return TASK_RESULT_ERROR;
            }
        }
    }


    public static String getGenreById(int id)
    {
        for(int i=0; i<mGeneros.size();i++)
        {
            Genero genre = mGeneros.get(i);
            if (genre.getId() == id)
            {
                return genre.getNombre();
            }
        }
        return "";
    }

    public void setActors(List<Actor> actors) { this.mActors = actors; }
    public void setMovies(List<Pelicula> movies) { this.mMovies = movies; }
    public void setGenres(List<Genero> genres) { this.mGeneros = genres; }
    public void setQuery(String query) { this.mQuery = query; }
    public String getQuery() { return mQuery; }
}
