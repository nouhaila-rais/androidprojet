package fr.uge.projetandroid;


import static android.content.ContentValues.TAG;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import fr.uge.projetandroid.fingerPrintDatabase.DatabaseFingerPrint;
import fr.uge.projetandroid.fingerPrintDatabase.DatabaseNotification;
import fr.uge.projetandroid.handlers.HttpHandler;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private final static int INTERVAL = 1000 * 10; //2 minutes
    private NotificationManagerCompat notificationManager;
    private Context context = this;
    Handler handler = new Handler();
    Runnable notificationChecker = new Runnable() {
        @Override
        public void run() {
            new ShowNotificationsTask(context).execute();
            handler.postDelayed(notificationChecker, INTERVAL);
        }
    };

    void createNotification(String title, String message) {
        Intent activityIntent = new Intent(this, App.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.launcher_background);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();

        notificationManager.notify(new Random().nextInt(61) + 20, notification);
    }

    void startRepeatingTask() {
        notificationChecker.run();
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(notificationChecker);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
        startRepeatingTask();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        startRepeatingTask();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }


    private class ShowNotificationsTask extends AsyncTask<Void, Void, Void> {


        DatabaseNotification db;
        DatabaseFingerPrint dbfp;

        public ShowNotificationsTask(Context context) {
            db = new DatabaseNotification(context);
            dbfp = new DatabaseFingerPrint(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int count = dbfp.getUserFingerPrintCount();
            if (count > 0) {
                String url = "http://uge-webservice.herokuapp.com/api/user/"+dbfp.getAllUsersFingerPrint().get(count - 1);
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall(url);
                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {

                        JSONObject json = new JSONObject(jsonStr);
                        JSONArray arrayResult = json.getJSONArray("notifications");
                        for (int i = 0; i < arrayResult.length(); i++) {
                            JSONObject jsonObj = arrayResult.getJSONObject(i);
                            if (!jsonObj.getBoolean("readNotification")) {
                                fr.uge.projetandroid.entities.Notification notification = new fr.uge.projetandroid.entities.Notification();
                                notification.setId(jsonObj.getLong("id"));
                                notification.setMessage(jsonObj.getString("message"));
                                notification.setProduct(jsonObj.getInt("product"));
                                notification.setImage(jsonObj.getString("image"));
                                notification.setCreatedAt(jsonObj.getString("createdAt"));
                                notification.setRead(jsonObj.getBoolean("readNotification"));
                                if (db.getNotif(notification.getId()) == null) {
                                    createNotification(notification.getCreatedAt(), notification.getMessage());
                                    db.insertNotif(notification);
                                }
                            }
                        }


                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                }
            }
                return null;
            }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}