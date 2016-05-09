package xavi.minesweeper;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Xavi on 8/5/16.
 */
public class DataBuilder implements Parcelable {
    String alias;
    int numOfColumns;
    boolean hasTime;
    int seconds;
    String percentage;
    String text;
    int transcurredTime;

    public DataBuilder(){
        this.alias="";
        this.numOfColumns=5;
        this.hasTime=false;
        this.seconds=0;
        this.percentage="25%";

    }
    public DataBuilder(String alias, int numOfColumns, boolean hasTime, int seconds, String percentage ){
        this.alias=alias;
        this.numOfColumns=numOfColumns;
        this.hasTime=hasTime;
        this.seconds=seconds;
        this.percentage=percentage;
    }

    protected DataBuilder(Parcel in) {
        alias = in.readString();
        numOfColumns = in.readInt();
        hasTime = in.readByte() != 0;
        seconds = in.readInt();
        percentage = in.readString();
        text = in.readString();
        transcurredTime = in.readInt();
    }

    public static final Creator<DataBuilder> CREATOR = new Creator<DataBuilder>() {
        @Override
        public DataBuilder createFromParcel(Parcel in) {
            return new DataBuilder(in);
        }

        @Override
        public DataBuilder[] newArray(int size) {
            return new DataBuilder[size];
        }
    };

    public String getAlias(){
        return this.alias;
    }

    public int getNumOfColumns() {
        return this.numOfColumns;
    }

    public boolean getHasTime(){
        return this.hasTime;
    }

    public int getSeconds(){
        return this.seconds;
    }

    public String getPercentage(){
        return this.percentage;
    }

    public String getText(){ return this.text;}

    public int getTranscurredTime(){ return this.transcurredTime;}

    public void setAlias(String alias){
        this.alias=alias;
    }

    public void setNumOfColumns(int numOfColumns) {
         this.numOfColumns=numOfColumns;
    }

    public void setHasTime(boolean hasTime){
         this.hasTime=hasTime;
    }

    public void setSeconds(int seconds){
         this.seconds=seconds;
    }

    public void setPercentage(String percentage){
         this.percentage=percentage;
    }

    public void setText(String text){ this.text=text;}

    public void setTranscurredTime(int transcurredTime){ this.transcurredTime=transcurredTime;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeInt(numOfColumns);
        dest.writeByte((byte) (hasTime ? 1 : 0));
        dest.writeInt(seconds);
        dest.writeString(percentage);
        dest.writeString(text);
        dest.writeInt(transcurredTime);
    }
}
