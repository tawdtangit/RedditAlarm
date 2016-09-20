package com.example.todd.redditalarm;

/**
 * Created by Todd on 6/14/2016.
 */


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todd.redditalarm.ADAPTERS.SubRedditPostAdapter;
import com.example.todd.redditalarm.ListObjects.SubRedditObjects;
import com.example.todd.redditalarm.ListObjects.SubRedditPostObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment{
    private List<SubRedditPostObject> mdata;
    private SubRedditPostAdapter adapter;
    private static int tester=0;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        /* t = new Thread(new Runnable() {
            @Override
            public void run() {
                mdata=fetchPosts();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.frag_one_rv);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        mdata= new ArrayList<SubRedditPostObject>();
        adapter= new SubRedditPostAdapter(mdata);
        rv.setAdapter(adapter);

        new AsyncCaller().execute();

        return view;
    }
    private List<SubRedditPostObject> fetchPosts(){
        String url = "https://www.reddit.com/.json";
        String raw= RemoteData.readContents(url);
        List<SubRedditPostObject> subData= new ArrayList<SubRedditPostObject>();

        try{
            JSONObject data =new JSONObject(raw).getJSONObject("data");
            JSONArray children=data.getJSONArray("children");
            for(int i=0;i < children.length();i++){
                String name=children.getJSONObject(i).getJSONObject("data").optString("title");
                String URL=children.getJSONObject(i).getJSONObject("data").optString("url");
                subData.add(new SubRedditPostObject(name,URL));
            }
        }
        catch (Exception e){
            Log.d("fetchPosts()",e.toString());
        }
        return subData;
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        List<SubRedditPostObject> subList = new ArrayList<SubRedditPostObject>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getActivity(), "ASYNC RUNNING", Toast.LENGTH_LONG).show();
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            subList = fetchPosts();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Toast.makeText(getActivity(), "ASYNC FINISHED", Toast.LENGTH_LONG).show();
            mdata.addAll(subList);
            adapter.notifyDataSetChanged();
            //this method will be running on UI thread

        }
    }

}
