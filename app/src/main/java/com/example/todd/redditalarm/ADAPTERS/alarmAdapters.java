package com.example.todd.redditalarm.ADAPTERS;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.todd.redditalarm.ItemTouchHelperAdapter;
import com.example.todd.redditalarm.ItemTouchHelperViewHolder;
import com.example.todd.redditalarm.MainActivity;
import com.example.todd.redditalarm.R;
import com.example.todd.redditalarm.ListObjects.listAlarm;

import java.util.Collections;
import java.util.List;

/**
 * Created by Todd on 6/15/2016.
 */
public class alarmAdapters extends RecyclerView.Adapter<alarmAdapters.ViewHolder> implements ItemTouchHelperAdapter {
    private List<listAlarm> mData;
    private View v;



    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public Switch mySwitch;
        public ViewHolder(View v) {
            super(v);
            mySwitch = (Switch) v.findViewById(R.id.switch1);
        }
    }
    public alarmAdapters(List<listAlarm> myData){
        mData=myData;
    }
    @Override
    public alarmAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_alarm_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(alarmAdapters.ViewHolder holder, final int position) {
        String time="";
        int hour= mData.get(position).getHour();
        int minute=mData.get(position).getMinute();
        boolean morning=true;
        if(hour>12){
            hour=hour%12;
            morning=false;
        }
        else if(hour==0){
            hour=12;
        }
        if(minute<10){
            time = hour + ":0" + mData.get(position).getMinute();
        }
        else {
            time = hour + ":" + mData.get(position).getMinute();
        }
        if(morning)
            time+=" am ";
        else
            time+=" pm ";
        holder.mySwitch.setText(mData.get(position).getText()+"\n"+ time + mData.get(position).printDates());
        holder.mySwitch.setChecked(mData.get(position).getState());
        holder.mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("On/Off", String.valueOf(b));
                Log.d("Hour", String.valueOf(mData.get(position).getHour()));
                Log.d("Minute", String.valueOf(mData.get(position).getMinute()));
                Log.d("Dates",mData.get(position).printDates());
                mData.get(position).printIDs();
                mData.get(position).setState(b);
                if (b){
                    MainActivity.alarmReciever.setAlarm(v.getContext(),mData.get(position));
                }
                else{
                    MainActivity.alarmReciever.removeAlarm(v.getContext(),mData.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public boolean moveDrag(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void dismissSwipe(int position) {
        MainActivity.alarmReciever.removeAlarm(v.getContext(),mData.get(position));
        mData.remove(position);
        notifyItemRemoved(position);
    }

}
