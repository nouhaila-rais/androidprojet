package fr.uge.projetandroid;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.uge.projetandroid.adapters.AdapterComment;
import fr.uge.projetandroid.entities.Borrow;
import fr.uge.projetandroid.entities.Comment;
import fr.uge.projetandroid.entities.Product;
import fr.uge.projetandroid.entities.RequestBorrow;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.fragments.DatePickerFragment;
import fr.uge.projetandroid.fragments.TimePickerFragment;
import fr.uge.projetandroid.handlers.HttpHandler;

public class AfficherProduitEmprunt extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ImageView Imageproduit_emprunt;
    private ImageView imageView_ratingstar_emprunt;

    private ImageButton star1;
    private ImageButton star2;
    private ImageButton star3;
    private ImageButton star4;
    private ImageButton star5;

    private TextView textView_categorie_type_emprunt;
    private TextView textView_nom_emprunt;
    private TextView textView_description_produit_emprunt;
    private TextView textView_etat_produit_emprunt;
    private TextView textView_jour;
    private TextView textView_heure;
    private TextView textView_minute;
    private TextView textView_seconde;


    private RecyclerView listView_listAvis;
    private AdapterComment adapterComment;
    private EditText editText_commentaire;

    private Button button_emprunter;
    private Button button_demandeEmprunt;
    private Button button_nouveauEmprunt;
    private Button button_nouvelleDemandeEmprunt;
    private Button button_ajoutercommentaire;
    private Button button_dateDebut_emprunt;
    private Button button_heureDebut_emprunt;
    private Button button_dateFin_emprunt;
    private Button button_heureFin_emprunt;
    private Button button_dateDebut_Demande_emprunt;
    private Button button_heureDebut_Demande_emprunt;
    private Button button_dateFin_Demande_emprunt;
    private Button button_heureFin_Demande_emprunt;



    public LinearLayout layout_boutton_emprunt;
    public LinearLayout layout_saisirDate_emprunt;
    public LinearLayout layout_demande_emprunt_boutton;
    public LinearLayout layout_saisirDate_DemandeEmprunt;
    public LinearLayout layout_demande_emprunt_countdown;


    private String DATE_COUNTDOWN = "2020-04-31 05:02:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_jour, linear_layout_heure;
    private Handler handler = new Handler();
    private Runnable runnable;


    private int note=0;
    private int avgRate=0;

    private Product product;
    private String date;
    private String heure;
    private String url;
    private String TAG = AfficherProduitEmprunt.class.getSimpleName();

    private DialogFragment datePickerDebutEmprunt;
    private DialogFragment datePickerFinEmprunt;
    private DialogFragment timePickerDebutEmprunt;
    private DialogFragment timePickerFinEmprunt;
    private DialogFragment datePickerDebutDemandeEmprunt;
    private DialogFragment datePickerFinDemandeEmprunt;
    private DialogFragment timePickerDebutDemandeEmprunt;
    private DialogFragment timePickerFinDemandeEmprunt;

    private ProgressDialog pDialog;


    private int TYPE_DIALOG_FRAGMENT = 0;
    private static final int DIALOG_FRAGMENT_DATE_DEBUT_EMPRUNT = 1;
    private static final int DIALOG_FRAGMENT_DATE_FIN_EMPRUNT = 2;
    private static final int DIALOG_FRAGMENT_TIME_DEBUT_EMPRUNT = 3;
    private static final int DIALOG_FRAGMENT_TIME_FIN_EMPRUNT = 4;
    private static final int DIALOG_FRAGMENT_DATE_DEBUT_DEMANDE_EMPRUNT = 5;
    private static final int DIALOG_FRAGMENT_DATE_FIN_DEMANDE_EMPRUNT = 6;
    private static final int DIALOG_FRAGMENT_TIME_DEBUT_DEMANDE_EMPRUNT = 7;
    private static final int DIALOG_FRAGMENT_TIME_FIN_DEMANDE_EMPRUNT = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_produit_emprunt);

        Intent myIntent = getIntent(); // gets the previously created intent
        String idProduct = myIntent.getStringExtra("idProduct");

        product = new Product();
        url = "http://uge-webservice.herokuapp.com/api/product/"+idProduct;
        //product.setAvailable(false);

        initUi();
        new AfficherProduitEmprunt.ShowProductTask().execute();

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star1);
                star2.setImageResource(R.drawable.star0);
                star3.setImageResource(R.drawable.star0);
                star4.setImageResource(R.drawable.star0);
                star5.setImageResource(R.drawable.star0);
                note =1;

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
                note =2;

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
                note =3;

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
                note =4;

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
                note =5;

            }
        });

        layout_saisirDate_DemandeEmprunt.setVisibility(View.GONE);
        layout_saisirDate_emprunt.setVisibility(View.GONE);

        button_emprunter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_boutton_emprunt.setVisibility(View.GONE);
                layout_saisirDate_emprunt.setVisibility(View.VISIBLE);

            }
        });

        button_demandeEmprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_saisirDate_DemandeEmprunt.setVisibility(View.VISIBLE);
                layout_demande_emprunt_boutton.setVisibility(View.GONE);
                layout_demande_emprunt_countdown.setVisibility(View.GONE);

            }
        });



        button_dateDebut_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_DATE_DEBUT_EMPRUNT;
                datePickerDebutEmprunt.show(getSupportFragmentManager(), "Datedebut");
            }
        });


        button_dateFin_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_DATE_FIN_EMPRUNT;
                datePickerFinEmprunt.show(getSupportFragmentManager(), "Datefin");
            }
        });

        button_heureDebut_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_TIME_DEBUT_EMPRUNT;
                timePickerDebutEmprunt.show(getSupportFragmentManager(), "Heuredebut");
            }
        });

        button_heureFin_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_TIME_FIN_EMPRUNT;
                timePickerFinEmprunt.show(getSupportFragmentManager(), "Heurefin");
            }
        });


        button_dateDebut_Demande_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_DATE_DEBUT_DEMANDE_EMPRUNT;
                datePickerDebutDemandeEmprunt.show(getSupportFragmentManager(), "Datedebut");
            }
        });


        button_dateFin_Demande_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_DATE_FIN_DEMANDE_EMPRUNT;
                datePickerFinDemandeEmprunt.show(getSupportFragmentManager(), "Datefin");
            }
        });

        button_heureDebut_Demande_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_TIME_DEBUT_DEMANDE_EMPRUNT;
                timePickerDebutDemandeEmprunt.show(getSupportFragmentManager(), "Heuredebut");
            }
        });

        button_heureFin_Demande_emprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TYPE_DIALOG_FRAGMENT=DIALOG_FRAGMENT_TIME_FIN_DEMANDE_EMPRUNT;
                timePickerFinDemandeEmprunt.show(getSupportFragmentManager(), "Heurefin");
            }
        });

        button_nouveauEmprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AfficherProduitEmprunt.AddBorrowTask().execute();
            }
        });

        button_nouvelleDemandeEmprunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AfficherProduitEmprunt.AddRequestBorrowTask().execute();
            }
        });

        button_ajoutercommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AfficherProduitEmprunt.AddCommentTask().execute();
            }
        });


    }

    private void initUi(){
        button_emprunter =(Button)findViewById(R.id.button_emprunter);
        button_demandeEmprunt =(Button)findViewById(R.id.button_demande_emprunt);
        button_nouveauEmprunt =(Button)findViewById(R.id.button_nouveau_emprunt);
        button_nouvelleDemandeEmprunt =(Button)findViewById(R.id.button_nouveau_Demande_emprunt);
        button_ajoutercommentaire=(Button)findViewById(R.id.button_ajoutercommentaire);
        button_dateDebut_emprunt=(Button)findViewById(R.id.button_dateDebut_emprunt);
        button_heureDebut_emprunt=(Button)findViewById(R.id.button_heureDebut_emprunt);
        button_dateFin_emprunt=(Button)findViewById(R.id.button_dateFin_emprunt);
        button_heureFin_emprunt=(Button)findViewById(R.id.button_heureFin_emprunt);
        button_dateDebut_Demande_emprunt=(Button)findViewById(R.id.button_dateDebut_Demande_emprunt);
        button_heureDebut_Demande_emprunt=(Button)findViewById(R.id.button_heureDebut_Demande_emprunt);
        button_dateFin_Demande_emprunt=(Button)findViewById(R.id.button_dateFin_Demande_emprunt);
        button_heureFin_Demande_emprunt=(Button)findViewById(R.id.button_heureFin_Demande_emprunt);

        Imageproduit_emprunt = (ImageView) findViewById(R.id.imageView_imageAfficherProduit_emprunt);
        imageView_ratingstar_emprunt = (ImageView) findViewById(R.id.imageView_ratingstar_emprunt);

        star1=(ImageButton)findViewById(R.id.imageButton_rating_1);
        star2=(ImageButton)findViewById(R.id.imageButton_rating_2);
        star3=(ImageButton)findViewById(R.id.imageButton_rating_3);
        star4 = (ImageButton) findViewById(R.id.imageButton_rating_4);
        star5=(ImageButton)findViewById(R.id.imageButton_rating_5);


        textView_categorie_type_emprunt=(TextView)findViewById(R.id.textView_categorie_type_emprunt);
        textView_nom_emprunt=(TextView)findViewById(R.id.textView_nom_emprunt);
        textView_description_produit_emprunt=(TextView)findViewById(R.id.textView_description_prpduit_emprunt);
        textView_etat_produit_emprunt=(TextView)findViewById(R.id.textView_etat_produit_emprunt);
        textView_jour = (TextView)findViewById(R.id.textView_countdown_jour);
        textView_heure = (TextView)findViewById(R.id.textView_countdown_heure);
        textView_minute = (TextView)findViewById(R.id.textView_countdown_minute);
        textView_seconde = (TextView)findViewById(R.id.textView_countdown_seconde);

        listView_listAvis = (RecyclerView)findViewById(R.id.listView_listAvis);
        editText_commentaire=(EditText)findViewById(R.id.editText_commentaire);


        layout_boutton_emprunt= findViewById(R.id.layout_boutton_emprunt);
        layout_saisirDate_emprunt= findViewById(R.id.layout_saisirDate_emprunt);
        layout_demande_emprunt_boutton= findViewById(R.id.layout_demande_emprunt_boutton);
        layout_saisirDate_DemandeEmprunt= findViewById(R.id.layout_saisirDate_Demande_emprunt);
        layout_demande_emprunt_countdown= findViewById(R.id.layout_demande_emprunt_countdown);
        linear_layout_jour = findViewById(R.id.LinearLayout_Jour);
        linear_layout_heure = findViewById(R.id.LinearLayout_Heure);


        datePickerDebutEmprunt = new DatePickerFragment();
        datePickerFinEmprunt = new DatePickerFragment();
        timePickerDebutEmprunt = new TimePickerFragment();
        timePickerFinEmprunt = new TimePickerFragment();
        datePickerDebutDemandeEmprunt = new DatePickerFragment();
        datePickerFinDemandeEmprunt = new DatePickerFragment();
        timePickerDebutDemandeEmprunt = new TimePickerFragment();
        timePickerFinDemandeEmprunt = new TimePickerFragment();

        adapterComment = new AdapterComment(product.getComments());
        listView_listAvis.setLayoutManager(new LinearLayoutManager(AfficherProduitEmprunt.this));
    }



    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(DATE_COUNTDOWN);
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

    public void changeLayoutEmprunt(){

        Log.e("Step1","change1");

        adapterComment.setResults(product.getComments());

        listView_listAvis.setAdapter(new AdapterComment(product.getComments()));
        Log.e("Step2","change2");
        //Log.e("Comments : ", product.getComments().toString());

        if(product.isAvailable()){
            layout_demande_emprunt_boutton.setVisibility(View.GONE);
            layout_demande_emprunt_countdown.setVisibility(View.GONE);

        }
        else {
            layout_boutton_emprunt.setVisibility(View.GONE);
            countDownStart();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        date = year+"-"+month+"-"+dayOfMonth;
        if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_DATE_DEBUT_EMPRUNT){
            button_dateDebut_emprunt.setText(date);
        }
        else if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_DATE_FIN_EMPRUNT){
            button_dateFin_emprunt.setText(date);
        }
        else if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_DATE_DEBUT_DEMANDE_EMPRUNT){
            button_dateDebut_Demande_emprunt.setText(date);
        }
        else if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_DATE_FIN_DEMANDE_EMPRUNT){
            button_dateFin_Demande_emprunt.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        heure=hourOfDay+":"+minute;
        if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_TIME_DEBUT_EMPRUNT){
            button_heureDebut_emprunt.setText(heure);
        }
        else if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_TIME_FIN_EMPRUNT){
            button_heureFin_emprunt.setText(heure);
        }
        else if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_TIME_DEBUT_DEMANDE_EMPRUNT){
            button_heureDebut_Demande_emprunt.setText(heure);
        }
        else if(TYPE_DIALOG_FRAGMENT==DIALOG_FRAGMENT_TIME_FIN_DEMANDE_EMPRUNT){
            button_heureFin_Demande_emprunt.setText(heure);
        }
    }


    public void setImageRatingStar(ImageView imageView,  int rate){
        switch(rate) {
            case 0:
                imageView.setImageResource(R.drawable.s0);
                break;
            case 5:
                imageView.setImageResource(R.drawable.s5);
                break;
            case 10:
                imageView.setImageResource(R.drawable.s10);
                break;
            case 15:
                imageView.setImageResource(R.drawable.s15);
                break;
            case 20:
                imageView.setImageResource(R.drawable.s20);
                break;
            case 25:
                imageView.setImageResource(R.drawable.s25);
                break;
            case 30:
                imageView.setImageResource(R.drawable.s30);
                break;
            case 35:
                imageView.setImageResource(R.drawable.s35);
                break;
            case 40:
                imageView.setImageResource(R.drawable.s40);
                break;
            case 45:
                imageView.setImageResource(R.drawable.s45);
                break;
            case 50:
                imageView.setImageResource(R.drawable.s50);
                break;
            default:
                imageView.setImageResource(R.drawable.s0);
        }
    }


    private class ShowProductTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherProduitEmprunt.this);
            pDialog.setMessage("Chargement du produit...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);



            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
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
                    avgRate = jsonObj.getInt("avgRate");

                    JSONArray arrayResult = jsonObj.getJSONArray("comments");


                    Log.e("Taille comments : " ,arrayResult.length()+"");
                    for (int i = 0; i < arrayResult.length(); i++) {

                        JSONObject a = arrayResult.getJSONObject(i);

                        Comment c = new Comment();
                        c.setId(a.getInt("id"));
                        c.setContent(a.getString("content"));
                        c.setRate(a.getInt("rate"));
                        c.setCreatedAt(a.getString("createdAt"));
                        JSONObject JsonUser = a.getJSONObject("user");
                        User u = new User();
                        u.setFirstName(JsonUser.getString("firstName"));
                        u.setLastName(JsonUser.getString("lastName"));
                        c.setUser(u);

                        Log.e("Commentaire json" , c.toJson());
                        Log.e("Commentaire json user" , u.getFirstName() +" "+ u.getLastName()+"Comment : "+c.getContent());
                        product.addComment(c);

                    }

                    if(product.isAvailable()){
                        //layout_demande_emprunt_boutton.setVisibility(View.GONE);
                        //layout_demande_emprunt_countdown.setVisibility(View.GONE);

                    }
                    else {

                        String url3 = "http://uge-webservice.herokuapp.com/api/borrow/borrowByProduct/"+product.getId();
                        HttpHandler shh = new HttpHandler();
                        jsonStr = shh.makeServiceCall(url3);
                        Log.e(TAG, "Response from url: " + jsonStr);

                        if (jsonStr != null) {
                            try {

                                JSONObject jsonObj2 = new JSONObject(jsonStr);
                                DATE_COUNTDOWN = jsonObj2.getString("endAt");

                            } catch (final JSONException e) {
                                Log.e(TAG, "Mohsine" + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "mohsine" + e.getMessage(),
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

                        //layout_boutton_emprunt.setVisibility(View.GONE);
                        //countDownStart();
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

            Picasso.get().load(product.getPath())
                    .error(R.drawable.erreurpicture)
                    .into(Imageproduit_emprunt);
            /*
            //Picasso.get().load(product.getPath())
                    .resize(150, 150)
                    .centerCrop()
                    .error(R.drawable.erreurpicture)
                    .into(Imageproduit_emprunt);
*/
            textView_categorie_type_emprunt.setText(product.getCategory()+" > "+ product.getType());
            textView_nom_emprunt.setText(product.getName());
            textView_description_produit_emprunt.setText(product.getDescription());
            textView_etat_produit_emprunt.setText(product.getState());
            setImageRatingStar(imageView_ratingstar_emprunt,avgRate);

            changeLayoutEmprunt();

        }

    }



    private class AddBorrowTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherProduitEmprunt.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpURLConnection urlConnection;
            String url2 = "http://uge-webservice.herokuapp.com/api/borrow/";
            Borrow borrow = new Borrow();
            String startAt = button_dateDebut_emprunt.getText() + " " +button_heureDebut_emprunt.getText();
            String endAt = button_dateFin_emprunt.getText() + " " +button_heureFin_emprunt.getText();
            borrow.setStartAt(startAt);
            borrow.setEndAt(endAt);
            borrow.setProduct(product.getId());

            String data = borrow.toJson();
            Log.e("Json", data);
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
            return null;
        }




        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(AfficherProduitEmprunt.this, "Produit bien emprunté", Toast.LENGTH_SHORT).show();

        }

    }


    private class AddRequestBorrowTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherProduitEmprunt.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpURLConnection urlConnection;
            String url2 = "http://uge-webservice.herokuapp.com/api/requestBorrow/";
            RequestBorrow requestBorrow = new RequestBorrow();

            String startAt = button_dateDebut_Demande_emprunt.getText() + " " +button_heureDebut_Demande_emprunt.getText();
            String endAt = button_dateFin_Demande_emprunt.getText() + " " +button_heureFin_Demande_emprunt.getText();
            requestBorrow.setStartAt(startAt);
            requestBorrow.setEndAt(endAt);
            requestBorrow.setProduct(product.getId());

            String data = requestBorrow.toJson();
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
                Log.e("Erreur Requestborrow", e.getMessage());
            }
            return null;
        }




        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(AfficherProduitEmprunt.this, "Demande bien enregistré", Toast.LENGTH_SHORT).show();

        }

    }


    private class AddCommentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AfficherProduitEmprunt.this);
            pDialog.setMessage("Traitement en cours...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpURLConnection urlConnection;
            String url2 = "http://uge-webservice.herokuapp.com/api/comment/";
            Comment comment = new Comment();
            comment.setRate(note);
            comment.setContent(editText_commentaire.getText().toString());
            comment.setProduct(product.getId());
            comment.setUser(product.getUser());
            product.addComment(comment);
            String data = comment.toJson();
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
            return null;
        }




        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(AfficherProduitEmprunt.this, "Commentaire bien ajouté", Toast.LENGTH_SHORT).show();
            editText_commentaire.setText("");


            updateComments();
        }

    }

    void updateComments(){
        new AfficherProduitEmprunt.ShowProductTask().execute();
    }


}
