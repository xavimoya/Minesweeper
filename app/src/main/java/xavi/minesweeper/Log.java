package xavi.minesweeper;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Log extends Fragment {

    private int pos = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_log, container, false);
            return view;
        }

        public void printDataInLayout(String text){
            TextView tv = (TextView)getActivity().findViewById(R.id.log_text);
            String initial = (String) tv.getText();
            String text2 = (initial + "\n" + pos + ". " + text);
            tv.setText(text2);
            pos++;
        }

}
