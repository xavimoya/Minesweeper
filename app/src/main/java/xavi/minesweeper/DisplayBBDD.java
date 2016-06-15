package xavi.minesweeper;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DisplayBBDD extends ListActivity {

    private SQLiteDatabase sqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bbdd);
        DataBase db = DataBase.getInstance(this);
        sqldb = db.getReadableDatabase();

        Cursor c = sqldb.rawQuery("SELECT * FROM " + R.string.name_tabledb,null);

        String[] from = new String[]{"alias","totalTime","percentage"};
        int[] to = new int[]{R.id.txtAlias,R.id.txtTtime,R.id.txtpercent};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.list_customized,c,from,to);

        setListAdapter(adapter);

        c.close();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        sqldb.close();

    }
}
