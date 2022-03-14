package fr.uge.projetandroid.shopping;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.adapters.Adapter_Panier_Achat;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Product;

public class AfficherWishlistAchat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView RecyclerView_wishlist_achat;
    private List<Product> products;
    private ProgressDialog pDialog;
    private String TAG = AfficherWishlistAchat.class.getSimpleName();

    private TextView textView_total_wishlist_achat;
    private double total=0;

    private Button button_vider_wishlist_achat;
    private Button button_valider_wishlist_achat;


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
        setContentView(R.layout.activity_afficher_wishlist_achat);

        Intent myIntent = getIntent();
        devise = myIntent.getStringExtra("devise");
        rate = myIntent.getDoubleExtra("rate",1);
        user = (User)getIntent().getSerializableExtra("user");

        RecyclerView_wishlist_achat = (RecyclerView)findViewById(R.id.RecyclerView_wishlist_achat);
        textView_total_wishlist_achat = (TextView)findViewById(R.id.textView_total_wishlist_achat);
        button_vider_wishlist_achat=(Button)findViewById(R.id.button_vider_wishlist_achat);
        button_valider_wishlist_achat=(Button)findViewById(R.id.button_valider_wishlist_achat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new AfficherWishlistAchat.ShowProductsTask().execute();

        button_valider_wishlist_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AfficherWishlistAchat.ValiderWishlistTask().execute();
            }
        });

        button_vider_wishlist_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AfficherWishlistAchat.ViderWishlistTask().execute();
            }
        });
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
                R.array.devise, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                Log.e("Devise",charSequence.toString());
                devise = charSequence.toString();
                new AfficherWishlistAchat.ChangeCurrencyTask().execute();

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
                    Intent myIntent = new Intent(AfficherWishlistAchat.this, AfficherProduitsRechercheAchat.class);
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

    private class ShowProductsTask extends AsyncTask<Void, Void, Void> {

        public ShowProductsTask() {
            products = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherWishlistAchat.this);
            pDialog.setMessage("Chargement des produits...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String url = "http://uge-webservice.herokuapp.com/api/cart/productInWishlist/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "url: " + url);
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
                        total+=product.getPrice();
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

            Adapter_Panier_Achat adapterPanierAchat = new Adapter_Panier_Achat(products,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterPanierAchat);

            textView_total_wishlist_achat.setText(getPriceProduct(total));

        }

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
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Adapter_Panier_Achat adapterPanierAchat = new Adapter_Panier_Achat(products,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterPanierAchat);

            textView_total_wishlist_achat.setText(getPriceProduct(total));
        }
    }

    public String getPriceProduct(Double price){
        Double prix = price*rate;
        DecimalFormat df = new DecimalFormat("0.00");
        String result  = df.format(prix)+" " +devise;
        return result ;
    }

    private class ViderWishlistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://uge-webservice.herokuapp.com/api/wishlist/deleteAll/";
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);
            user.setTotalWishlist(0);
            total =0;
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Adapter_Panier_Achat adapterPanierAchat = new Adapter_Panier_Achat(products,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterPanierAchat);

            textView_total_wishlist_achat.setText(getPriceProduct(total));
        }
    }

    private class ValiderWishlistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://uge-webservice.herokuapp.com/api/wishlist/addInCart/";
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);
            user.setTotalPanier(user.getTotalPanier()+user.getTotalWishlist());
            user.setTotalWishlist(0);
            total =0;
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Adapter_Panier_Achat adapterPanierAchat = new Adapter_Panier_Achat(products,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterPanierAchat);

            textView_total_wishlist_achat.setText(getPriceProduct(total));
        }
    }
}
