package fr.uge.projetandroid.borrow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Product;

public class AjouterProduit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    /************* Php script path ****************/
    String upLoadServerUri = "http://makcenter.ma/uge/projetAndroid/uploadimage.php";
    /**********  File Path *************/
    String uploadFilePath ;
    String uploadFileName ;
    int ResponseCodePhpServer=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);

        product = new Product();

        spinnerEtat = (Spinner) findViewById(R.id.spinner_ajouterProduit_etat);
        spinnerCategorie = (Spinner) findViewById(R.id.spinner_ajouterProduit_categorie);
        spinnerType  = (Spinner) findViewById(R.id.spinner_ajouterProduit_type);
        editTextNom = (EditText)  findViewById(R.id.editText_ajouterProduit_nom);
        editTextPrix = (EditText)  findViewById(R.id.editText_ajouterProduit_prix);
        editTextDescrition = (EditText)  findViewById(R.id.editText_ajouterProduit_description);
        buttonUploadImage = (ImageButton)  findViewById(R.id.button_ajouterProduit_photo);
        buttonAjouter = (Button)  findViewById(R.id.button_ajouterproduit_ajouter);
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

                }
                else if(categrorie.equals("Electronique")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_electronique_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);


                }
                else if(categrorie.equals("Mode et vetements")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_modevetement_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                }
                else if(categrorie.equals("Musique")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_music_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                }
                else if(categrorie.equals("Accessoires")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
                            R.array.type_accessoire_array, android.R.layout.simple_spinner_item);
                    adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapterType);

                }
                else if(categrorie.equals("Autre")){
                    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(AjouterProduit.this,
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
                new AddProductTask().execute();
            }
        });



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });


    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", photoFile);
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
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        uploadFileName=image.getName();
        uploadFilePath=imageFilePath;

        return image;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class AddProductTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AjouterProduit.this);
            pDialog.setMessage("Ajout en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String fileName = uploadFilePath;
            String path = "http://makcenter.ma/uge/projetAndroid/"+uploadFileName;
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
                        +uploadFilePath + "" + uploadFileName);

            }
            else {
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
                    ResponseCodePhpServer=serverResponseCode;
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
                            System.out.println(e.getMessage());
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
                Toast.makeText(AjouterProduit.this, "Produit ajouté avec succes", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AjouterProduit.this, "Erreur de connexion veuillez reessayer", Toast.LENGTH_SHORT).show();
            }

            Log.e("produit2",product.toJson());


        }

    }
}
