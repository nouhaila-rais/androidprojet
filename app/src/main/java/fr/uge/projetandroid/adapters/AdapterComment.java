package fr.uge.projetandroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Comment;

public class AdapterComment  extends RecyclerView.Adapter<AdapterComment.ViewHolder> {

    private List<Comment> results;
    private Map<String,Comment>  oldResult;

    public AdapterComment(List<Comment> results) {
        this.results = results;
        this.oldResult = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_commentaire, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(results.get(i));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_nom_utilisateur_commentaire;
        private TextView textView_commentaire;
        private TextView textView_date_commentaire;
        private ImageView imageView_ratingstar_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             textView_nom_utilisateur_commentaire = itemView.findViewById(R.id.textView_nom_utilisateur_commentaire);
             textView_commentaire = itemView.findViewById(R.id.textView_commentaire);
             textView_date_commentaire = itemView.findViewById(R.id.textView_date_commentaire);
             imageView_ratingstar_comment = itemView.findViewById(R.id.imageView_ratingstar_comment);
        }

        public void update(Comment entity){


                    String utilisateur = entity.getFirstName() + " " + entity.getLastName();

                    //String utilisateur = entity.getUser().getFirstName() + " " + entity.getUser().getLastName();

                    textView_nom_utilisateur_commentaire.setText(utilisateur);
                    textView_commentaire.setText(entity.getContent());
                    textView_date_commentaire.setText(entity.getCreatedAt());
                    setImageRatingStar(imageView_ratingstar_comment, entity.getRate());
                    Comment c = entity;
                    Log.e("mohsine", "Rate :" + c.getRate() + " / " + " user : " + utilisateur + " content : " + c.getContent());
                    //oldResult.put(entity.getCreatedAt() ,entity);




        }
    }

    public void setImageRatingStar(ImageView imageView,  int note){
        int rate = ConvertRate(note);
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


    int ConvertRate(int rate) {
        int rate10 = rate*10;
        int intRate = rate*10;

        int intRate10 = (int)rate10;
        int diff = intRate10 - intRate;

        if(diff==5) return intRate10;
        else if(diff<5) return intRate;
        else return intRate+5;
    }
}
