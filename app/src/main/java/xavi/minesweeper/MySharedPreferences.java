package xavi.minesweeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MySharedPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mypreferences);

        SharedPreferences prefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

    }


}
