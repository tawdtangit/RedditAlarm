package com.example.todd.redditalarm;

/**
 * Created by Todd on 6/14/2016.
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.todd.redditalarm.ADAPTERS.alarmAdapters;
import com.example.todd.redditalarm.ListObjects.listAlarm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class TwoFragment extends Fragment implements View.OnClickListener{
    private Button btnAdd;

    View view;
    alarmAdapters adapter;
    static List<listAlarm> myList;
    public static int IDS=0;
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HERE","Fragment TWO");
        /*if(savedInstanceState!=null){
            ArrayList<String> names=savedInstanceState.getStringArrayList("names");
            for (String name:names) {
                myList.add((listAlarm) savedInstanceState.getSerializable(name));
            }
            IDS=savedInstanceState.getInt("IDS");
            Log.d("HERE","WE PARSING SAVE");
        }*/
        if(myList==null) {
            try {
                Log.d("HERE", "WE PARSING SAVE");

                FileInputStream fis = getContext().openFileInput("RedditAlarms");
                ObjectInputStream is = new ObjectInputStream(fis);
                myList = (ArrayList<listAlarm>) is.readObject();
                is.close();
                fis.close();
                FileInputStream fisIDS = getContext().openFileInput("IDS");
                ObjectInputStream isIDS = new ObjectInputStream(fisIDS);
                IDS = (int) isIDS.readObject();
                isIDS.close();
                fisIDS.close();
                Log.d("HERE", String.valueOf(IDS));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(myList==null){
            myList = new ArrayList<listAlarm>();
        }
        view=inflater.inflate(R.layout.fragment_two, container, false);
        RecyclerView rv= (RecyclerView) view.findViewById(R.id.rView);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        btnAdd=(Button) view.findViewById(R.id.addAlarm);


        btnAdd.setOnClickListener(this);

        adapter = new alarmAdapters(myList);
        rv.setAdapter(adapter);
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        //setListAdapter((ListAdapter) new ArrayAdapter<String>(getActivity(), R.layout.my_alarm_item_layout, R.id.switch1, myList));

        return view;

    }
    /*@Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.d("HERE ","SAVING...");
        ArrayList<String > names= new ArrayList<String>();
        state.putInt("IDS",IDS);
        for (listAlarm a: myList) {
            names.add(a.getText());
            state.putSerializable(a.getText(),a);
        }
        state.putStringArrayList("names",names);
    }*/
    @Override
    public void onStop(){
        super.onStop();
        try {
            Log.d("HERE ","SAVING...");

            FileOutputStream fos = getContext().openFileOutput("RedditAlarms", Context.MODE_PRIVATE);
            FileOutputStream fosIDS = getContext().openFileOutput("IDS", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            ObjectOutputStream osIDS = new ObjectOutputStream(fosIDS);
            osIDS.writeObject(IDS);
            os.writeObject(myList);
            os.close();
            fos.close();
            osIDS.close();
            fosIDS.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.addAlarm:
                Log.d("add:","addingButton is pressed");
                startActivity(new Intent(getActivity(),addAlarmActivity.class ));
                //myList.add("Hello");
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
