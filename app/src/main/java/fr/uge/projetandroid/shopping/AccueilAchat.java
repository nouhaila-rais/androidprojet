package fr.uge.projetandroid.shopping;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.adapters.Adapter_SlideAccueil_Achat;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.handlers.HttpHandler;

public class AccueilAchat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewpager_home_achat;
    private LinearLayout LinearLayout_home_dots_achat;
    private Adapter_SlideAccueil_Achat adapterSlideAccueilAchat;

    private TextView[] mdots;
    private ImageButton ImageButton_home_next_achat;
    private ImageButton ImageButton_home_back_achat;

    private int mCureentPage;


    private TextView textView_nombre_panier_achat;
    private TextView textView_nombre_wishlist_achat;
    private TextView Textview_nom_prenom_utilisateur_achat;
    private TextView Textview_email_utilisateur_achat;
    private User user;
    private String devise;
    private double rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_achat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        devise = getIntent().getStringExtra("devise");
        rate = getIntent().getDoubleExtra("rate",1);
        user = (User)getIntent().getSerializableExtra("user");
        Log.e("UserAccueilAchat",user.toString());


        viewpager_home_achat =(ViewPager)findViewById(R.id.viewpager_home_achat);
        LinearLayout_home_dots_achat =(LinearLayout)findViewById(R.id.LinearLayout_home_dots_achat);

        ImageButton_home_next_achat =(ImageButton)findViewById(R.id.ImageButton_home_next_achat);
        ImageButton_home_back_achat =(ImageButton)findViewById(R.id.ImageButton_home_back_achat);

        adapterSlideAccueilAchat =new Adapter_SlideAccueil_Achat(this,user,devise,rate);
        viewpager_home_achat.setAdapter(adapterSlideAccueilAchat);
        adddots(0);

        viewpager_home_achat.addOnPageChangeListener(viewlistener);

        ImageButton_home_next_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager_home_achat.setCurrentItem(mCureentPage+1);

            }
        });

        ImageButton_home_back_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager_home_achat.setCurrentItem(mCureentPage-1);

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
                            viewpager_home_achat.setCurrentItem(mCureentPage);
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
        LinearLayout_home_dots_achat.removeAllViews();

        for (int x=0;x<mdots.length;x++){

            mdots[x]=new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(35);
            mdots[x].setTextColor(getResources().getColor(R.color.colorGris));

            LinearLayout_home_dots_achat.addView(mdots[x]);
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
            adapterSlideAccueilAchat.setCurrent(mCureentPage);


            if (position==0){
                ImageButton_home_next_achat.setEnabled(true);
                ImageButton_home_back_achat.setEnabled(false);


                ImageButton_home_next_achat.setVisibility(View.VISIBLE);
                ImageButton_home_back_achat.setVisibility(View.INVISIBLE);
            }
            else if(position==mdots.length-1){

                ImageButton_home_next_achat.setEnabled(true);
                ImageButton_home_back_achat.setEnabled(true);
                ImageButton_home_back_achat.setVisibility(View.VISIBLE);
                ImageButton_home_next_achat.setVisibility(View.INVISIBLE);


            }
            else {
                ImageButton_home_next_achat.setEnabled(true);
                ImageButton_home_back_achat.setEnabled(true );
                ImageButton_home_back_achat.setVisibility(View.VISIBLE);
                ImageButton_home_next_achat.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageSelected(int position) {

            adddots(position);
            mCureentPage = position;
            adapterSlideAccueilAchat.setCurrent(mCureentPage);

            if (position==0){
                ImageButton_home_next_achat.setEnabled(true);
                ImageButton_home_back_achat.setEnabled(false);


                ImageButton_home_next_achat.setVisibility(View.VISIBLE);
                ImageButton_home_back_achat.setVisibility(View.INVISIBLE);
            }
            else if(position==mdots.length-1){

                ImageButton_home_next_achat.setEnabled(true);
                ImageButton_home_back_achat.setEnabled(true);
                ImageButton_home_back_achat.setVisibility(View.VISIBLE);
                ImageButton_home_next_achat.setVisibility(View.INVISIBLE);


            }
            else {
                ImageButton_home_next_achat.setEnabled(true);
                ImageButton_home_back_achat.setEnabled(true );
                ImageButton_home_back_achat.setVisibility(View.VISIBLE);
                ImageButton_home_next_achat.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void setupBadge() {

        if (textView_nombre_panier_achat != null) {
            if (user.getTotalPanier() == 0) {
                if (textView_nombre_panier_achat.getVisibility() != View.GONE) {
                    textView_nombre_panier_achat.setVisibility(View.GONE);
                }
            } else {
                textView_nombre_panier_achat.setText(String.valueOf(Math.min(user.getTotalPanier() , 99)));
                if (textView_nombre_panier_achat.getVisibility() != View.VISIBLE) {
                    textView_nombre_panier_achat.setVisibility(View.VISIBLE);
                }
            }
        }

        if (textView_nombre_wishlist_achat != null) {
            if (user.getTotalWishlist() == 0) {
                if (textView_nombre_wishlist_achat.getVisibility() != View.GONE) {
                    textView_nombre_wishlist_achat.setVisibility(View.GONE);
                }
            } else {
                textView_nombre_wishlist_achat.setText(String.valueOf(Math.min(user.getTotalWishlist() , 99)));
                if (textView_nombre_wishlist_achat.getVisibility() != View.VISIBLE) {
                    textView_nombre_wishlist_achat.setVisibility(View.VISIBLE);
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
        Textview_nom_prenom_utilisateur_achat = (TextView)findViewById(R.id.Textview_nom_prenom_utilisateur_achat);
        Textview_email_utilisateur_achat = (TextView)findViewById(R.id.Textview_email_utilisateur_achat);
        Textview_nom_prenom_utilisateur_achat.setText(user.getFirstName()+" "+user.getLastName());
        Textview_email_utilisateur_achat.setText(user.getEmail());
        getMenuInflater().inflate(R.menu.main_2, menu);


        final MenuItem menuItemNombreWishlist = menu.findItem(R.id.item_wishlist_achat);
        View actionViewWishlist = menuItemNombreWishlist.getActionView();
        textView_nombre_wishlist_achat = (TextView) actionViewWishlist.findViewById(R.id.textView_nombre_wishlist_achat);

        final MenuItem menuItemNombrePanier = menu.findItem(R.id.item_nombre_panier_achat);
        View actionViewPanier = menuItemNombrePanier.getActionView();
        textView_nombre_panier_achat = (TextView)actionViewPanier.findViewById(R.id.textView_nombre_panier_achat);


        final MenuItem menuItemDevise = menu.findItem(R.id.item_devise_achat);
        Spinner spinner = (Spinner) menuItemDevise.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.devise, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                Log.e("Devise",charSequence.toString());
                devise = charSequence.toString();
                new AccueilAchat.ChangeCurrencyTask().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        MenuItem mSearch = menu.findItem(R.id.item_search_achat);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query!=null){
                    Intent myIntent = new Intent(AccueilAchat.this, AfficherProduitsRechercheAchat.class);
                    myIntent.putExtra("user",user);
                    myIntent.putExtra("Keyword",query);
                    myIntent.putExtra("devise",devise);
                    myIntent.putExtra("rate",rate);
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

        actionViewPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItemNombrePanier);
            }
        });

        actionViewWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItemNombreWishlist);
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_nombre_panier_achat) {
            Intent myIntent = new Intent(this, AfficherPanierAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);
            return true;
        }
        else if (id == R.id.item_wishlist_achat) {
            Intent myIntent = new Intent(this, AfficherWishlistAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);
            return true;
        }
        else if (id == R.id.item_search_achat) {
            return true;
        }

        else if (id == R.id.item_devise_achat) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_achat_accueil) {

            Intent myIntent = new Intent(this, AccueilAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);

        } else if (id == R.id.nav_achat_acheter) {
            Intent myIntent = new Intent(this, Acheter.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);

        } else if (id == R.id.nav_achat_panier) {
            Intent myIntent = new Intent(this, AfficherPanierAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);

        } else if (id == R.id.nav_achat_whislist) {

            Intent myIntent = new Intent(this, AfficherWishlistAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);
        }

        else if (id == R.id.nav_achat_solde) {
            Intent myIntent = new Intent(this, AfficherSoldeAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);
        }

        else if (id == R.id.nav_achat_deconnexion) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ChangeCurrencyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://uge-webservice.herokuapp.com/api/currency/"+devise;
            HttpHandler sh = new HttpHandler();
            String result = sh.makeServiceCall(url);
            rate = Double.parseDouble(result);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}