package fr.uge.projetandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.messages.Inscription_Succes;

public class InscriptionActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private EditText editText_nom_inscription;
    private EditText editText_prenom_inscription;
    private EditText editText_email_inscription;
    private EditText editText_motdepasse_inscription;
    private EditText editText_telephone_inscription;
    private EditText editText_adresse_inscription;
    private Button button_inscription;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        String email = getIntent().getStringExtra("email");

        editText_nom_inscription=(EditText)findViewById(R.id.editText_nom_inscription);
        editText_prenom_inscription=(EditText)findViewById(R.id.editText_prenom_inscription);
        editText_email_inscription=(EditText)findViewById(R.id.editText_email_inscription);
        editText_motdepasse_inscription=(EditText)findViewById(R.id.editText_motdepasse_inscription);
        editText_telephone_inscription=(EditText)findViewById(R.id.editText_telephone_inscription);
        editText_adresse_inscription=(EditText)findViewById(R.id.editText_adresse_inscription);
        button_inscription=(Button)findViewById(R.id.button_inscription);

        if(email!=null) editText_email_inscription.setText(email);
        user = new User();
        button_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setLastName(editText_nom_inscription.getText().toString());
                user.setFirstName(editText_prenom_inscription.getText().toString());
                user.setEmail(editText_email_inscription.getText().toString());
                user.setPassword(editText_motdepasse_inscription.getText().toString());
                user.setPhone(editText_telephone_inscription.getText().toString());
                user.setAddress(editText_adresse_inscription.getText().toString());
                user.setRole("Customer");
                new InscriptionActivity.InscriptionTask().execute();
            }
        });

    }

    private class InscriptionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InscriptionActivity.this);
            pDialog.setMessage("Inscription en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpURLConnection urlConnection;
            String url2 = "https://projetandroiduge.herokuapp.com/register";
            String data = user.userRegisterToJson();
            Log.e("user",user.userRegisterToJson());
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.e("inscription","votre compte a bien été créé");
            test();
        }
    }

    void test(){
        Intent intent = new Intent(this, Inscription_Succes.class);
        intent.putExtra("email",user.getEmail());
        startActivity(intent);
    }
}

