package fr.uge.projetandroid.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.borrow.AfficherProduitEmprunt;
import fr.uge.projetandroid.entities.Product;

public class AdapterProduitsRechercheEmprunt  extends RecyclerView.Adapter<AdapterProduitsRechercheEmprunt.ViewHolder> {

    private List<Product> results;


    public AdapterProduitsRechercheEmprunt(List<Product> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_produit_recherche_emprunt, viewGroup, false));
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

        private TextView textView_nomProduit_recherche_emprunt;
        private TextView textView_etatProduit_recherche_emprunt;
        private ImageView imageView_ratingStar_recherche_emprunt;
        private ImageView imageView_image_recherche_emprunt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_nomProduit_recherche_emprunt = itemView.findViewById(R.id.textView_nomProduit_recherche_emprunt);
            textView_etatProduit_recherche_emprunt = itemView.findViewById(R.id.textView_etatProduit_recherche_emprunt);
            imageView_ratingStar_recherche_emprunt = itemView.findViewById(R.id.imageView_ratingStar_recherche_emprunt);
            imageView_image_recherche_emprunt = itemView.findViewById(R.id.imageView_image_recherche_emprunt);
        }

        public void update(final Product entity){

            textView_nomProduit_recherche_emprunt.setText(entity.getName());
            textView_etatProduit_recherche_emprunt.setText(entity.getState());

            setImageRatingStar(imageView_ratingStar_recherche_emprunt, entity.getRate());

            Picasso.get().load(entity.getPath())
                    .resize(600, 600)
                    .centerCrop()
                    .error(R.drawable.erreurpicture)
                    .into(imageView_image_recherche_emprunt);

            imageView_image_recherche_emprunt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherProduitEmprunt.class);
                    myIntent.putExtra("idProduct",entity.getId()+"");
                    v.getContext().startActivity(myIntent);
                }
            });

            textView_nomProduit_recherche_emprunt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherProduitEmprunt.class);
                    myIntent.putExtra("idProduct",entity.getId()+"");
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
