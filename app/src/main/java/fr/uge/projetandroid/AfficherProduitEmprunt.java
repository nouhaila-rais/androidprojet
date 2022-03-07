package fr.uge.projetandroid;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AfficherProduitEmprunt extends AppCompatActivity {

    private ImageView Imageproduit;

    private ImageButton star1;
    private ImageButton star2;
    private ImageButton star3;
    private ImageButton star4;
    private ImageButton star5;

    private TextView textView_jour;
    private TextView textView_heure;
    private TextView textView_minute;
    private TextView textView_seconde;


    private String EVENT_DATE_TIME = "2020-03-31 05:02:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_jour, linear_layout_heure;
    private Handler handler = new Handler();
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_produit_emprunt);

        Imageproduit = (ImageView) findViewById(R.id.imageView_imageAfficherProduit_emprunt);
        star1=(ImageButton)findViewById(R.id.imageButton_rating_1);
        star2=(ImageButton)findViewById(R.id.imageButton_rating_2);
        star3=(ImageButton)findViewById(R.id.imageButton_rating_3);
        star4=(ImageButton)findViewById(R.id.imageButton_rating_4);
        star5=(ImageButton)findViewById(R.id.imageButton_rating_5);

        textView_jour = (TextView)findViewById(R.id.textView_countdown_jour);
        textView_heure = (TextView)findViewById(R.id.textView_countdown_heure);
        textView_minute = (TextView)findViewById(R.id.textView_countdown_minute);
        textView_seconde = (TextView)findViewById(R.id.textView_countdown_seconde);

        linear_layout_jour = findViewById(R.id.LinearLayout_Jour);
        linear_layout_heure = findViewById(R.id.LinearLayout_Heure);

        Picasso.get().load("http://marketingconfort.com/projetAndroid/IMG_20200329_012624_2710855663484451951.jpg")
                .resize(150, 150)
                .centerCrop()
                .error(R.drawable.ic_camera_alt_black_24dp)
                .into(Imageproduit);


        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star1);
                star2.setImageResource(R.drawable.star0);
                star3.setImageResource(R.drawable.star0);
                star4.setImageResource(R.drawable.star0);
                star5.setImageResource(R.drawable.star0);

            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star1);
                star2.setImageResource(R.drawable.star1);
                star3.setImageResource(R.drawable.star0);
                star4.setImageResource(R.drawable.star0);
                star5.setImageResource(R.drawable.star0);

            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star1);
                star2.setImageResource(R.drawable.star1);
                star3.setImageResource(R.drawable.star1);
                star4.setImageResource(R.drawable.star0);
                star5.setImageResource(R.drawable.star0);

            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star1);
                star2.setImageResource(R.drawable.star1);
                star3.setImageResource(R.drawable.star1);
                star4.setImageResource(R.drawable.star1);
                star5.setImageResource(R.drawable.star0);

            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star1);
                star2.setImageResource(R.drawable.star1);
                star3.setImageResource(R.drawable.star1);
                star4.setImageResource(R.drawable.star1);
                star5.setImageResource(R.drawable.star1);

            }
        });



        countDownStart();

    }





    private void initUI() {

    }

    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        textView_jour.setText(String.format("%02d", Days));
                        textView_heure.setText(String.format("%02d", Hours));
                        textView_minute.setText(String.format("%02d", Minutes));
                        textView_seconde.setText(String.format("%02d", Seconds));
                    } else {
                        linear_layout_jour.setVisibility(View.VISIBLE);
                        linear_layout_heure.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
