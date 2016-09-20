package com.example.todd.redditalarm.ListObjects;
import android.util.Log;

import com.example.todd.redditalarm.TwoFragment;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by Todd on 6/16/2016.
 */
public class listAlarm implements Serializable{
    private String name;
    private boolean on;
    private int hour;
    private int minute;
    private boolean [] dates;
    private int[] IntentID;
    private MusicObject music;
    private String subredditURL;
    private float volume;
    public listAlarm(String n, boolean Switch, int setHour,int setMinute,boolean[] d,MusicObject music, String s, float vol){
        name=n;
        on=Switch;
        hour=setHour;
        minute=setMinute;
        this.music=music;
        this.dates=d;
        this.volume=vol;
        IntentID= new int[7];
        subredditURL=s;
        int index=0;
        for (boolean bool:d) {
            if(bool){
                IntentID[index++]= TwoFragment.IDS++;
            }
            else{
                IntentID[index++]=-1;
            }
        }

    }
    public String getText(){
        return name;
    }
    public void setText(String newName){
        name=newName;
    }
    public boolean getState(){
        return on;
    }
    public void setState(boolean newState){
        on=newState;
    }
    public int getHour(){
        return hour;
    }
    public void setHour(int newHour){
        hour=newHour;
    }
    public int getMinute(){
        return minute;
    }
    public void setMinute(int newMinute){
        minute=newMinute;
    }
    public boolean[] getDates(){
        return dates;
    }
    public boolean getDates(int postion){
        return dates[postion];
    }
    public void setDates(boolean temp,int position){
        dates[position]=temp;
    }
    public String printDates(){
        String print="(";
        String[] days= {"Sun.", "Mon.", "Tues.", "Wed.", "Thur.", "Fri.", "Sat."};
        boolean first=false;
        for (int i=0;i<dates.length;i++){
            if(dates[i]){
                if(first){
                    print+=", ";
                }
                print+=days[i];
                first=true;
            }
        }
        if(!first){
            return "";
        }
        return print+")";
    }
    public int numberOfDays(){
        int count=0;
        for (boolean bool:dates) {
            if (bool)
                count++;
        }
        return count;
    }
    public int[] getIntentID(){
        return IntentID;
    }
    public int getIntentID(int position){
        return IntentID[position];
    }
    public void printIDs(){
        for (int a:IntentID) {
            Log.d("IDS:", String.valueOf(a));
        }
    }
    public MusicObject getMusic(){
        return music;
    }

    public String getSubredditURL() {
        return subredditURL;
    }

    public void setSubreddit(String subredditURL) {
        this.subredditURL = subredditURL;
    }

    public float getVolume() {
        return volume;
    }
}
