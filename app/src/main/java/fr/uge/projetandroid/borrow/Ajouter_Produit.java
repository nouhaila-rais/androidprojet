package fr.uge.projetandroid.borrow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Product;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.messages.Produit_Ajoute;

public class Ajouter_Produit extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,NavigationView.OnNavigationItemSelectedListener {

    private Spinner spinnerEtat;
    private Spinner spinnerCategorie;
    private Spinner spinnerType;
    private Product product;
    private EditText editTextNom;
    private EditText editTextPrix;
    private EditText editTextDescrition;
    private ImageButton buttonUploadImage;
    private Button buttonAjouter;
    private ProgressDialog pDialog;
    private ImageView imageView;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";
    int serverResponseCode = 0;
    String upLoadServerUri = "http://makcenter.ma/uge/projetAndroid/uploadimage.php";
    String uploadFilePath;
    String uploadFileName;
    int ResponseCodePhpServer = 0;

    private TextView textView_nombre_notifications_emprunt;
    private TextView textView_nombre_panier_emprunt;
    private TextView Textview_nom_prenom_utilisateur_emprunt;
    private TextView Textview_email_utilisateur_emprunt;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);


        user = (User) getIntent().getSerializableExtra("user");

        product = new Product();

        spinnerEtat = (Spinner) findViewById(R.id.spinner_ajouterProduit_etat);
        spinnerCategorie = (Spinner) findViewById(R.id.spinner_ajouterProduit_categorie);
        spinnerType = (Spinner) findViewById(R.id.spinner_ajouterProduit_type);
        editTextNom = (EditText) findViewById(R.id.editText_ajouterProduit_nom);
        editTextPrix = (EditText) findViewById(R.id.editText_ajouterProduit_prix);
        editTextDescrition = (EditText) findViewById(R.id.editText_ajouterProduit_description);
        buttonUploadImage = (ImageButton) findViewById(R.id.button_ajouterProduit_photo);
        buttonAjouter = (Button) findViewById(R.id.button_ajouterproduit_ajouter);
        imageView = findViewById(R.id.image);


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
                Log.e("etat", charSequence.toString());
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
                Log.e("type", charSequence.toString());
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
                Log.e("categorie", charSequence.toString());


                if (categrorie.equals("Bibliotheque")) {
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Ajouter_Produit.this,
                            R.array.type_bibliotheque_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                } else if (categrorie.equals("Electronique")) {
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Ajouter_Produit.this,
                            R.array.type_electronique_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);


                } else if (categrorie.equals("Mode et vetements")) {
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Ajouter_Produit.this,
                            R.array.type_modevetement_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                } else if (categrorie.equals("Musique")) {
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Ajouter_Produit.this,
                            R.array.type_music_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                } else if (categrorie.equals("Accessoires")) {
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Ajouter_Produit.this,
                            R.array.type_accessoire_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                } else if (categrorie.equals("Autre")) {
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(Ajouter_Produit.this,
                            R.array.type_autre_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setName(editTextNom.getText().toString());
                product.setPrice(Double.parseDouble(editTextPrix.getText().toString()));
                product.setDescription(editTextDescrition.getText().toString());
                new Ajouter_Produit.AddProductTask().execute();
            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
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


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(Uri.parse(imageFilePath));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        uploadFileName = image.getName();
        uploadFilePath = imageFilePath;

        return image;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void setupBadge() {

        if (textView_nombre_notifications_emprunt != null) {
            if (user.getTotalNotification() == 0) {
                if (textView_nombre_notifications_emprunt.getVisibility() != View.GONE) {
                    textView_nombre_notifications_emprunt.setVisibility(View.GONE);
                }
            } else {
                textView_nombre_notifications_emprunt.setText(String.valueOf(Math.min(user.getTotalNotification(), 99)));
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
                textView_nombre_panier_emprunt.setText(String.valueOf(Math.min(user.getTotalProduitEmprunte(), 99)));
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
        Textview_nom_prenom_utilisateur_emprunt = (TextView) findViewById(R.id.Textview_nom_prenom_utilisateur_emprunt);
        Textview_email_utilisateur_emprunt = (TextView) findViewById(R.id.Textview_email_utilisateur_emprunt);
        Textview_nom_prenom_utilisateur_emprunt.setText(user.getFirstName() + " " + user.getLastName());
        Textview_email_utilisateur_emprunt.setText(user.getEmail());
        getMenuInflater().inflate(R.menu.main_emprunt, menu);


        final MenuItem menuItemPanier = menu.findItem(R.id.item_nombre_panier_emprunt);
        View actionViewPanier = menuItemPanier.getActionView();
        textView_nombre_panier_emprunt = (TextView) actionViewPanier.findViewById(R.id.textView_nombre_panier_emprunt);

        final MenuItem menuItemNotification = menu.findItem(R.id.item_notifiction_emprunt);
        View actionViewNotification = menuItemNotification.getActionView();
        textView_nombre_notifications_emprunt = (TextView) actionViewNotification.findViewById(R.id.textView_nombre_notifications_emprunt);


        MenuItem mSearch = menu.findItem(R.id.item_search_emprunt);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    Intent myIntent = new Intent(Ajouter_Produit.this, Afficher_ProduitsRecherche_Emprunt.class);
                    myIntent.putExtra("user", user);
                    myIntent.putExtra("Keyword", query);
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
            myIntent.putExtra("user", user);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.item_nombre_panier_emprunt) {
            Intent myIntent = new Intent(this, Afficher_MesProduits_Emprunte.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.item_search_emprunt) {
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
            myIntent.putExtra("user", user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_retourner) {
            Intent myIntent = new Intent(this, Afficher_MesProduits_Emprunte.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_emprunter) {
            Intent myIntent = new Intent(this, Emprunter.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);

        } else if (id == R.id.nav__emprunt_mesproduits) {

            Intent myIntent = new Intent(this, Afficher_Produit_Ajoute.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);
        } else if (id == R.id.nav__emprunt_ajouterproduit) {
            Intent myIntent = new Intent(this, Ajouter_Produit.class);
            myIntent.putExtra("user", user);
            startActivity(myIntent);
        } else if (id == R.id.nav__emprunt_deconnexion) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AddProductTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Ajouter_Produit.this);
            pDialog.setMessage("Ajout en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String fileName = uploadFilePath;
            String path = "http://makcenter.ma/uge/projetAndroid/" + uploadFileName;
            product.setPath(path);
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(uploadFilePath);

            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File not exist :"
                        + uploadFilePath + "" + uploadFileName);

            } else {
                Log.e("path", uploadFilePath);
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    ResponseCodePhpServer = serverResponseCode;
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.e("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);


                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                    if (serverResponseCode == 200) {

                        HttpURLConnection urlConnection;
                        String url2 = "http://uge-webservice.herokuapp.com/api/product/";
                        String data = product.toJson();
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
                            Log.e("Erreur", e.getMessage());
                        }
                        //return result;
                    }

                } catch (MalformedURLException ex) {


                    ex.printStackTrace();


                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {


                    e.printStackTrace();


                    Log.e("Upload file error", "Exception : "
                            + e.getMessage(), e);
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(ResponseCodePhpServer==200){
                Intent myIntent = new Intent(Ajouter_Produit.this, Produit_Ajoute.class);
                myIntent.putExtra("idProduct",product.getId());
                myIntent.putExtra("user",user);
                startActivity(myIntent);
            }
            else {
                Toast.makeText(Ajouter_Produit.this, "Erreur de connexion veuillez reessayer", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
