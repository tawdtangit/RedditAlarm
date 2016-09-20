package com.example.todd.redditalarm;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.todd.redditalarm.ADAPTERS.fileBrowserAdapter;
import com.example.todd.redditalarm.ListObjects.MusicObject;
import com.example.todd.redditalarm.ListObjects.SubRedditObjects;
import com.example.todd.redditalarm.ListObjects.listAlarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class addAlarmActivity extends AppCompatActivity {
    private Button createBtn,addMusic,addSubReddit;
    private SeekBar seekBar;
    AlertDialog alertDialog;
    private float volume=0;
    private  ToggleButton mon,tues,wed,thur,fri,sat,sun;
    private boolean[] dates=new boolean[7];
    private Calendar calendar;
    private MusicObject tune;
    private SubRedditObjects subReddit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);
        String ids = TimeZone.getTimeZone("America/Los_Angeles").getID();

        SimpleTimeZone pdt = new SimpleTimeZone(-7 * 60 * 60 * 1000, ids);
        this.calendar=new GregorianCalendar(pdt);
        //System.out.println(calendar.getTimeZone().toString());
        //TEST
        /*
        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        */
        dates[calendar.get(Calendar.DAY_OF_WEEK)-1]=true;
        mon= (ToggleButton) findViewById(R.id.Monday);
        tues= (ToggleButton) findViewById(R.id.Tuesday);
        wed= (ToggleButton) findViewById(R.id.Wednesday);
        thur= (ToggleButton) findViewById(R.id.Thursday);
        fri= (ToggleButton) findViewById(R.id.Friday);
        sat= (ToggleButton) findViewById(R.id.Saturday);
        sun= (ToggleButton) findViewById(R.id.Sunday);
        createBtn= (Button) findViewById(R.id.create);
        addMusic=(Button) findViewById(R.id.alarm_tone);
        addSubReddit=(Button) findViewById((R.id.choose_sub));
        seekBar=(SeekBar) findViewById(R.id.volCtrl);
        //initialize states
        sun.setChecked(dates[0]);
        mon.setChecked(dates[1]);
        tues.setChecked(dates[2]);
        wed.setChecked(dates[3]);
        thur.setChecked(dates[4]);
        fri.setChecked(dates[5]);
        sat.setChecked(dates[6]);
        //Listeners
        createBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                allOnClick(view);
            }
        });
        addMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allOnClick(view);
            }
        });
        addSubReddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allOnClick(view);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                volume=(float) progress/seekBar.getMax();
                ((TextView) findViewById(R.id.volumeDisplay)).setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[1]=b;
            }
        });
        tues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[2]=b;
            }
        });
        wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[3]=b;
            }
        });
        thur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[4]=b;
            }
        });
        fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[5]=b;
            }
        });
        sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[6]=b;
            }
        });
        sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dates[0]=b;
            }
        });



        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("One of your inputs is invalid");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public void allOnClick(View view){
        switch (view.getId()){
            case R.id.create:
                EditText name= (EditText) findViewById(R.id.add_name);
                TimePicker time=(TimePicker)findViewById((R.id.timePicker));
                Log.d("create:", "Create button pressed");
                if(!name.getText().toString().matches("") && tune!=null && subReddit!=null) {
                    listAlarm newAlarm=new listAlarm(name.getText().toString(), true, time.getCurrentHour(), time.getCurrentMinute(),dates,tune,subReddit.getURL(),volume);
                    TwoFragment.myList.add(newAlarm);
                    Intent temp = new Intent(this, MainActivity.class);
                    MainActivity.alarmReciever.setAlarm(this.getApplicationContext(),newAlarm);
                    int page = 1;
                    temp.putExtra("ARG_PAGE", page);
                    startActivity(temp);
                }
                else{

                    alertDialog.show();
                }
                break;
            case R.id.alarm_tone:
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else {
                    final AddMusicPopUp temp= new AddMusicPopUp();
                    temp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (temp.getAdapter().getSelectedFileName()!=null) {
                                ((TextView) findViewById(R.id.alarm_tone)).setText(temp.getAdapter().getSelectedFileName());
                                tune=new MusicObject(temp.getAdapter().getSelectedFileName(),temp.getAdapter().getSelectedPath());
                            }
                        }
                    });
                    temp.show(getFragmentManager(), "addMusicPopUp");

                }
                break;
            case R.id.choose_sub:
                final addSubreddit temp = new addSubreddit();
                temp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (temp.getAdapter().getSubRedditObjects()!=null) {
                            ((TextView) findViewById(R.id.choose_sub)).setText(temp.getAdapter().getSubRedditObjects().getStr());
                            subReddit= new SubRedditObjects(temp.getAdapter().getSubRedditObjects().getStr(),temp.getAdapter().getSubRedditObjects().getURL(),temp.getAdapter().getSubRedditObjects().getID());
                        }
                    }
                });

                temp.show(getFragmentManager(), "addSubreddit");

        }
    }
}
