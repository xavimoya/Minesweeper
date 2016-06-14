package xavi.minesweeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xavi on 22/5/16.
 */
public class DataBase extends SQLiteOpenHelper {

    DataBase db = null;

    String sqlCreate = "CREATE TABLE Users " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " alias TEXT, " +
            " nColumns INTEGER, " +
            " hasTime BOOLEAN" +
            " seconds INTEGER" +
            " transcurredTime INTEGER" +
            " percentage TEXT " +
            " result TEXT)";


    private DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBase getInstance(Context context){
        return db==null ? db = new DataBase(context,"ResultsData",null,1):db;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL(sqlCreate);
    }
}
