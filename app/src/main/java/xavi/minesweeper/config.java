package xavi.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by xavi on 25/4/15.
 */
public class config extends Activity {
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
        Intent i = new Intent(config.this,game.class);
        EditText et1 = (EditText) findViewById(R.id.editText);
        EditText et2 = (EditText) findViewById(R.id.editText_parrilla);
        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        RadioGroup rb = (RadioGroup)findViewById(R.id.radiogroup);
        alias = et1.getText().toString();
        columns = Integer.parseInt(et2.getText().toString());
        i.putExtra("num_Casillas", columns);
        if (cb.isChecked()){
            time = true;
            i.putExtra("time",true);
            EditText et = (EditText)findViewById(R.id.textTime);
            int value = Integer.parseInt(et.getText().toString());
            i.putExtra("timevalue",value);
        }else{
            time = false;
            i.putExtra("time",false);
        }
        radiovalue = ((RadioButton)findViewById(rb.getCheckedRadioButtonId() )).getText().toString();
        i.putExtra("percentage",radiovalue);
        startActivityForResult(i,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 123){
            if(resultCode == RESULT_OK){
                String result = data.getData().toString();
                Intent res = new Intent(this,results.class);
                res.putExtra("ALIAS",alias);
                res.putExtra("COLS",columns);
                res.putExtra("TIME",time);
                res.putExtra("PERCENT",radiovalue);
                Bundle b = data.getExtras();
                String text = b.getString("TEXT");
                res.putExtra("TEXT",text);
                if(time){
                    int trans = b.getInt("TRANSCURRED");
                    res.putExtra("TRANSCURRED",trans);
                }
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
