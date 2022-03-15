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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.adapters.AdapterComment;
import fr.uge.projetandroid.entities.Comment;
import fr.uge.projetandroid.entities.Product;


public class AfficherProduitAchat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ImageView imageView_imageAfficherProduit_achat;
    private ImageView imageView_ratingstar_achat;


    private TextView textView_categorie_type_achat;
    private TextView textView_nom_achat;
    private TextView textView_description_produit_achat;
    private TextView textView_etat_produit_achat;
    private TextView textView_prix_afficher_produit_achat;


    private RecyclerView listView_listAvis_achat;
    private AdapterComment adapterComment;


    private Button button_afficher_produit_ajouterPanier_achat;
    private Button button_afficher_produit_ajouterWishlist_achat;



    private long idNotification;
    private int avgRate;

    private Product product;
    private String url;
    private String TAG = AfficherProduitAchat.class.getSimpleName();


    private ProgressDialog pDialog;
    private long idProduct;


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
        setContentView(R.layout.activity_afficher_produit_achat);

        Intent myIntent = getIntent();
        idProduct = myIntent.getLongExtra("idProduct",0);
        devise = myIntent.getStringExtra("devise");
        rate = myIntent.getDoubleExtra("rate",1);
        user = (User)getIntent().getSerializableExtra("user");

        Log.e("idProductAfficher","->>"+idProduct+"");

        product = new Product();
        url = "http://uge-webservice.herokuapp.com/api/product/"+idProduct;


        initUi();
        new AfficherProduitAchat.ShowProductTask().execute();



        button_afficher_produit_ajouterPanier_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AfficherProduitAchat.AddProductCartTask().execute();

            }
        });

        button_afficher_produit_ajouterWishlist_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AfficherProduitAchat.AddProductWishlistTask().execute();

            }
        });


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

    private void initUi(){
        button_afficher_produit_ajouterPanier_achat =(Button)findViewById(R.id.button_afficher_produit_ajouterPanier_achat);
        button_afficher_produit_ajouterWishlist_achat =(Button)findViewById(R.id.button_afficher_produit_ajouterWishlist_achat);

        imageView_imageAfficherProduit_achat = (ImageView) findViewById(R.id.imageView_imageAfficherProduit_achat);
        imageView_ratingstar_achat = (ImageView) findViewById(R.id.imageView_ratingstar_achat);

        textView_categorie_type_achat =(TextView)findViewById(R.id.textView_categorie_type_achat);
        textView_nom_achat =(TextView)findViewById(R.id.textView_nom_achat);
        textView_description_produit_achat =(TextView)findViewById(R.id.textView_description_produit_achat);
        textView_etat_produit_achat =(TextView)findViewById(R.id.textView_etat_produit_achat);
        textView_prix_afficher_produit_achat=(TextView)findViewById(R.id.textView_prix_afficher_produit_achat);

        listView_listAvis_achat = (RecyclerView)findViewById(R.id.listView_listAvis_achat);

        adapterComment = new AdapterComment(product.getComments());
        listView_listAvis_achat.setLayoutManager(new LinearLayoutManager(AfficherProduitAchat.this));
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
                new AfficherProduitAchat.ChangeCurrencyTask().execute();
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
                    Intent myIntent = new Intent(AfficherProduitAchat.this, AfficherProduitsRechercheAchat.class);
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




    public void setImageRatingStar(ImageView imageView,  int rate){
        switch(rate) {
            case 0:
                imageView.setImageResource(R.drawable.star_0);
                break;
            case 5:
                imageView.setImageResource(R.drawable.star_5);
                break;
            case 10:
                imageView.setImageResource(R.drawable.star_10);
                break;
            case 15:
                imageView.setImageResource(R.drawable.star_15);
                break;
            case 20:
                imageView.setImageResource(R.drawable.star_20);
                break;
            case 25:
                imageView.setImageResource(R.drawable.star_25);
                break;
            case 30:
                imageView.setImageResource(R.drawable.star_30);
                break;
            case 35:
                imageView.setImageResource(R.drawable.star_35);
                break;
            case 40:
                imageView.setImageResource(R.drawable.star_40);
                break;
            case 45:
                imageView.setImageResource(R.drawable.star_45);
                break;
            case 50:
                imageView.setImageResource(R.drawable.star_50);
                break;
            default:
                imageView.setImageResource(R.drawable.star_0);
        }
    }


    private class ShowProductTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherProduitAchat.this);
            pDialog.setMessage("Chargement du produit...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);



            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
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
                    avgRate = jsonObj.getInt("avgRate");

                    JSONArray arrayResult = jsonObj.getJSONArray("comments");


                    Log.e("Taille comments : " ,arrayResult.length()+"");
                    for (int i = 0; i < arrayResult.length(); i++) {

                        JSONObject a = arrayResult.getJSONObject(i);

                        Comment c = new Comment();
                        c.setId(a.getInt("id"));
                        c.setContent(a.getString("content"));
                        c.setRate(a.getInt("rate"));
                        c.setCreatedAt(a.getString("createdAt"));
                        c.setFirstName(a.getString("firstName"));
                        c.setLastName(a.getString("lastName"));


                        Log.e("Commentaire json" , c.toJson());
                        product.addComment(c);

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

            Picasso.get().load(product.getPath())
                    .error(R.drawable.erreurpicture)
                    .into(imageView_imageAfficherProduit_achat);

            textView_categorie_type_achat.setText(product.getCategory()+" > "+ product.getType());
            textView_nom_achat.setText(product.getName());
            textView_description_produit_achat.setText(product.getDescription());
            textView_etat_produit_achat.setText(product.getState());
            setImageRatingStar(imageView_ratingstar_achat,avgRate);
            textView_prix_afficher_produit_achat.setText(getPriceProduct());

            adapterComment.setResults(product.getComments());

            listView_listAvis_achat.setAdapter(new AdapterComment(product.getComments()));

        }

    }







    private class AddProductCartTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://uge-webservice.herokuapp.com/api/cart/add/"+product.getId()+"/"+user.getId();
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);
            user.setTotalPanier(user.getTotalPanier()+1);
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),
                    "Produit ajouté avec succes",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    private class AddProductWishlistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://uge-webservice.herokuapp.com/api/wishlist/add/"+product.getId()+"/"+user.getId();
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);
            user.setTotalPanier(user.getTotalPanier()+1);
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),
                    "Produit ajouté avec succes",
                    Toast.LENGTH_LONG)
                    .show();
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
            textView_prix_afficher_produit_achat.setText(getPriceProduct());
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public String getPriceProduct(){
        Double prix = product.getPrice()*rate;
        DecimalFormat df = new DecimalFormat("0.00");
        String result  = df.format(prix)+" " +devise;
        return result ;
    }
}
