package xavi.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Minessweeper_main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minessweeper);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_minessweeper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,Config.class);
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickHelp(View v){
        Intent i = new Intent(Minessweeper_main.this,Help.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void clickStart(View v){
        Intent i = new Intent(Minessweeper_main.this,Config.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void clickExit(View v){
        finish();
    }

    public void clickQuery(View v ){
        Toast.makeText(this,"Query DB not implemented yet.",Toast.LENGTH_SHORT).show();
        /*
            Intent i = new Intent(this,DisplayBBDD.class);
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        */
    }

}
