package nl.lennartklein.lennartkleinpset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TodoDatabase extends SQLiteOpenHelper {

    // Singleton holder
    private static TodoDatabase instance;

    // Names
    private static int DB_VERSION = 1;
    private static String NAME_DB = "todos";
    private static String NAME_TABLE_TODOS = "todos";
    private static String NAME_ID = "_id";
    private static String NAME_TODO = "title";
    private static String NAME_STATUS = "completed";

    // SQL statements
    private static String CREATE_TABLE_TODOS = "CREATE TABLE " + NAME_TABLE_TODOS
            + "(" + NAME_ID + " INTEGER PRIMARY KEY,"
                  + NAME_TODO + " TEXT,"
                  + NAME_STATUS + " INTEGER"
            + ")";
    private static String SELECT_ALL = "SELECT  * FROM " + NAME_TABLE_TODOS;

    Context mContext;

    // Constructor
    private TodoDatabase(Context context) {
        super(context, NAME_DB, null, DB_VERSION);
        mContext = context;
    }

    // Get instance of singleton
    static TodoDatabase getInstance(Context context) {
        if(instance == null) {
            instance = new TodoDatabase(context);
        }
        Log.d("TAART", instance.toString());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODOS);

        this.insert("Creating a todo item");
        this.insert("Finding a purpose for life");
        this.insert("Summarize Shakespears Taming of the Shrew");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE_TODOS);
        onCreate(db);
    }

    Cursor selectAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SELECT_ALL, null);
    }

    void insert(String title) {
        ContentValues val = new ContentValues();
        val.put(NAME_TODO, title);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(NAME_TABLE_TODOS, null, val);
    }

    void update(long id, boolean status) {
        int statusInt = 0;
        if (status) {
            statusInt = 1;
        }
        ContentValues val = new ContentValues();
        val.put(NAME_STATUS, statusInt);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(NAME_TABLE_TODOS, val, "_id = " + id, null);
    }

    void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_TABLE_TODOS, "_id = " + id,  null);
    }

}
