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

import java.text.DecimalFormat;
import java.util.List;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Product;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.shopping.AfficherProduitAchat;

public class AdapterPanierAchat extends RecyclerView.Adapter<AdapterPanierAchat.ViewHolder> {

    private List<Product> results;
    private User user;
    private String devise;
    private Double rate;


    public AdapterPanierAchat(List<Product> results, User user, String devise, Double rate) {
        this.results = results;
        this.user = user;
        this.devise = devise;
        this.rate = rate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_produit_panier_achat, viewGroup, false));
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

        private TextView textView_nomProduit_panier_achat;
        private TextView textView_typeCategorie_panier_achat;
        private TextView textView_prix_panier_achat;
        private ImageView imageView_imageProduit_panier_achat;
        private ImageView imageView_ratingStar_panier_achat;
        private LinearLayout LinearLayout_produit_panier_achat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_nomProduit_panier_achat = itemView.findViewById(R.id.textView_nomProduit_panier_achat);
            textView_typeCategorie_panier_achat = itemView.findViewById(R.id.textView_typeCategorie_panier_achat);
            textView_prix_panier_achat = itemView.findViewById(R.id.textView_prix_panier_achat);
            imageView_imageProduit_panier_achat = itemView.findViewById(R.id.imageView_imageProduit_panier_achat);
            imageView_ratingStar_panier_achat = itemView.findViewById(R.id.imageView_ratingStar_panier_achat);
            LinearLayout_produit_panier_achat = itemView.findViewById(R.id.LinearLayout_produit_panier_achat);
        }

        public void update(final Product entity){

            textView_nomProduit_panier_achat.setText(entity.getName());
            textView_typeCategorie_panier_achat.setText(entity.getCategory()+" > "+entity.getType());
            textView_prix_panier_achat.setText(getPriceProduct(entity.getPrice()));
            setImageRatingStar(imageView_ratingStar_panier_achat, entity.getRate());

            Picasso.get().load(entity.getPath())
                    .resize(150, 150)
                    .centerCrop()
                    .error(R.drawable.erreurpicture)
                    .into(imageView_imageProduit_panier_achat);



            LinearLayout_produit_panier_achat.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherProduitAchat.class);
                    myIntent.putExtra("idProduct",entity.getId());
                    myIntent.putExtra("user",user);
                    myIntent.putExtra("devise",devise);
                    myIntent.putExtra("rate",rate);
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

    public String getPriceProduct(Double price){
        Double prix = price*rate;
        DecimalFormat df = new DecimalFormat("0.00");
        String result  = df.format(prix)+" " +devise;
        return result ;
    }

}
