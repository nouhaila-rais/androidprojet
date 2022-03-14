package fr.uge.projetandroid.borrow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.adapters.Adapter_Panier_Emprunt;
import fr.uge.projetandroid.entities.Borrow;
import fr.uge.projetandroid.entities.Product;

public class Afficher_MesProduits_Emprunte extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView RecyclerView_ProduitEmprunte;
    private ProgressDialog pDialog;
    private String TAG = "Afficher_MesProduits_Emprunte";

    private TextView textView_nombre_notifications_emprunt;
    private TextView textView_nombre_panier_emprunt;
    private TextView Textview_nom_prenom_utilisateur_emprunt;
    private TextView Textview_email_utilisateur_emprunt;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_produits_emprunte);

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
        initUi();
        new Afficher_MesProduits_Emprunte.ShowProductsTask().execute();
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
                    Intent myIntent = new Intent(Afficher_MesProduits_Emprunte.this, Afficher_ProduitsRecherche_Emprunt.class);
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
            Intent myIntent = new Intent(this, Afficher_Notifications_Emprunt.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);
            return true;
        }
        else if (id == R.id.item_nombre_panier_emprunt) {
            Intent myIntent = new Intent(this, Afficher_MesProduits_Emprunte.class);
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

            Intent myIntent = new Intent(this, Accueil_Emprunt.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_retourner) {
            Intent myIntent = new Intent(this, Afficher_MesProduits_Emprunte.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_emprunter) {
            Intent myIntent = new Intent(this, Emprunter.class);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_mesproduits) {

            Intent myIntent = new Intent(this, Afficher_Produit_Ajoute.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);
        }

        else if (id == R.id.nav__emprunt_ajouterproduit) {
            Intent myIntent = new Intent(this, Ajouter_Produit.class);
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
            pDialog = new ProgressDialog(Afficher_MesProduits_Emprunte.this);
            pDialog.setMessage("Chargement des produits...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "http://uge-webservice.herokuapp.com/api/user/"+user.getId();
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

            Adapter_Panier_Emprunt adapterPanierEmprunt = new Adapter_Panier_Emprunt(produitsEmprunte,borrows,user);

            RecyclerView_ProduitEmprunte.setLayoutManager(new LinearLayoutManager(Afficher_MesProduits_Emprunte.this));

            RecyclerView_ProduitEmprunte.setAdapter(adapterPanierEmprunt);

        }

    }
}
