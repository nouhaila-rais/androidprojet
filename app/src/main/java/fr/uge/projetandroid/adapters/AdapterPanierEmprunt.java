package fr.uge.projetandroid.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.uge.projetandroid.borrow.AfficherProduitEmprunt;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Borrow;
import fr.uge.projetandroid.entities.Product;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.messages.ProduitRetourne;

public class AdapterPanierEmprunt extends RecyclerView.Adapter<AdapterPanierEmprunt.ViewHolder> {

    private List<Product> results;
    private List<Borrow> borrows;
    private int itemSelected = 0;
    private long idProduct;
    private String[] etats;
    private Context context;
    private User user;

    public AdapterPanierEmprunt(List<Product> results, List<Borrow> borrows, User user) {
        this.results = results;
        this.borrows = borrows;
        this.user = user;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_produits_panier_emprunt, viewGroup, false));
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

        private TextView textView_nomProduit_panier_emprunt;
        private TextView textView_debutEmprunt_panier_emprunt;
        private TextView textView_finEmprunt_panier_emprunt;
        private ImageView imageView_imageProduit_panier_emprunt;
        private Button button_retouner_panier_emprunt ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_nomProduit_panier_emprunt = itemView.findViewById(R.id.textView_nomProduit_panier_emprunt);
            textView_debutEmprunt_panier_emprunt = itemView.findViewById(R.id.textView_debutEmprunt_panier_emprunt);
            textView_finEmprunt_panier_emprunt = itemView.findViewById(R.id.textView_finEmprunt_panier_emprunt);
            imageView_imageProduit_panier_emprunt = itemView.findViewById(R.id.imageView_imageProduit_panier_emprunt);
            button_retouner_panier_emprunt = itemView.findViewById(R.id.button_retouner_panier_emprunt);
        }

        public void update(final Product entity){

            Borrow borrow = getBorrowByProduct(entity.getId());
            if(borrow!=null){
                Log.e("NomProduit",entity.getName());
                Log.e("getStartAt",borrow.getStartAt());
                Log.e("getEndAt",borrow.getEndAt());
                textView_nomProduit_panier_emprunt.setText(entity.getName());
                textView_debutEmprunt_panier_emprunt.setText(borrow.getStartAt());
                textView_finEmprunt_panier_emprunt.setText(borrow.getEndAt());


                Picasso.get().load(entity.getPath())
                        .resize(150, 150)
                        .centerCrop()
                        .error(R.drawable.erreurpicture)
                        .into(imageView_imageProduit_panier_emprunt);

                imageView_imageProduit_panier_emprunt.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent myIntent = new Intent(v.getContext(), AfficherProduitEmprunt.class);
                        myIntent.putExtra("idProduct",entity.getId());
                        myIntent.putExtra("user",user);
                        v.getContext().startActivity(myIntent);
                    }
                });

                textView_nomProduit_panier_emprunt.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent myIntent = new Intent(v.getContext(), AfficherProduitEmprunt.class);
                        myIntent.putExtra("idProduct",entity.getId());
                        myIntent.putExtra("user",user);
                        v.getContext().startActivity(myIntent);
                    }
                });

                button_retouner_panier_emprunt.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        context=v.getContext();
                        idProduct=entity.getId();
                        showDialog();
                    }
                });
            }


        }
    }

    public void setImageRatingStar(ImageView imageView,  int rate){
        switch(rate) {
            case 0:
                imageView.setImageResource(R.drawable.star_0);
                break;
            case 5:
                imageView.setImageResource(R.drawable.star_5);
                break;
            case 10:
                imageView.setImageResource(R.drawable.star_10);
                break;
            case 15:
                imageView.setImageResource(R.drawable.star_15);
                break;
            case 20:
                imageView.setImageResource(R.drawable.star_20);
                break;
            case 25:
                imageView.setImageResource(R.drawable.star_25);
                break;
            case 30:
                imageView.setImageResource(R.drawable.star_30);
                break;
            case 35:
                imageView.setImageResource(R.drawable.star_35);
                break;
            case 40:
                imageView.setImageResource(R.drawable.star_40);
                break;
            case 45:
                imageView.setImageResource(R.drawable.star_45);
                break;
            case 50:
                imageView.setImageResource(R.drawable.star_50);
                break;
            default:
                imageView.setImageResource(R.drawable.star_0);
        }
    }

    public Borrow getBorrowByProduct(long idProduct){
        for(Borrow borrow: borrows ){
            if(borrow.getProduct()==idProduct) return borrow;
        }
        return null;
    }


    public void showDialog() {
        String[] singleChoiceItems = context.getResources().getStringArray(R.array.etat_produit_array);
        etats = singleChoiceItems;
        new AlertDialog.Builder(context)
                .setTitle("Veuillez choisir l'état du produit actuel")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        itemSelected=selectedIndex;
                        Log.e("Etat  ",selectedIndex+"");

                    }

                })
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Etat 2 ",etats[itemSelected]);
                        Intent myIntent = new Intent(context, ProduitRetourne.class);
                        myIntent.putExtra("idProduct",idProduct);
                        myIntent.putExtra("etat",etats[itemSelected]);
                        myIntent.putExtra("user",user);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }



}