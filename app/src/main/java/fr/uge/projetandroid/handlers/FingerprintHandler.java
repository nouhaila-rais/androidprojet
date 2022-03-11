package fr.uge.projetandroid.handlers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;

import fr.uge.projetandroid.MainActivity;
import fr.uge.projetandroid.R;
import fr.uge.projetandroid.borrow.AccueilEmprunt;


public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private ImageView imageView;

    public FingerprintHandler(Context mContext) {
        context = mContext;
        imageView = (ImageView) ((Activity)context).findViewById(R.id.imageView_fingerprint);
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Erreur d'authentification par empreinte digitale\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Aide sur l'authentification par empreinte digitale\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("L'authentification par empreinte digitale a échoué");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        imageView.setImageResource(R.drawable.ic_action_fingerprint_vert);
        ((Activity) context).finish();
        Intent intent = new Intent(context, AccueilEmprunt.class);
        context.startActivity(intent);
    }

    private void update(String e){
        imageView.setImageResource(R.drawable.ic_action_fingerprint_rouge);
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.textView_erreur_fingerprint);
        textView.setText(e);
    }

}
