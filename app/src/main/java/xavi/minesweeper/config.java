package xavi.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by xavi on 25/4/15.
 */
public class Config extends Activity {

    DataBuilder dataBuilder = new DataBuilder();
    String alias = "";
    int columns = 5;
    boolean time = false;
    String radiovalue = "%25";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);
        EditText et = (EditText)findViewById(R.id.editText_parrilla);
        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
                EditText timeText = (EditText) findViewById(R.id.textTime);
                if (cb.isChecked()){
                    timeText.setText("30");
                    timeText.setVisibility(View.VISIBLE);
                }else{
                    timeText.setVisibility(View.INVISIBLE);
                }

            }
        });
    }
    public void clickStartGame(View v){
        EditText et2 = (EditText) findViewById(R.id.editText_parrilla);
        columns = Integer.parseInt(et2.getText().toString());
        if (columns < 2 || columns > 10){
            TextView ct = (TextView)findViewById(R.id.configtext);
            ct.setText(R.string.textErrorColumn);
            ct.setTextColor(Color.RED);
            ct.setTextSize(20);

        }else {
            Intent i = new Intent(Config.this, Game.class);
            EditText et1 = (EditText) findViewById(R.id.editText);
            CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
            RadioGroup rb = (RadioGroup) findViewById(R.id.radiogroup);
            alias = et1.getText().toString();
            columns = Integer.parseInt(et2.getText().toString());
            dataBuilder.setNumOfColumns(columns);
            //i.putExtra("num_Casillas", columns);
            if (cb.isChecked()) {
                time = true;
                dataBuilder.setHasTime(true);
                //i.putExtra("time", true);
                EditText et = (EditText) findViewById(R.id.textTime);
                int value = Integer.parseInt(et.getText().toString());
                dataBuilder.setSeconds(value);
                //i.putExtra("timevalue", value);
            } else {
                time = false;
                dataBuilder.setHasTime(false);
                //i.putExtra("time", false);
            }
            radiovalue = ((RadioButton) findViewById(rb.getCheckedRadioButtonId())).getText().toString();
           // i.putExtra("percentage", radiovalue);
            dataBuilder.setPercentage(radiovalue);
            i.putExtra(getString(R.string.extraData), dataBuilder);
            startActivityForResult(i, 123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 123){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(resultCode == RESULT_OK){
                String result = data.getData().toString();
                Intent res = new Intent(this,Results.class);
                /*res.putExtra("ALIAS",alias);
                res.putExtra("COLS",columns);
                res.putExtra("TIME",time);
                res.putExtra("PERCENT",radiovalue);*/
                dataBuilder.setAlias(alias);
                Bundle b = data.getExtras();
                DataBuilder db = b.getParcelable(getString(R.string.extraData));
                //String text = b.getString("TEXT");
                String text = db.getText();
                //res.putExtra("TEXT",text);
                dataBuilder.setText(text);
                if(time){
                    dataBuilder.setTranscurredTime(db.getTranscurredTime());
                    //int trans = b.getInt("TRANSCURRED");
                    //res.putExtra("TRANSCURRED",trans);

                }
                res.putExtra(getString(R.string.extraData), dataBuilder);
                switch(result){
                    case "WIN":
                        res.addFlags(res.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(res);
                        finish();
                        break;
                    case "BOOM":
                        res.addFlags(res.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(res);
                        finish();
                        break;
                    case "TIMEOUT":
                        res.addFlags(res.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(res);
                        finish();
                        break;

                }
            }
        }
    }
}
