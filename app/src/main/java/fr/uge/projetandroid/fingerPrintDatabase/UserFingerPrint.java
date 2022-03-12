package fr.uge.projetandroid.fingerPrintDatabase;

public class UserFingerPrint {
    public static final String TABLE_NAME = "userFingerPrint";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER= "user";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private int user;
    private String timestamp;


    public UserFingerPrint() {
    }

    public UserFingerPrint(int id, int user, String timestamp) {
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
