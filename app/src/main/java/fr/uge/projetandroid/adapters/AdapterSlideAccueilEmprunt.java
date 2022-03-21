package fr.uge.projetandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.borrow.AfficherProduitsRechercheEmprunt;
import fr.uge.projetandroid.entities.User;

public class AdapterSlideAccueilEmprunt extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int current;
    private User user;

    public AdapterSlideAccueilEmprunt(Context context, User user) {
        this.context = context;
        this.user = user;
    }




    //Array
    public int[] list_images = {

            R.drawable.biblio,
            R.drawable.electronique,
            R.drawable.vetement,
            R.drawable.musique,
            R.drawable.accesoire,
            R.drawable.autre
    };

    public String[] list_title = {

            "Bibliotheque",
            "Electronique",
            "Mode et vetements",
            "Musique",
            "Accesoires",
            "Autres"
    };

    public String[] list_description = {

            "Roman, Programmation, Cuisine, Proverbe",
            "Ordinateur, Telephone, Tablette, Cle USB, Cable",
            "Chemise, Pull, Pantalon, Robe",
            "Guitare, Piano, Percussion",
            "Montre, Sac, Collier",
            "Meuble, Jeu Video, Film"
    };
    public int[] list_color = {

            Color.parseColor("#721b65"),
            Color.parseColor("#543864"),
            Color.rgb(247, 52, 122),
            Color.rgb(87, 56, 247),
            Color.parseColor("#d7385e"),
            Color.rgb(128, 0, 128)

    };

    @Override
    public int getCount() {
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == (LinearLayout) obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_emprunt, container, false);

        LinearLayout linearlayout_slide_emprunt = (LinearLayout) view.findViewById(R.id.linearlayout_slide_emprunt);
        ImageView imageView_slide_emprunt = (ImageView) view.findViewById(R.id.imageView_slide_emprunt);
        TextView textView_slidetitle_emprunt = (TextView) view.findViewById(R.id.textView_slidetitle_emprunt);
        TextView textView_slidedescription_emprunt = (TextView) view.findViewById(R.id.textView_slidedescription_emprunt);


        imageView_slide_emprunt.setImageResource(list_images[position]);
        textView_slidetitle_emprunt.setText(list_title[position]);
        textView_slidedescription_emprunt.setText(list_description[position]);
        linearlayout_slide_emprunt.setBackgroundColor(list_color[position]);

        linearlayout_slide_emprunt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(v.getContext(), AfficherProduitsRechercheEmprunt.class);
                myIntent.putExtra("Keyword",list_title[getCurrent()]);
                myIntent.putExtra("user",user);
                v.getContext().startActivity(myIntent);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
