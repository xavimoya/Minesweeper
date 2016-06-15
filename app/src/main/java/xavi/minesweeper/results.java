package xavi.minesweeper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.*;
import android.os.Process;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.*;

/**
 * Created by xavi on 25/4/15.
 */
public class Results extends Activity {
    String totaltext;
    String subject;
    DataBuilder dataBuilder;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);
        Bundle bundle = getIntent().getExtras();
       /* String alias = bundle.getString("ALIAS");
        String percent = bundle.getString("PERCENT");
        boolean haveTime = bundle.getBoolean("TIME");
        int columns = bundle.getInt("COLS");
        String text = bundle.getString("TEXT");*/
        dataBuilder = bundle.getParcelable(getString(R.string.extraData));
       // saveTheResult(dataBuilder);
        String alias = dataBuilder.getAlias();
        String percent = dataBuilder.getPercentage();
        boolean haveTime = dataBuilder.getHasTime();
        int columns = dataBuilder.getNumOfColumns();
        String text = dataBuilder.getText();

        TextView time = (TextView) findViewById(R.id.resultTime);
        TextView log = (TextView) findViewById(R.id.resultLog);
        Calendar c = getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd , HH:mm:ss aaa");
        String timeText = sdf.format(c.getTime());
        time.setText(timeText);
        subject = "LOG - " +timeText;
        String logText = "Alias: "+ alias+ " "+ getString(R.string.str_boxes)+" "+(columns*columns)+" " + getString(R.string.str_mines)+" "+percent;
        if (haveTime){
            //int tr = bundle.getInt("TRANSCURRED");
            int tr = dataBuilder.getTranscurredTime();
            logText = logText + " "+getString(R.string.str_trTime) +" "+tr+ getString(R.string.str_seconds);
        }
        totaltext=logText +"\n" + text;
        log.setText(totaltext);

    }

    public void onclickSendMail(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.result_one_mail)} );
        i.putExtra(Intent.EXTRA_SUBJECT,subject);
        i.putExtra(Intent.EXTRA_TEXT,totaltext);
        startActivity(i);
        finish();
    }

    public void onclickNewGame(View v){
        finish();
    }

    public void onclickExit(View v){
        moveTaskToBack(true);
        finish();
    }

    private void saveTheResult(DataBuilder data){
        DataBase db = DataBase.getInstance(this);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("alias",data.getAlias());
        cv.put("nColumns",data.getNumOfColumns());
        cv.put("hasTime",data.getHasTime());
        cv.put("seconds",data.getSeconds());
        cv.put("totalTime",data.getTranscurredTime());
        cv.put("percentage",data.getPercentage());
        cv.put("result",data.getText());
        long i = sqldb.insert(getString(R.string.name_tabledb),null,cv);
        if (i==-1)//insert error
            showToast("Error inserting row");
        else
            showToast("Insert a row with ID: "+i);
    }

    private void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
