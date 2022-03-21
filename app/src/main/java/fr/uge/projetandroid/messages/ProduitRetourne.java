package fr.uge.projetandroid.messages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.MainActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.borrow.AccueilEmprunt;
import fr.uge.projetandroid.borrow.AfficherMesProduitsEmprunte;
import fr.uge.projetandroid.borrow.AfficherNotificationsEmprunt;
import fr.uge.projetandroid.borrow.AfficherProduitAjoute;
import fr.uge.projetandroid.borrow.AfficherProduitEmprunt;
import fr.uge.projetandroid.borrow.AfficherProduitsRechercheEmprunt;
import fr.uge.projetandroid.borrow.AjouterProduit;
import fr.uge.projetandroid.borrow.Emprunter;
import fr.uge.projetandroid.entities.RequestBorrow;
import fr.uge.projetandroid.entities.ReturnProduct;
import fr.uge.projetandroid.entities.User;

public class ProduitRetourne extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private long idProduct;
    private String etat;

    private TextView textView_nombre_notifications_emprunt;
    private TextView textView_nombre_panier_emprunt;
    private TextView Textview_nom_prenom_utilisateur_emprunt;
    private TextView Textview_email_utilisateur_emprunt;
    private User user;

    private ProgressDialog pDialog;
    private String TAG = ProduitRetourne.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit_retourne);

        Intent myIntent = getIntent();
        etat = myIntent.getStringExtra("etat");
        idProduct = myIntent.getLongExtra("idProduct",0);
        Log.e("idPorduitretoune",idProduct+"");
        Log.e("etatproduitretourne",etat);
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

        new ProduitRetourne.ReturnProductTask().execute();

    }

    private void setupBadge() {
        int total =user.getTotalProduitEmprunte()-1;
        if(total<=0) total=0;
        user.setTotalProduitEmprunte(total);

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
                    Intent myIntent = new Intent(ProduitRetourne.this, AfficherProduitsRechercheEmprunt.class);
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



    private class ReturnProductTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProduitRetourne.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpURLConnection urlConnection;
            String url2 = "http://projetandroiduge.herokuapp.com/api/return/";

            ReturnProduct returnProduct = new ReturnProduct(idProduct,etat);

            String data = returnProduct.toJson();
            Log.e("Json", data);
            String result = null;
            try {
                //Connect
                urlConnection = (HttpURLConnection) ((new URL(url2).openConnection()));
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(data);
                writer.close();
                outputStream.close();

                //Read
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                result = sb.toString();
                Log.e("Json", data);
                Log.e("Message retour", "resultat : " + result);


            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e("Erreur", e.getMessage());
            }
            return null;
        }




        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


        }

    }
}
