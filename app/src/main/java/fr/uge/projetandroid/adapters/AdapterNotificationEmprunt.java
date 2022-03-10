package fr.uge.projetandroid.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.uge.projetandroid.R;
import fr.uge.projetandroid.entities.Notification;

public class AdapterNotificationEmprunt extends RecyclerView.Adapter<AdapterNotificationEmprunt.ViewHolder> {

    private List<Notification> results;


    public AdapterNotificationEmprunt(List<Notification> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification_emprunt, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(results.get(i));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Notification> results) {
        this.results = results;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_message_notifications_emprunt;
        private TextView textView_date_notifications_emprunt;
        private ImageView imageView_imageProduit_notifications_emprunt;
        private Button button_emprunter_notifications_emprunt ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_message_notifications_emprunt = itemView.findViewById(R.id.textView_message_notifications_emprunt);
            textView_date_notifications_emprunt = itemView.findViewById(R.id.textView_date_notifications_emprunt);
            imageView_imageProduit_notifications_emprunt = itemView.findViewById(R.id.imageView_imageProduit_notifications_emprunt);
            button_emprunter_notifications_emprunt = itemView.findViewById(R.id.button_emprunter_notifications_emprunt);
        }

        public void update(final Notification entity){


            textView_message_notifications_emprunt.setText(entity.getMessage());
            textView_date_notifications_emprunt.setText(entity.getCreatedAt());



            Picasso.get().load(entity.getImage())
                    .resize(150, 150)
                    .centerCrop()
                    .error(R.drawable.erreurpicture)
                    .into(imageView_imageProduit_notifications_emprunt);

            imageView_imageProduit_notifications_emprunt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherMesProduitsEmprunte.class);
                    myIntent.putExtra("idProduct",entity.getProduct()+"");
                    v.getContext().startActivity(myIntent);
                }
            });

            textView_message_notifications_emprunt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherMesProduitsEmprunte.class);
                    myIntent.putExtra("idProduct",entity.getProduct()+"");
                    v.getContext().startActivity(myIntent);
                }
            });

            button_emprunter_notifications_emprunt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent myIntent = new Intent(v.getContext(), AfficherMesProduitsEmprunte.class);
                    myIntent.putExtra("idProduct",entity.getProduct()+"");
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
