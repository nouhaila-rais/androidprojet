package fr.uge.projetandroid;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fr.uge.projetandroid.borrow.Accueil_Emprunt;
import fr.uge.projetandroid.entities.User;
import fr.uge.projetandroid.fingerPrintDatabase.DatabaseFingerPrint;
import fr.uge.projetandroid.fingerPrintDatabase.UserFingerPrint;
import fr.uge.projetandroid.handlers.HttpHandler;
import fr.uge.projetandroid.shopping.AccueilAchat;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private DatabaseFingerPrint db;
    private List<UserFingerPrint> userFingerPrints = new ArrayList<>();

    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private KeyStore keyStore;
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    private TextView textView_erreur_fingerprint;
    private boolean authentifiedFingerPrint=false;
    private int idUser =-1;
    private User user;

    private FingerprintHandler helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        db = new DatabaseFingerPrint(this);
        userFingerPrints.addAll(db.getAllUsersFingerPrint());



        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        textView_erreur_fingerprint = (TextView) findViewById(R.id.textView_erreur_fingerprint);

        if(!fingerprintManager.isHardwareDetected()){

            textView_erreur_fingerprint.setText("Votre appareil n'a pas de capteur d'empreintes digitales");
        }else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView_erreur_fingerprint.setText("L'autorisation d'authentification par empreinte digitale n'est pas activée");
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    textView_erreur_fingerprint.setText("Enregistrer au moins une empreinte digitale dans les paramètres");
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        textView_erreur_fingerprint.setText("La sécurité de l'écran de verrouillage n'est pas activée dans les paramètres");
                    }else{
                        generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            helper = new FingerprintHandler(this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }
    }




    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }








    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    public void attemptLogin() {


        if (mAuthTask != null) {
            return;
        }



        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email;
        String password;


        boolean cancel = false;
        View focusView = null;

        if(authentifiedFingerPrint){
            email = "";
            password = "";
        }
        else {

            email = mEmailView.getText().toString();
            password = mPasswordView.getText().toString();

            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    public class UserLoginTask extends AsyncTask<Void, Void, User> {

        private final String mEmail;
        private final String mPassword;
        private String devise;
        private String role;




        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected User doInBackground(Void... params) {

            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return null;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    if(!pieces[1].equals(mPassword)) return null;
                }
            }

            User user = new User(mEmail,mPassword);
            HttpURLConnection urlConnection;
            String url2 = "http://uge-webservice.herokuapp.com/api/login";
            String data = user.EmailPasswordToJson();
            String result = null;
            try {
                if(!authentifiedFingerPrint){
                    urlConnection = (HttpURLConnection) ((new URL(url2).openConnection()));
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.connect();

                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(data);
                    writer.close();
                    outputStream.close();


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    result = sb.toString();
                    Log.e("Login Json", data);
                    Log.e("Login Message retour", "resultat : " + result);
                    idUser =Integer.parseInt(result);
                }
                if(idUser >0) {

                    long id = db.insertUser(idUser);
                    if(id==-1){
                        Log.e("FingerPrint",  "erreur");
                    }
                    else {
                        Log.e("FingerPrint", "Bien ajouté");
                    }

                    String url = "http://uge-webservice.herokuapp.com/api/user/"+idUser;
                    HttpHandler sh = new HttpHandler();
                    String jsonStr = sh.makeServiceCall(url);



                    Log.e("Login", "Response from url: " + jsonStr);

                    if (jsonStr != null) {
                        try {

                            user = new User();
                            JSONObject jsonObj = new JSONObject(jsonStr);
                            user.setId(jsonObj.getLong("Id"));
                            user.setEmail(jsonObj.getString("Email"));
                            user.setPassword(jsonObj.getString("Password"));
                            user.setFirstName(jsonObj.getString("FirstName"));
                            user.setLastName(jsonObj.getString("LastName"));
                            user.setTotalNotification(jsonObj.getInt("TotalNotification"));
                            user.setTotalPanier(jsonObj.getInt("TotalCart"));
                            user.setTotalProduitEmprunte(jsonObj.getInt("TotalBorrow"));
                            //user.setTotalWishlist(jsonObj.getInt("totalWishlist"));
                            user.setTotalWishlist(7);
                            role=jsonObj.getString("role");
                            user.setRole(role);
                            devise="EUR";

                            Log.e("User",user.toString());

                        } catch (final JSONException e) {
                            Log.e("Login", "Json parsing error: " + e.getMessage());

                        }
                    } else {
                        Log.e("Login", "Couldn't get json from server.");

                    }
                }


            } catch (Exception e) {
                Log.e("Login Erreur", e.getMessage());
            }

            if(idUser >=0) return user;
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            mAuthTask = null;
            showProgress(false);

            if (user!=null) {
                if(role.equals("Customer")){
                    Intent intent = new Intent(LoginActivity.this, AccueilAchat.class);
                    intent.putExtra("devise",devise);
                    intent.putExtra("user",user);
                    LoginActivity.this.startActivity(intent);
                    Log.e("UserEmprunt",user.toString());
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, Accueil_Emprunt.class);
                    intent.putExtra("user",user);
                    LoginActivity.this.startActivity(intent);
                    Log.e("UserEmprunt",user.toString());
                }
                finish();
            } else {
                if(idUser==-1){
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
                else if(idUser==-2){
                    Intent intent = new Intent(LoginActivity.this, InscriptionActivity.class);
                    intent.putExtra("email",mEmail);
                    LoginActivity.this.startActivity(intent);
                }

            }


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private Context context;
        private ImageView imageView;
        private TextView textView;

        public FingerprintHandler(Context mContext) {
            context = mContext;
            imageView = (ImageView) ((Activity) context).findViewById(R.id.imageView_fingerprint);
            textView = (TextView) ((Activity) context).findViewById(R.id.textView_erreur_fingerprint);
        }

        public Boolean startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
            return authentifiedFingerPrint;
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
            if(userFingerPrints.size()==0){
                this.update("C’est votre première connexion, vous devez vous authentifier avec votre email et votre mot de passe");

            }
            else {
                imageView.setImageResource(R.drawable.fingerprint_vert);
                authentifiedFingerPrint = true;
                idUser=userFingerPrints.get(0).getUser();
                textView.setText("");
                attemptLogin();
            }


        }

        private void update(String e) {
            imageView.setImageResource(R.drawable.fingerprint_rouge);
            textView = (TextView) ((Activity) context).findViewById(R.id.textView_erreur_fingerprint);
            textView.setText(e);
        }


    }
}

