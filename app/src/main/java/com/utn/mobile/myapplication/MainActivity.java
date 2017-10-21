package com.utn.mobile.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import com.utn.mobile.myapplication.component.AsapTextView;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Genero;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.service.GenreService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private List<Actor> mActors = new ArrayList<>();
    private List<Pelicula> mMovies = new ArrayList<>();
    private String mQuery;
    private static List<Genero> mGeneros = new ArrayList<>();

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

    public void setFragment(Fragment fragment, boolean toBackStack, String name ) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        if(toBackStack) {
            ft.addToBackStack(name);
        }
        ft.commit();
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
    public void onNewIntent(Intent intent){
        setIntent(intent);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            View parentLayout = findViewById(android.R.id.content);
            setFragment(new BuscadorFragment());
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
            selectedFragment = new ReconocedorFragment();
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
