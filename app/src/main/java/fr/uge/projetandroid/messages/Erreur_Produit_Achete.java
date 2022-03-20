package fr.uge.projetandroid.messages;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.shopping.AccueilAchat;
import fr.uge.projetandroid.shopping.Acheter;
import fr.uge.projetandroid.shopping.AfficherPanierAchat;
import fr.uge.projetandroid.shopping.AfficherProduitsRechercheAchat;
import fr.uge.projetandroid.shopping.AfficherSoldeAchat;
import fr.uge.projetandroid.shopping.AfficherWishlistAchat;

public class Erreur_Produit_Achete extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textView_nombre_panier_achat;
    private TextView textView_nombre_wishlist_achat;
    private TextView Textview_nom_prenom_utilisateur_achat;
    private TextView Textview_email_utilisateur_achat;
    private User user;
    private String devise;
    private double rate;
    private Boolean ChangeCurrency=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erreur_produit_achete);

        Intent myIntent = getIntent();
        devise = myIntent.getStringExtra("devise");
        rate = myIntent.getDoubleExtra("rate",1);
        user = (User)getIntent().getSerializableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



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
                R.array.devise, R.layout.spinner_item_menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setBackgroundResource(R.drawable.bg_spinner_menu);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                if(ChangeCurrency){
                    devise = charSequence.toString();
                    new Erreur_Produit_Achete.ChangeCurrencyTask().execute();
                }
                ChangeCurrency=true;

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
                    Intent myIntent = new Intent(Erreur_Produit_Achete.this, AfficherProduitsRechercheAchat.class);
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


            String url = "https://projetandroiduge.herokuapp.com/api/currency/"+devise;
            HttpHandler sh = new HttpHandler();
            String result = sh.makeServiceCall(url);
            rate = Double.parseDouble(result);
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
