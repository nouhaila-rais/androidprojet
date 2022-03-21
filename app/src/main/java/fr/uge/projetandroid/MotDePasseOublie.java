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

import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.shopping.AfficherPanierAchat;
import fr.uge.projetandroid.shopping.AfficherWishlistAchat;

public class MotDePasseOublie extends AppCompatActivity {


    private ProgressDialog pDialog;
    private Button button_motdepasse_envoyer;
    private EditText editText_motdepasse_email;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mot_de_passe_oublie);
        button_motdepasse_envoyer=(Button)findViewById(R.id.button_motdepasse_envoyer);
        editText_motdepasse_email=(EditText)findViewById(R.id.editText_motdepasse_email);

        email = getIntent().getStringExtra("email");
        if(email!=null) editText_motdepasse_email.setText(email);


        button_motdepasse_envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=editText_motdepasse_email.getText().toString();
                new MotDePasseOublie.MotDePasseOublieTask().execute();
            }
        });

    }

    private class MotDePasseOublieTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MotDePasseOublie.this);
            pDialog.setMessage("Envoi en cours...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            String url = "http://projetandroiduge.herokuapp.com/api/forgetpw/"+email;
            HttpHandler sh = new HttpHandler();
            sh.makeServiceCall(url);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            Intent myIntent = new Intent(MotDePasseOublie.this, LoginActivity.class);
            myIntent.putExtra("email",email);
            startActivity(myIntent);
        }
    }
}
