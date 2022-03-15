package fr.uge.projetandroid.fingerPrintDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.uge.projetandroid.entities.Notification;

public class DatabaseNotification extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notification";
    public static final String TABLE_NAME = "notif";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE = "message";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_MESSAGE + " VARCHAR(200)"
                    + ")";

    public DatabaseNotification(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNotif(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, notification.getMessage());
        values.put(COLUMN_ID,notification.getId());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Notification getNotif(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_MESSAGE},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        try{
            Notification  notif = new Notification();
            notif.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            notif.setMessage(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE)));
        cursor.close();
        return notif;
        }catch(Exception e){
            cursor.close();
            return null;
        }

    }
}
