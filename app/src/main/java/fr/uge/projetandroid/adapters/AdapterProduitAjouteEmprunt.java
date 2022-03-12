package fr.uge.projetandroid.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Product;

public class AdapterProduitAjouteEmprunt extends RecyclerView.Adapter<AdapterProduitAjouteEmprunt.ViewHolder> {

    private List<Product> results;


    public AdapterProduitAjouteEmprunt(List<Product> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_produit_ajouter_emprunt, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(results.get(i));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_nomProduit_mesProduits_emprunt;
        private TextView textView_typeCategorie_mesProduits_emprunt;
        private TextView textView_etat_mesProduits_emprunt;
        private TextView textView_dateAjout_mesProduits_emprunt;
        private ImageView imageView_imageProduit_mesProduits_emprunt;
        private ImageView imageView_ratingStar__mesProduits_emprunt;
        private LinearLayout LinearLayout_produit_ajoute_emprunt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_nomProduit_mesProduits_emprunt = itemView.findViewById(R.id.textView_nomProduit_mesProduits_emprunt);
            textView_typeCategorie_mesProduits_emprunt = itemView.findViewById(R.id.textView_typeCategorie_mesProduits_emprunt);
            textView_etat_mesProduits_emprunt = itemView.findViewById(R.id.textView_etat_mesProduits_emprunt);
            textView_dateAjout_mesProduits_emprunt = itemView.findViewById(R.id.textView_dateAjout_mesProduits_emprunt);
            imageView_imageProduit_mesProduits_emprunt = itemView.findViewById(R.id.imageView_imageProduit_mesProduits_emprunt);
            imageView_ratingStar__mesProduits_emprunt = itemView.findViewById(R.id.imageView_ratingStar__mesProduits_emprunt);
            LinearLayout_produit_ajoute_emprunt = itemView.findViewById(R.id.LinearLayout_produit_ajoute_emprunt);
        }

        public void update(final Product entity){

            textView_nomProduit_mesProduits_emprunt.setText(entity.getName());
            textView_typeCategorie_mesProduits_emprunt.setText(entity.getCategory()+" > "+entity.getType());
            textView_etat_mesProduits_emprunt.setText(entity.getState());
            textView_dateAjout_mesProduits_emprunt.setText(entity.getCreatedAt());
            setImageRatingStar(imageView_ratingStar__mesProduits_emprunt, entity.getRate());

            Picasso.get().load(entity.getPath())
                    .resize(150, 150)
                    .centerCrop()
                    .error(R.drawable.erreurpicture)
                    .into(imageView_imageProduit_mesProduits_emprunt);



            LinearLayout_produit_ajoute_emprunt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherProduitEmprunt.class);
                    myIntent.putExtra("idProduct",entity.getId());
                    v.getContext().startActivity(myIntent);
                }
            });

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



}
