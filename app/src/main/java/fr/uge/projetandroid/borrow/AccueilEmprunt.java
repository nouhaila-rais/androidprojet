package fr.uge.projetandroid.borrow;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.MainActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.adapters.AdapterSlideAccueilEmprunt;
import fr.uge.projetandroid.entities.User;

public class AccueilEmprunt extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewpager_home_emprunt;
    private LinearLayout LinearLayout_home_dots_emprunt;
    private AdapterSlideAccueilEmprunt adapterSlideAccueilEmprunt;

    private TextView[] mdots;
    private ImageButton ImageButton_home_next_emprunt;
    private ImageButton ImageButton_home_back_emprunt;

    private int mCureentPage;


    private TextView textView_nombre_notifications_emprunt;
    private TextView textView_nombre_panier_emprunt;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_emprunt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        user = (User)getIntent().getSerializableExtra("user");
        Log.e("UserAccueilEmprunt",user.toString());


        viewpager_home_emprunt =(ViewPager)findViewById(R.id.viewpager_home_emprunt);
        LinearLayout_home_dots_emprunt =(LinearLayout)findViewById(R.id.LinearLayout_home_dots_emprunt);

        ImageButton_home_next_emprunt =(ImageButton)findViewById(R.id.ImageButton_home_next_emprunt);
        ImageButton_home_back_emprunt =(ImageButton)findViewById(R.id.ImageButton_home_back_emprunt);

        adapterSlideAccueilEmprunt =new AdapterSlideAccueilEmprunt(this,user);
        viewpager_home_emprunt.setAdapter(adapterSlideAccueilEmprunt);
        adddots(0);

        viewpager_home_emprunt.addOnPageChangeListener(viewlistener);

        ImageButton_home_next_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager_home_emprunt.setCurrentItem(mCureentPage+1);

            }
        });

        ImageButton_home_back_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager_home_emprunt.setCurrentItem(mCureentPage-1);

            }
        });

        playSlider playSlider = new playSlider();
        playSlider.start();

    }




    private class playSlider extends Thread {


        @Override
        public synchronized void run() {

            while (true) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mCureentPage<6) mCureentPage++;
                            else mCureentPage=0;
                            viewpager_home_emprunt.setCurrentItem(mCureentPage);
                        }
                    });
                    sleep(3300);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }


    public void adddots(int i){

        mdots=new TextView[6];
        LinearLayout_home_dots_emprunt.removeAllViews();

        for (int x=0;x<mdots.length;x++){

            mdots[x]=new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(35);
            mdots[x].setTextColor(getResources().getColor(R.color.colorGris));

            LinearLayout_home_dots_emprunt.addView(mdots[x]);
        }
        if (mdots.length>0){

            mdots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            adddots(position);
            mCureentPage = position;
            adapterSlideAccueilEmprunt.setCurrent(mCureentPage);


            if (position==0){
                ImageButton_home_next_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setEnabled(false);


                ImageButton_home_next_emprunt.setVisibility(View.VISIBLE);
                ImageButton_home_back_emprunt.setVisibility(View.INVISIBLE);
            }
            else if(position==mdots.length-1){

                ImageButton_home_next_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setVisibility(View.VISIBLE);
                ImageButton_home_next_emprunt.setVisibility(View.INVISIBLE);


            }
            else {
                ImageButton_home_next_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setEnabled(true );
                ImageButton_home_back_emprunt.setVisibility(View.VISIBLE);
                ImageButton_home_next_emprunt.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageSelected(int position) {

            adddots(position);
            mCureentPage = position;
            adapterSlideAccueilEmprunt.setCurrent(mCureentPage);

            if (position==0){
                ImageButton_home_next_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setEnabled(false);


                ImageButton_home_next_emprunt.setVisibility(View.VISIBLE);
                ImageButton_home_back_emprunt.setVisibility(View.INVISIBLE);
            }
            else if(position==mdots.length-1){

                ImageButton_home_next_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setVisibility(View.VISIBLE);
                ImageButton_home_next_emprunt.setVisibility(View.INVISIBLE);


            }
            else {
                ImageButton_home_next_emprunt.setEnabled(true);
                ImageButton_home_back_emprunt.setEnabled(true );
                ImageButton_home_back_emprunt.setVisibility(View.VISIBLE);
                ImageButton_home_next_emprunt.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void setupBadge() {
        user = (User)getIntent().getSerializableExtra("user");

        if (textView_nombre_notifications_emprunt != null) {
            if (user.getTotalNotification() == 0) {
                if (textView_nombre_notifications_emprunt.getVisibility() != View.GONE) {
                    textView_nombre_notifications_emprunt.setVisibility(View.GONE);
                }
            } else {
                textView_nombre_notifications_emprunt.setText(String.valueOf(Math.min(user.getTotalNotification() , 99)));
                if (textView_nombre_notifications_emprunt.getVisibility() != View.VISIBLE) {
                    textView_nombre_notifications_emprunt.setVisibility(View.VISIBLE);
                }
            }
        }

        if (textView_nombre_panier_emprunt != null) {
            if (user.getTotalProduitEmprunte() == 0) {
                if (textView_nombre_panier_emprunt.getVisibility() != View.GONE) {
                    textView_nombre_panier_emprunt.setVisibility(View.GONE);
                }
            } else {
                textView_nombre_panier_emprunt.setText(String.valueOf(Math.min(user.getTotalProduitEmprunte() , 99)));
                if (textView_nombre_panier_emprunt.getVisibility() != View.VISIBLE) {
                    textView_nombre_panier_emprunt.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_emprunt, menu);
        user = (User)getIntent().getSerializableExtra("user");

        final MenuItem menuItemPanier = menu.findItem(R.id.item_nombre_panier_emprunt);
        View actionViewPanier = menuItemPanier.getActionView();
        textView_nombre_panier_emprunt = (TextView) actionViewPanier.findViewById(R.id.textView_nombre_panier_emprunt);

        final MenuItem menuItemNotification = menu.findItem(R.id.item_notifiction_emprunt);
        View actionViewNotification = menuItemNotification.getActionView();
        textView_nombre_notifications_emprunt = (TextView)actionViewNotification.findViewById(R.id.textView_nombre_notifications_emprunt);


        MenuItem mSearch = menu.findItem(R.id.item_search_emprunt);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query!=null){
                    Intent myIntent = new Intent(AccueilEmprunt.this, AfficherProduitsRechercheEmprunt.class);
                    myIntent.putExtra("user",user);
                    myIntent.putExtra("Keyword",query);
                    startActivity(myIntent);
                }

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        setupBadge();

        actionViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItemNotification);
            }
        });

        actionViewPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItemPanier);
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_notifiction_emprunt) {
            Intent myIntent = new Intent(this, AfficherNotificationsEmprunt.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);
            return true;
        }
        else if (id == R.id.item_nombre_panier_emprunt) {
            Intent myIntent = new Intent(this, AfficherMesProduitsEmprunte.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);
            return true;
        }
        else if (id == R.id.item_search_emprunt) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_emprunt_accueil) {

            Intent myIntent = new Intent(this, AccueilEmprunt.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_retourner) {
            Intent myIntent = new Intent(this, AfficherMesProduitsEmprunte.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_emprunter) {
            Intent myIntent = new Intent(this, Emprunter.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_mesproduits) {

            Intent myIntent = new Intent(this, AfficherProduitAjoute.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);
        }

        else if (id == R.id.nav__emprunt_ajouterproduit) {
            Intent myIntent = new Intent(this, AjouterProduit.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);
        }

        else if (id == R.id.nav__emprunt_deconnexion) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}