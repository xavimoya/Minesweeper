package xavi.minesweeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xavi on 22/5/16.
 */
public class DataBase extends SQLiteOpenHelper {

    private static DataBase db;
    private static final String DATABASE_NAME = "ResultsData";
    private static final String DATABASE_TABLE = "DataGame";
    private static final int DATABASE_VERSION = 1;

    String sqlCreate = "CREATE TABLE " + DATABASE_TABLE +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " alias TEXT, " +
            " nColumns INTEGER, " +
            " hasTime BOOLEAN, " +
            " seconds INTEGER, " +
            " totalTime INTEGER, " +
            " percentage TEXT, " +
            " result TEXT )";



    private DataBase(Context context){
        super(context.getApplicationContext(),DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static synchronized DataBase getInstance(Context context){
        return db==null ? db = new DataBase(context.getApplicationContext()):db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL(sqlCreate);
    }




}
