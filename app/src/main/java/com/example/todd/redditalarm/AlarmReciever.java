package com.example.todd.redditalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.todd.redditalarm.ListObjects.listAlarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by Todd on 6/21/2016.
 */
public class AlarmReciever extends BroadcastReceiver {
    public static final long WEEK_LENGTH=1000*60*60*24*7;


    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        Log.d("Received:", "Alarm is ringing");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "");
        wl.acquire();

        Intent newIntent=new Intent(context, AlarmService.class);
        newIntent.putExtra("MusicPath",intent.getStringExtra("MusicPath"));
        newIntent.putExtra("SubRedditURL",intent.getStringExtra("SubRedditURL"));
        newIntent.putExtra("Volume",intent.getFloatExtra("Volume",0));
        context.startService(newIntent);
        wl.release();
    }



    public void setAlarm(Context context,listAlarm alarm){
        AlarmManager am=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d("Connected","Setting Alarm " + alarm.getText() );

        Intent intent=new Intent(context,AlarmReciever.class);

        for (int i=0;i<alarm.getIntentID().length;i++) {
            if(alarm.getDates(i) && alarm.getIntentID(i)!=-1) {
                intent.putExtra("MusicPath",alarm.getMusic().getPath());
                intent.putExtra("SubRedditURL",alarm.getSubredditURL());
                intent.putExtra("Volume",alarm.getVolume());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getIntentID(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                long start=convertToMS(i,alarm.getHour(),alarm.getMinute());
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, start,WEEK_LENGTH ,pendingIntent);
            }
        }

    }
    public void removeAlarm(Context context, listAlarm alarm){
        AlarmManager am=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReciever.class);
        for (int i=0;i<alarm.getIntentID().length;i++) {
            if(alarm.getDates(i) && alarm.getIntentID(i)!=-1) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getIntentID(i), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.cancel(pendingIntent);
            }
        }
        Log.d("Connected","removing Alarm "+ alarm.getText());
    }
    public long convertToMS(int days, int hours, int minute){
        //calender
        String ids = TimeZone.getTimeZone("America/Los_Angeles").getID();

        SimpleTimeZone pdt = new SimpleTimeZone(-7 * 60 * 60 * 1000, ids);
        Calendar calendar=new GregorianCalendar(pdt);
        Calendar alarmTime= new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),hours,minute);
        long offset=0;
        if(calendar.get(Calendar.DAY_OF_WEEK)-1>days){
            offset=days+7-(calendar.get(Calendar.DAY_OF_WEEK)-1);
        }
        else{
            offset=days-(calendar.get(Calendar.DAY_OF_WEEK)-1);
        }
        long alarm=alarmTime.getTimeInMillis()+offset*24*3600*1000;
        while(alarm<calendar.getTimeInMillis()){
            alarm+=WEEK_LENGTH;
        }
        alarmTime.setTimeInMillis(alarm);
        Log.d("Offset", String.valueOf(offset));
        Log.d("CurrentTime:", String.valueOf(calendar.getTime()));
        Log.d("AlarmTime:", String.valueOf(alarmTime.getTime()));
        return alarm;
        /*Calendar alarmTime=new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)+days-calendar.get(Calendar.DAY_OF_WEEK)+1,hours,minute);
        System.out.println(alarmTime.getTime().toString());
        long alarm=alarmTime.getTimeInMillis();
        while(alarm<calendar.getTimeInMillis()){
            alarm+=WEEK_LENGTH;
        }
        Log.d("CurrentTime:", String.valueOf(calendar.getTimeInMillis()));
        Log.d("AlarmTime:", String.valueOf(alarm));
        return alarm;*/

    }
}
