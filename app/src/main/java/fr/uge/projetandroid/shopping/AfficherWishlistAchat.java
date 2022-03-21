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
import android.widget.ImageButton;
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
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Product;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

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
    private Boolean ChangeCurrency=false;
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
                    new AfficherWishlistAchat.ChangeCurrencyTask().execute();
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


            String url = "http://projetandroiduge.herokuapp.com/api/wishlist/productInWishlist/"+user.getId();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            total=0;
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

            AdapterWishlistAchat adapterWishlistAchat  = new AdapterWishlistAchat(products,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterWishlistAchat);

            textView_total_wishlist_achat.setText(getPriceProduct(total));

        }

    }



    private class SupprimerProduitTask extends AsyncTask<Void, Void, Void> {

        private long idProduct;

        public SupprimerProduitTask(long idProduct) {
            this.idProduct = idProduct;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherWishlistAchat.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://projetandroiduge.herokuapp.com/api/wishlist/product/"+idProduct+"/"+user.getId();
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            int total =user.getTotalWishlist()-1;
            if(total<=0) total=0;
            user.setTotalWishlist(total);
            setupBadge();
            new AfficherWishlistAchat.ShowProductsTask().execute();
        }
    }



    private class ChangeCurrencyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://projetandroiduge.herokuapp.com/api/currency/"+devise;
            HttpHandler sh = new HttpHandler();
            String result = sh.makeServiceCall(url);
            rate = Double.parseDouble(result);
            setupBadge();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AdapterWishlistAchat adapterWishlistAchat  = new AdapterWishlistAchat(products,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterWishlistAchat);

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
            pDialog = new ProgressDialog(AfficherWishlistAchat.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://projetandroiduge.herokuapp.com/api/wishlist/deleteAll/"+user.getId();
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            user.setTotalWishlist(0);
            total =0;
            setupBadge();
            List<Product> res = new ArrayList<>();

            AdapterWishlistAchat adapterWishlistAchat  = new AdapterWishlistAchat(res,user,devise,rate);

            RecyclerView_wishlist_achat.setLayoutManager(new LinearLayoutManager(AfficherWishlistAchat.this));

            RecyclerView_wishlist_achat.setAdapter(adapterWishlistAchat);

            textView_total_wishlist_achat.setText(getPriceProduct(total));
        }
    }

    private class ValiderWishlistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherWishlistAchat.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://projetandroiduge.herokuapp.com/api/wishlist/addInCart/"+user.getId();
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            user.setTotalPanier(user.getTotalPanier()+user.getTotalWishlist());
            user.setTotalWishlist(0);
            total =0;
            setupBadge();

            Intent myIntent = new Intent(AfficherWishlistAchat.this, AfficherPanierAchat.class);
            myIntent.putExtra("user",user);
            myIntent.putExtra("devise",devise);
            myIntent.putExtra("rate",rate);
            startActivity(myIntent);
        }
    }


    public class AdapterWishlistAchat extends RecyclerView.Adapter<AdapterWishlistAchat.ViewHolder> {

        private List<Product> results;
        private User user;
        private String devise;
        private Double rate;


        public AdapterWishlistAchat(List<Product> results, User user, String devise, Double rate) {
            this.results = results;
            this.user = user;
            this.devise = devise;
            this.rate = rate;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_produit_wishlist_achat, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.update(results.get(i));
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        public void setResults(List<Product> results) {
            this.results = results;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            private TextView textView_nomProduit_wishlist_achat;
            private TextView textView_typeCategorie_wishlist_achat;
            private TextView textView_prix_wishlist_achat;
            private ImageView imageView_imageProduit_wishlist_achat;
            private ImageView imageView_ratingStar_wishlist_achat;
            private LinearLayout LinearLayout_produit_wishlist_achat;
            private ImageButton imageButton_wishlist_delete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_nomProduit_wishlist_achat = itemView.findViewById(R.id.textView_nomProduit_wishlist_achat);
                textView_typeCategorie_wishlist_achat = itemView.findViewById(R.id.textView_typeCategorie_wishlist_achat);
                textView_prix_wishlist_achat = itemView.findViewById(R.id.textView_prix_wishlist_achat);
                imageView_imageProduit_wishlist_achat = itemView.findViewById(R.id.imageView_imageProduit_wishlist_achat);
                imageView_ratingStar_wishlist_achat = itemView.findViewById(R.id.imageView_ratingStar_wishlist_achat);
                LinearLayout_produit_wishlist_achat = itemView.findViewById(R.id.LinearLayout_produit_wishlist_achat);
                imageButton_wishlist_delete=itemView.findViewById(R.id.imageButton_wishlist_delete);
            }

            public void update(final Product entity){

                textView_nomProduit_wishlist_achat.setText(entity.getName());
                textView_typeCategorie_wishlist_achat.setText(entity.getCategory()+" > "+entity.getType());
                textView_prix_wishlist_achat.setText(getPriceProduct(entity.getPrice()));
                setImageRatingStar(imageView_ratingStar_wishlist_achat, entity.getRate());

                Picasso.get().load(entity.getPath())
                        .resize(150, 150)
                        .centerCrop()
                        .error(R.drawable.erreurpicture)
                        .into(imageView_imageProduit_wishlist_achat);



                LinearLayout_produit_wishlist_achat.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent myIntent = new Intent(v.getContext(), AfficherProduitAchat.class);
                        myIntent.putExtra("idProduct",entity.getId());
                        myIntent.putExtra("user",user);
                        myIntent.putExtra("devise",devise);
                        myIntent.putExtra("rate",rate);
                        v.getContext().startActivity(myIntent);
                    }
                });

                imageButton_wishlist_delete.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        deleteProduit(entity.getId());
                    }
                });

            }
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

        public String getPriceProduct(Double price){
            Double prix = price*rate;
            DecimalFormat df = new DecimalFormat("0.00");
            String result  = df.format(prix)+" " +devise;
            return result ;
        }

    }

    void deleteProduit(long idProduct){
        new AfficherWishlistAchat.SupprimerProduitTask(idProduct).execute();
    }
}
