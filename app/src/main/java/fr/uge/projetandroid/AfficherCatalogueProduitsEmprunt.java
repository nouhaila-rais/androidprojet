package fr.uge.projetandroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.uge.projetandroid.adapters.AdapterCatalogueProduitsEmprunt;
import fr.uge.projetandroid.entities.Comment;
import fr.uge.projetandroid.entities.Product;
import fr.uge.projetandroid.entities.User;

public class AfficherCatalogueProduitsEmprunt extends AppCompatActivity {


    private RecyclerView RecyclerView_CatalogueProduit_Bibliotheque_Emprunt;
    private RecyclerView RecyclerView_CatalogueProduit_Electronique_Emprunt;
    private RecyclerView RecyclerView_CatalogueProduit_ModeVetement_Emprunt;
    private ProgressDialog pDialog;
    private String TAG = AfficherProduitEmprunt.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_catalogue_produits_emprunt);
        initUi();
        new AfficherCatalogueProduitsEmprunt.ShowProductsTask().execute();
    }

    private void initUi(){
        RecyclerView_CatalogueProduit_Bibliotheque_Emprunt = (RecyclerView)findViewById(R.id.RecyclerView_CatalogueProduit_Bibliotheque_Emprunt);
        //RecyclerView_CatalogueProduit_Electronique_Emprunt = (RecyclerView)findViewById(R.id.RecyclerView_CatalogueProduit_Electronique_Emprunt);
        //RecyclerView_CatalogueProduit_ModeVetement_Emprunt = (RecyclerView)findViewById(R.id.RecyclerView_CatalogueProduit_ModeVetement_Emprunt);
    }

    private class ShowProductsTask extends AsyncTask<Void, Void, Void> {

        List<Product> produitsBibliotheque;
        List<Product> produitsElectronique;
        List<Product> produitsModeVetements;

        public ShowProductsTask() {
            produitsBibliotheque = new ArrayList<>();
            produitsElectronique = new ArrayList<>();
            produitsModeVetements = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherCatalogueProduitsEmprunt.this);
            pDialog.setMessage("Chargement des produits...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            produitsBibliotheque = getProductsBycategory("Electronique");
            // produitsElectronique = getProductsBycategory("Electronique");
            // produitsModeVetements = getProductsBycategory("eau");
            return null;
        }

        protected List<Product> getProductsBycategory(String category){
            List<Product> resultats= new ArrayList<>();
            //String url = "http://uge-webservice.herokuapp.com/api/product/category/"+category+"/";
            //String url = "http://uge-webservice.herokuapp.com/api/product/category/Electronique/";
            String url = "http://uge-webservice.herokuapp.com/api/product/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);



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
                        resultats.add(product);
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

            return resultats;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            AdapterCatalogueProduitsEmprunt adapterBibliotheque = new AdapterCatalogueProduitsEmprunt(produitsBibliotheque);
            // AdapterCatalogueProduitsEmprunt adapterElectronique = new AdapterCatalogueProduitsEmprunt(produitsElectronique);
            //AdapterCatalogueProduitsEmprunt adapterModeVetement = new AdapterCatalogueProduitsEmprunt(produitsModeVetements);

            RecyclerView_CatalogueProduit_Bibliotheque_Emprunt.setLayoutManager(new LinearLayoutManager(AfficherCatalogueProduitsEmprunt.this));
            //RecyclerView_CatalogueProduit_Electronique_Emprunt.setLayoutManager(new LinearLayoutManager(AfficherCatalogueProduitsEmprunt.this));
            //RecyclerView_CatalogueProduit_ModeVetement_Emprunt.setLayoutManager(new LinearLayoutManager(AfficherCatalogueProduitsEmprunt.this));

            RecyclerView_CatalogueProduit_Bibliotheque_Emprunt.setAdapter(adapterBibliotheque);
            //RecyclerView_CatalogueProduit_Electronique_Emprunt.setAdapter(adapterElectronique);
            //RecyclerView_CatalogueProduit_ModeVetement_Emprunt.setAdapter(adapterModeVetement);
        }

    }
}
