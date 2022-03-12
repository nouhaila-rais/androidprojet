package fr.uge.projetandroid.shopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.User;

public class AccueilAchat extends AppCompatActivity {
    private TextView fatitest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_achat);
        fatitest= (TextView)findViewById(R.id.Tmouna);
        User u = (User)getIntent().getSerializableExtra("Mouna");

        fatitest.setText(u.getEmail()+"\n"+u.getPassword());



        Log.e("UserAccueilEmprunt",u.toString());
    }
}
