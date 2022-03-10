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
import fr.uge.projetandroid.adapters.AdapterPanierEmprunt;
import fr.uge.projetandroid.entities.Borrow;
import fr.uge.projetandroid.entities.Product;

public class AfficherMesProduitsEmprunte extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView RecyclerView_ProduitEmprunte;
    private ProgressDialog pDialog;
    private String TAG = "AfficherMesProduitsEmprunte";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_produits_emprunte);


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
        RecyclerView_ProduitEmprunte = (RecyclerView)findViewById(R.id.RecyclerView_ProduitEmprunte);
    }

    private class ShowProductsTask extends AsyncTask<Void, Void, Void> {

        List<Product> produitsEmprunte;
        List<Borrow> borrows;


        public ShowProductsTask() {
            produitsEmprunte = new ArrayList<>();
            borrows = new ArrayList<>();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherMesProduitsEmprunte.this);
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
                    JSONArray arrayResult = json.getJSONArray("borrows");
                    for (int i = 0; i < arrayResult.length(); i++) {



                        Borrow borrow = new Borrow();
                        JSONObject jsonObj = arrayResult.getJSONObject(i);
                        borrow.setProduct(jsonObj.getInt("product"));
                        borrow.setEndAt(jsonObj.getString("endAt"));
                        borrow.setStartAt(jsonObj.getString("startAt"));
                        borrows.add(borrow);




                        String url3 = "http://uge-webservice.herokuapp.com/api/product/"+borrow.getProduct();
                        HttpHandler shh = new HttpHandler();
                        jsonStr = shh.makeServiceCall(url3);
                        Log.e(TAG, "Response from url: " + jsonStr);

                        if (jsonStr != null) {
                            try {
                                Product product = new Product();
                                JSONObject jsonObj2 = new JSONObject(jsonStr);
                                product.setId(jsonObj2.getInt("id"));
                                product.setName(jsonObj2.getString("name"));
                                product.setCategory(jsonObj2.getString("category"));
                                product.setType(jsonObj2.getString("type"));
                                product.setDescription((jsonObj2.getString("description")));
                                product.setPrice(jsonObj2.getDouble("price"));
                                product.setState(jsonObj2.getString("state"));
                                product.setAvailable(jsonObj2.getBoolean("available"));
                                product.setCreatedAt(jsonObj2.getString("createdAt"));
                                product.setPath(jsonObj2.getString("path"));
                                product.setRate(jsonObj2.getInt("avgRate"));
                                produitsEmprunte.add(product);

                            } catch (final JSONException e) {
                                Log.e(TAG, "Erreur" + e.getMessage());
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
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "mamadou" + e.getMessage(),
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

            Log.e("ListProduit",produitsEmprunte.toString());
            Log.e("borrows",borrows.toString());

            AdapterPanierEmprunt adapterPanierEmprunt = new AdapterPanierEmprunt(produitsEmprunte,borrows);

            RecyclerView_ProduitEmprunte.setLayoutManager(new LinearLayoutManager(AfficherMesProduitsEmprunte.this));

            RecyclerView_ProduitEmprunte.setAdapter(adapterPanierEmprunt);

        }

    }
}
