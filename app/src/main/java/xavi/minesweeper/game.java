package xavi.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
//import android.widget.Toast;


/**
 * Created by xavi on 25/4/16.
 *
 */
public class Game extends AppCompatActivity {
    //Toast toast;
    GridView gridview;
    int[] filenames;
    public int numCols = 5; //default size
    public int percentageOfMines = 25; //default
    boolean time = false; //default
    int timeout = 30; //default
    int transcurred = 0;
    private long startTime = 0L;
    private TextView tm;
    boolean finished = false;
    private android.os.Handler cusHandler = new android.os.Handler();
    long timeInMilliseconds = 0L;
    int numberOfMines = 0;
    private boolean firstClick = true;
    DataBuilder dataBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        Bundle bundle = getIntent().getExtras();
        dataBuilder = bundle.getParcelable(getString(R.string.extraData));
        assert dataBuilder != null;
        numCols = dataBuilder.getNumOfColumns();
        time = dataBuilder.getHasTime();
        String bundleStr = dataBuilder.getPercentage();
        if (bundleStr != null) {
            switch (bundleStr) {
                case "%15":
                    percentageOfMines = 15;
                    break;
                case "%25":
                    percentageOfMines = 25;
                    break;
                case "%35":
                    percentageOfMines = 35;
                    break;
                default:
                    break;
            }
        }else{
            //By default use a 25 percent
            percentageOfMines = 25;
        }
        int numBoxes = numCols*numCols;
        numberOfMines = (numBoxes*percentageOfMines)/100;
        TextView tv = (TextView)findViewById(R.id.numberOfMines);
        tv.setText(String.format(getString(R.string.numberofmines),numberOfMines));
        if(time){
            timeout = dataBuilder.getSeconds();
            tm = (TextView)findViewById(R.id.timer);
            startTime = SystemClock.uptimeMillis();
            cusHandler.postDelayed(updateTimerThread,0);
        }

        filenames = new int[numBoxes];
        for (int i=0; i<numBoxes;i++){
             filenames[i]= 0; // 0 means a void box
        }

        gridview = (GridView)findViewById(R.id.gridmines);
        gridview.setNumColumns(numCols);
        gridview.setAdapter(new ButtonAdapter(this));

    }



    private void setInitialBoombs(int numBoxes){
        for(int i=0; i<numberOfMines; i++){
            int j = (int) (Math.random() * (numBoxes - 1));
            if (filenames[j] == 0){
                filenames[j] = -1; //-1 means a BOOM box
            }else{
                i--;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("TIME_SWAP",timeSwapBuff);
        savedInstanceState.putLong("TIMESTART",startTime);
        savedInstanceState.putIntArray("FILENAMES",filenames);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        timeSwapBuff = savedInstanceState.getLong("TIME_SWAP");
        startTime = savedInstanceState.getLong("TIMESTART");
        filenames = savedInstanceState.getIntArray("FILENAMES");
    }


    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs=(int)(updatedTime / 1000);
            int mins = secs/60;
            transcurred = secs;
            secs = secs%60;
            String strTM = getString(R.string.timerName)+" " + mins + ":"
                    + String.format("%02d", secs);
            tm.setText(strTM);
            if(itsTime(mins,secs) && !finished){
                finished = true;
                Intent data = new Intent();
                data.setData(Uri.parse("TIMEOUT"));
                String text = String.format(getString(R.string.sentenceWhenTimeout),numClosedBoxes(), numberOfMines);
                dataBuilder.setText(text);
                if(time) dataBuilder.setTranscurredTime(transcurred);

                data.putExtra(getString(R.string.extraData),  dataBuilder);
                TextView tv = (TextView)findViewById(R.id.gametitle);
                tv.setText(R.string.titleENDtime);
                tv.setTextColor(Color.RED);
                setResult(RESULT_OK,data);
                finish(); //LOSE BECAUSE TIMEOUT
            }else if(!finished){
                cusHandler.postDelayed(this, 0);
            }
        }
    };

    private int numClosedBoxes(){
        int num = 0;
        for(int i:filenames){
            if(i==0 || i == -1) num++;
        }
        return num;
    }

    private boolean itsTime(int mins, int secs){
        if(timeout<60){
            if (timeout <= secs) return true;
        }else{
            int timeoutmin = timeout/60;
            int timeoutsec = timeout%60;
            if(timeoutmin < mins) return true;
            if(timeoutmin == mins){
                if(timeoutsec <= secs) return true;
            }
        }
        return false;
    }

    public class ButtonAdapter extends BaseAdapter{
        private Context mContext;
        public ButtonAdapter(Context c){
            this.mContext = c;
        }

        @Override
        public int getCount() {
            return filenames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Button btn;


            if (convertView == null) {
                btn = new Button(mContext);
                View v = findViewById(R.id.idgrid);
                int width = v.getWidth();
                double density = (width - 10)/ (dataBuilder.getNumOfColumns()+1);

                btn.setLayoutParams(new GridView.LayoutParams((int)(density), (int)(density)));
                btn.setFocusable(false);
                btn.setClickable(false);
            } else {
                btn = (Button) convertView;
            }
            if (filenames[position] == 0 || filenames[position] == -1) {
                btn.setId(position);
                btn.setBackgroundResource(R.drawable.color_buttons);
                btn.setOnClickListener(new ClickedListener(position));
                btn.setOnLongClickListener(new LongClickedListener(position));
                return btn;
            } else if (filenames[position] == -2) {
                btn.setId(position);
                btn.setBackgroundResource(R.drawable.color_buttonpressed);
                return btn;
            }else if(filenames[position] == -3) {
                btn.setId(position);
                btn.setBackgroundResource(R.drawable.mine64);
                return btn;
            }else if(filenames[position] == -10 || filenames[position] == -11){
                btn.setId(position);
                btn.setBackgroundResource(R.drawable.flag64);
                btn.setOnClickListener(new ClickedListener(position));
                btn.setOnLongClickListener(new LongClickedListener(position));
                return btn;
            }else{
                btn.setText(String.valueOf(filenames[position]));
                btn.setTextColor(Color.WHITE);
                if (dataBuilder.getNumOfColumns() > 8) {
                    btn.setTextSize(11);
                } else if (dataBuilder.getNumOfColumns() > 7) {
                    btn.setTextSize(15);
                } else {
                    btn.setTextSize(20);
                }
                btn.setId(position);
                btn.setBackgroundResource(R.drawable.color_buttonpressed);
                return btn;
            }
        }
    }

    private boolean win(){
        for(int i : filenames){
            if (i == 0) return false;
        }
        return true;
    }

    public class LongClickedListener implements View.OnLongClickListener{

        private final int position;
        public LongClickedListener(int p){
            this.position = p;
        }

        @Override
        public boolean onLongClick(View v) {

            if (filenames[position] == 0) filenames[position] = -10; //flag to void cell
            else if(filenames[position] == -10) filenames[position] = 0;
            else if(filenames[position] == -1) filenames[position] = -11; //flag over the bomb
            else if(filenames[position] == -11) filenames[position] = -1;

            gridview.setAdapter(new ButtonAdapter(getApplicationContext()));
            return true;
        }
    }

    public class ClickedListener implements View.OnClickListener {

        private final int position;
        public ClickedListener(int p){
            this.position = p;
        }
        @Override
        public void onClick(View v) {
            boolean frags = false;
            Log frag = (Log) getSupportFragmentManager().findFragmentById(R.id.frag_log);
            if (frag != null && frag.isInLayout()){
                frags = true;
            }

            if (firstClick){
                firstClick = false;
                filenames[position]=1;
                setInitialBoombs(numCols*numCols);
                filenames[position]=0;
                algorithmForBox(position);
                if (frags) frag.printDataInLayout("You touch the first button: \n  " + "row/col" + getCellInPos(position));
                gridview.setAdapter(new ButtonAdapter(getApplicationContext()));
            }
            if(filenames[position]==-1 && !finished){ //BOOM
                if (frags) frag.printDataInLayout("You touch a BOMB! \n  " + "row/col" + getCellInPos(position));
                filenames[position]=-3;
                finished = true;
                gridview.setAdapter(new ButtonAdapter(getApplicationContext()));
                Intent data = new Intent();
                data.setData(Uri.parse("BOOM"));
                String text = String.format(getString(R.string.sentenceWhenLose), numClosedBoxes(), numberOfMines);
                if(time) dataBuilder.setTranscurredTime(transcurred);
                //See the bombs...
                for (int i=0; i<numCols*numCols; i++){
                    if (filenames[i]==-1 || filenames[i] == -11){
                        filenames[i]=-3;
                    }
                    gridview.setAdapter(new ButtonAdapter(getApplicationContext()));
                }
                //End see the bombs..
                dataBuilder.setText(text);
                data.putExtra(getString(R.string.extraData),dataBuilder);

                TextView tv = (TextView)findViewById(R.id.gametitle);
                tv.setText(R.string.titleENDlose);
                tv.setTextColor(Color.RED);

                setResult(RESULT_OK,data);

                finish(); //LOSE BECAUSE CLICKED BOMB
            }else if (filenames[position] == 0){ //first click in this button

                if (frags) frag.printDataInLayout("You touch the button: \n  " + "row/col" + getCellInPos(position));
                algorithmForBox(position);
                gridview.setAdapter(new ButtonAdapter(getApplicationContext()));
                if(win() && !finished){
                    finished = true;
                    Intent data = new Intent();
                    data.setData(Uri.parse("WIN"));
                    String text;
                    if (time){
                        dataBuilder.setTranscurredTime(transcurred);
                        text = String.format(getString(R.string.sentenceWhenWinWithTimer),(timeout-transcurred));
                    }else{
                        text = getString(R.string.sentenceWhenWinWithOUTTimer);
                    }
                    dataBuilder.setText(text);
                    //data.putExtra("TEXT",text);
                    data.putExtra(getString(R.string.extraData),dataBuilder);
                    TextView tv = (TextView)findViewById(R.id.gametitle);
                    tv.setText(R.string.titleENDwin);
                    tv.setTextColor(Color.BLUE);

                    setResult(RESULT_OK,data);
                    finish(); //WIN
                }

            }
        }
    }

          /*  private void showToast(String text) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
                toast.show();
            }
*/
    private void algorithmForBox(int position){
        int bombs = getAdjacencyBombs(position);
        if(bombs == 0){
            filenames[position]= -2;
            expand(position);
        }else{
            filenames[position] = bombs;
        }

    }

    private int getAdjacencyBombs(int position){
        int bombs = 0;
        bombs = bombs + checkHorizontally(position);
        bombs = bombs + checkVerticallyAndDiagonal(position);
        return bombs;
    }

    private int checkHorizontally(int position){
        int bombs = 0;
        //Check Horizontally
        if(position>0 && (position)%numCols != 0){//Value not at the beginning
            if(filenames[position-1] == -1 || filenames[position-1] == -3 || filenames[position-1] == -11) bombs++;
        }
        if(position<(numCols*numCols)-1 && (position+1)%numCols != 0){ //Value not at end
            if(filenames[position+1] == -1 || filenames[position+1] == -3 || filenames[position+1] == -11) bombs++;
        }
        return bombs;
    }

    private int checkVerticallyAndDiagonal(int position){
        int bombs = 0;
        //Check Vertically and Diagonal
        //UP
        if((position+numCols) < (numCols*numCols)){
            if(filenames[position+numCols] == -1 || filenames[position+numCols] == -3 || filenames[position+numCols] == -11) bombs++; //Vertically
            bombs = bombs + checkHorizontally(position+numCols); //Diagonal
        }
        //DOWN
        if((position-numCols) >= 0){
            if(filenames[position-numCols] == -1 || filenames[position-numCols] == -3 || filenames[position-numCols] == -11) bombs++; //Vertically
            bombs = bombs + checkHorizontally(position-numCols); //Diagonal
        }
        return bombs;
    }

    private void expand(int pos){
        expandHorizontal( pos);
        expandVertical( pos);
    }

    private void expandHorizontal(int position){
        if(position>0 && (position)%numCols != 0) {//Value not at the beginning
            if(filenames[position-1] == 0 || filenames[position-1] == 10)algorithmForBox(position-1);
        }
        if(position<(numCols*numCols)-1 && (position+1)%numCols != 0) { //Value not at end
            if(filenames[position+1] == 0 || filenames[position+1] == 10) algorithmForBox(position+1);
        }
    }

    private void expandVertical(int position) {
        if ((position + numCols) < (numCols * numCols)) {
            if(filenames[position+numCols] == 0 || filenames[position+numCols] == 10) algorithmForBox(position + numCols);
            expandHorizontal(position + numCols);
        }
        if ((position - numCols) >= 0) {
            if(filenames[position-numCols] == 0 || filenames[position-numCols] == 10) algorithmForBox(position - numCols);
            expandHorizontal(position - numCols);
        }
    }

    private String getCellInPos(int position){
        String col = "";
        int row = 0;
        boolean isfinish = false;
        while (!isfinish){
            row ++;
            if (position/numCols < 0 ){
                col = Integer.toString(position) + 1 ;
                isfinish = true;
            }else{
                position = position - numCols;
            }
        }
        return "("+ row + "," +col+")";
    }

}
