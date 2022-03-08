package fr.uge.projetandroid.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Comment;

public class AdapterComment extends BaseAdapter {

    private Vector<Comment> data;

    public AdapterComment(Vector<Comment> data) {
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentaire,null);

        TextView textView_nom_utilisateur_commentaire=item_view.findViewById(R.id.textView_nom_utilisateur_commentaire);
        TextView textView_commentaire =item_view.findViewById(R.id.textView_commentaire);
        TextView textView_date_commentaire  =item_view.findViewById(R.id.textView_date_commentaire);
        ImageView imageView_ratingstar_comment  =item_view.findViewById(R.id.imageView_ratingstar_comment);


        String utilisateur = data.get(position).getUser().getFirstName()  +" "+data.get(position).getUser().getLastName();
        textView_nom_utilisateur_commentaire.setText(utilisateur);
        textView_commentaire.setText(data.get(position).getContent());
        textView_date_commentaire.setText(data.get(position).getCreatedAt());
        setImageRatingStar(imageView_ratingstar_comment,data.get(position).getRate());
        return item_view;
    }

    public void update(Vector<Comment> data){
        this.data=data;
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
