package fr.uge.projetandroid.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fr.uge.projetandroid.LoginActivity;
import fr.uge.projetandroid.R;

public class Inscription_Succes extends AppCompatActivity {

    private Button button_Seconnecter;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_succes);
        email = getIntent().getStringExtra("email");


        button_Seconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inscription_Succes.this, LoginActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }
}
