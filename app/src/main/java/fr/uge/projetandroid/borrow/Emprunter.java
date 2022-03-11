package fr.uge.projetandroid.borrow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.uge.projetandroid.MainActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.adapters.AdapterProduitsRechercheEmprunt;
import fr.uge.projetandroid.entities.Product;
import fr.uge.projetandroid.handlers.HttpHandler;


public class Emprunter extends AppCompatActivity implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView RecyclerView_layout_emprunter;
    private Spinner spinner_categorie_emprunter;
    private Spinner spinner_type_emprunter;
    private String keyword="mac";
    private String url;
    private ProgressDialog pDialog;
    private String TAG = Emprunter.class.getSimpleName();
    private Boolean search=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprunter);

        RecyclerView_layout_emprunter = (RecyclerView)findViewById(R.id.RecyclerView_layout_emprunter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        spinner_categorie_emprunter = (Spinner) findViewById(R.id.spinner_categorie_produits_emprunter);
        spinner_type_emprunter  = (Spinner) findViewById(R.id.spinner_type_produits_emprunter);

        ArrayAdapter<CharSequence> adapterCategorie = ArrayAdapter.createFromResource(this,
                R.array.categorie_array, R.layout.spinner_item);
        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categorie_emprunter.setAdapter(adapterCategorie);
        spinner_categorie_emprunter.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type_bibliotheque_array, R.layout.spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type_emprunter.setAdapter(adapterType);
        spinner_type_emprunter.setOnItemSelectedListener(this);

        spinner_type_emprunter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                Log.e("type",charSequence.toString());
                keyword=charSequence.toString();
                showProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_categorie_emprunter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                String categrorie = charSequence.toString();

                //keyword=charSequence.toString().replaceAll(" ","%");
                //new Emprunter.ShowProductsTask().execute();

                System.out.println("Item : " + charSequence.toString());
                Log.e("categorie",charSequence.toString());


                if(categrorie.equals("Bibliotheque")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Emprunter.this,
                            R.array.type_bibliotheque_array, R.layout.spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_type_emprunter.setAdapter(adapterType);

                }
                else if(categrorie.equals("Electronique")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Emprunter.this,
                            R.array.type_electronique_array, R.layout.spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_type_emprunter.setAdapter(adapterType);


                }
                else if(categrorie.equals("Mode et vetements")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Emprunter.this,
                            R.array.type_modevetement_array, R.layout.spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_type_emprunter.setAdapter(adapterType);

                }
                else if(categrorie.equals("Musique")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Emprunter.this,
                            R.array.type_music_array, R.layout.spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_type_emprunter.setAdapter(adapterType);

                }
                else if(categrorie.equals("Accessoires")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Emprunter.this,
                            R.array.type_accessoire_array, R.layout.spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_type_emprunter.setAdapter(adapterType);

                }
                else if(categrorie.equals("Autre")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Emprunter.this,
                            R.array.type_autre_array, R.layout.spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_type_emprunter.setAdapter(adapterType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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



    void showProducts(){
        if(search) {
            new ShowProductsTask().execute();
            search=false;
        }

    }

    private class ShowProductsTask extends AsyncTask<Void, Void, Void> {

        List<Product> products;


        public ShowProductsTask() {
            products = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Emprunter.this);
            pDialog.setMessage("Chargement des produits...");
            pDialog.setCancelable(false);
            pDialog.show();



        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://uge-webservice.herokuapp.com/api/product/key/"+keyword;
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Urllll",url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray arrayResult = new JSONArray(jsonStr);
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
                        products.add(product);
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

            AdapterProduitsRechercheEmprunt adapterProduitsRechercheEmprunt  = new AdapterProduitsRechercheEmprunt(products);

            RecyclerView_layout_emprunter.setLayoutManager(new LinearLayoutManager(Emprunter.this));

            RecyclerView_layout_emprunter.setAdapter(adapterProduitsRechercheEmprunt);

            search=true;

        }

    }
}
