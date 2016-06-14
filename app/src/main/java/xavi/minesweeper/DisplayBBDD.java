package xavi.minesweeper;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayBBDD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bbdd);
        DataBase db = null;
        db = db.getInstance(this.getApplicationContext());
        SQLiteDatabase sqldb = db.getReadableDatabase();
    }
}
