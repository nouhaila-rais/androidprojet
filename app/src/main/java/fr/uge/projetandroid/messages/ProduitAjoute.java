package fr.uge.projetandroid.messages;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.borrow.AccueilEmprunt;
import fr.uge.projetandroid.borrow.AfficherMesProduitsEmprunte;
import fr.uge.projetandroid.borrow.AfficherNotificationsEmprunt;
import fr.uge.projetandroid.borrow.AfficherProduitAjoute;
import fr.uge.projetandroid.borrow.AfficherProduitEmprunt;
import fr.uge.projetandroid.borrow.AfficherProduitsRechercheEmprunt;
import fr.uge.projetandroid.borrow.AjouterProduit;
import fr.uge.projetandroid.borrow.Emprunter;
import fr.uge.projetandroid.entities.User;

public class ProduitAjoute extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textView_nombre_notifications_emprunt;
    private TextView textView_nombre_panier_emprunt;
    private TextView Textview_nom_prenom_utilisateur_emprunt;
    private TextView Textview_email_utilisateur_emprunt;
    private User user;
    private long idProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit_ajoute);
        user = (User)getIntent().getSerializableExtra("user");
        idProduct = getIntent().getLongExtra("idProduct",1);

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
        Textview_nom_prenom_utilisateur_emprunt = (TextView)findViewById(R.id.Textview_nom_prenom_utilisateur_emprunt);
        Textview_email_utilisateur_emprunt = (TextView)findViewById(R.id.Textview_email_utilisateur_emprunt);
        Textview_nom_prenom_utilisateur_emprunt.setText(user.getFirstName()+" "+user.getLastName());
        Textview_email_utilisateur_emprunt.setText(user.getEmail());
        getMenuInflater().inflate(R.menu.main_emprunt, menu);


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
                    Intent myIntent = new Intent(ProduitAjoute.this, AfficherProduitsRechercheEmprunt.class);
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
