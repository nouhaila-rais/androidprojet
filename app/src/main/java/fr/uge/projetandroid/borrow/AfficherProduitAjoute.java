package fr.uge.projetandroid.borrow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.uge.projetandroid.HttpHandler;
import fr.uge.projetandroid.MainActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.adapters.AdapterProduitAjouteEmprunt;
import fr.uge.projetandroid.entities.Product;

public class AfficherProduitAjoute extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView RecyclerView_ProduitAjoute;
    private ProgressDialog pDialog;
    private String TAG = AfficherProduitAjoute.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_produit_ajoute);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initUi();
        new ShowProductsTask().execute();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_emprunt_accueil) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_retourner) {
            Intent myIntent = new Intent(this, AfficherNotificationsEmprunt.class);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_emprunter) {
            Intent myIntent = new Intent(this, AfficherProduitEmprunt.class);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_mesproduits) {
            Intent myIntent = new Intent(this, AfficherProduitAjoute.class);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_deconnexion) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUi(){
        RecyclerView_ProduitAjoute = (RecyclerView)findViewById(R.id.RecyclerView_ProduitAjoute);
    }

    private class ShowProductsTask extends AsyncTask<Void, Void, Void> {

        List<Product> produitsAjoute;



        public ShowProductsTask() {
            produitsAjoute = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherProduitAjoute.this);
            pDialog.setMessage("Chargement des produits...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "http://uge-webservice.herokuapp.com/api/user/12";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);



            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject json = new JSONObject(jsonStr);
                    JSONArray arrayResult = json.getJSONArray("products");
                    for (int i = 0; i < arrayResult.length(); i++) {
                        Product product = new Product();
                        JSONObject jsonObj = arrayResult.getJSONObject(i);
                        product.setId(jsonObj.getInt("id"));
                        product.setName(jsonObj.getString("name"));
                        product.setCategory(jsonObj.getString("category"));
                        product.setType(jsonObj.getString("type"));
                        product.setDescription((jsonObj.getString("description")));
                        product.setPrice(jsonObj.getDouble("price"));
                        product.setState(jsonObj.getString("state"));
                        product.setAvailable(jsonObj.getBoolean("available"));
                        product.setCreatedAt(jsonObj.getString("createdAt"));
                        product.setPath(jsonObj.getString("path"));
                        product.setRate(jsonObj.getInt("avgRate"));
                        produitsAjoute.add(product);
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Erreur" + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            AdapterProduitAjouteEmprunt adapterProduitAjouteEmprunt = new AdapterProduitAjouteEmprunt(produitsAjoute);

            RecyclerView_ProduitAjoute.setLayoutManager(new LinearLayoutManager(AfficherProduitAjoute.this));

            RecyclerView_ProduitAjoute.setAdapter(adapterProduitAjouteEmprunt);

        }

    }
}
