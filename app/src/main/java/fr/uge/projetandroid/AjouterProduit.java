package fr.uge.projetandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import fr.uge.projetandroid.entities.Product;

public class AjouterProduit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerEtat;
    private Spinner spinnerCategorie;
    private Spinner spinnerType;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);

        product = new Product();

        spinnerEtat = (Spinner) findViewById(R.id.spinner_ajouterProduit_etat);
        spinnerCategorie = (Spinner) findViewById(R.id.spinner_ajouterProduit_categorie);
        spinnerType  = (Spinner) findViewById(R.id.spinner_ajouterProduit_type);


        ArrayAdapter<CharSequence> adapterEtat = ArrayAdapter.createFromResource(this,
                R.array.etat_array, android.R.layout.simple_spinner_item);
        adapterEtat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtat.setAdapter(adapterEtat);
        spinnerEtat.setOnItemSelectedListener(this);



        ArrayAdapter<CharSequence> adapterCategorie = ArrayAdapter.createFromResource(this,
                R.array.categorie_array, android.R.layout.simple_spinner_item);
        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapterCategorie);
        spinnerCategorie.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type_bibliotheque_array, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setOnItemSelectedListener(this);


        spinnerEtat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                Log.e("etat",charSequence.toString());
                product.setState(charSequence.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                Log.e("type",charSequence.toString());
                product.setType(charSequence.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence charSequence = (CharSequence) parent.getItemAtPosition(position);
                String categrorie = charSequence.toString();

                product.setCategory(categrorie);

                System.out.println("Item : " + charSequence.toString());
                Log.e("categorie",charSequence.toString());


                if(categrorie.equals("Bibliotheque")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_bibliotheque_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                    spinnerType.setOnItemSelectedListener(this);

                }
                else if(categrorie.equals("Electronique")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_electronique_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                    spinnerType.setOnItemSelectedListener(this);

                }
                else if(categrorie.equals("Mode et vetements")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_modevetement_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                    spinnerType.setOnItemSelectedListener(this);

                }
                else if(categrorie.equals("Musique")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_music_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                    spinnerType.setOnItemSelectedListener(this);

                }
                else if(categrorie.equals("Accessoires")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_accessoire_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                    spinnerType.setOnItemSelectedListener(this);

                }
                else if(categrorie.equals("Autre")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_autre_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                    spinnerType.setOnItemSelectedListener(this);

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
}
