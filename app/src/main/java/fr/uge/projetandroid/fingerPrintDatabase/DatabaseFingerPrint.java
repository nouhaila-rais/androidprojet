package fr.uge.projetandroid.fingerPrintDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseFingerPrint extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "userFingerPrint";


    public DatabaseFingerPrint(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserFingerPrint.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserFingerPrint.TABLE_NAME);
        onCreate(db);
    }

    public long insertUser(int user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserFingerPrint.COLUMN_USER, user);
        long id = db.insert(UserFingerPrint.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public UserFingerPrint getUserFingerPrint(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserFingerPrint.TABLE_NAME,
                new String[]{UserFingerPrint.COLUMN_ID, UserFingerPrint.COLUMN_USER, UserFingerPrint.COLUMN_TIMESTAMP},
                UserFingerPrint.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        UserFingerPrint userFingerPrint = new UserFingerPrint(
                cursor.getInt(cursor.getColumnIndex(UserFingerPrint.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(UserFingerPrint.COLUMN_USER)),
                cursor.getString(cursor.getColumnIndex(UserFingerPrint.COLUMN_TIMESTAMP)));

        cursor.close();

        return userFingerPrint;
    }

    public List<UserFingerPrint> getAllUsersFingerPrint() {
        List<UserFingerPrint> userFingerPrints = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + UserFingerPrint.TABLE_NAME + " ORDER BY " +
                UserFingerPrint.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserFingerPrint userFingerPrint = new UserFingerPrint();
                userFingerPrint.setId(cursor.getInt(cursor.getColumnIndex(UserFingerPrint.COLUMN_ID)));
                userFingerPrint.setUser(cursor.getInt(cursor.getColumnIndex(UserFingerPrint.COLUMN_USER)));
                userFingerPrint.setTimestamp(cursor.getString(cursor.getColumnIndex(UserFingerPrint.COLUMN_TIMESTAMP)));

                userFingerPrints.add(userFingerPrint);
            } while (cursor.moveToNext());
        }

        db.close();

        return userFingerPrints;
    }

    public int getUserFingerPrintCount() {
        String countQuery = "SELECT  * FROM " + UserFingerPrint.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateUserFingerPrint(UserFingerPrint userFingerPrint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserFingerPrint.COLUMN_USER, userFingerPrint.getUser());

        // updating row
        return db.update(UserFingerPrint.TABLE_NAME, values, UserFingerPrint.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userFingerPrint.getId())});
    }

    public void deleteUserFingerPrint(UserFingerPrint userFingerPrint) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserFingerPrint.TABLE_NAME, UserFingerPrint.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userFingerPrint.getId())});
        db.close();
    }
}
