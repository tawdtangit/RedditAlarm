package com.example.todd.redditalarm;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.todd.redditalarm.ADAPTERS.SubRedditAdapter;
import com.example.todd.redditalarm.ADAPTERS.SubRedditPostAdapter;
import com.example.todd.redditalarm.ADAPTERS.fileBrowserAdapter;
import com.example.todd.redditalarm.ListObjects.SubRedditObjects;
import com.example.todd.redditalarm.ListObjects.SubRedditPostObject;
import com.example.todd.redditalarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Todd on 7/31/2016.
 */
public class addSubreddit extends DialogFragment {
    private SubRedditAdapter adapter;
    private DialogInterface.OnDismissListener onDismissListener;

    private List<SubRedditObjects> mdata= new ArrayList<>();
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog b = new Dialog(getActivity());
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                mdata=fetchSubReddits();
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        b.setTitle("Pick a Subreddit");
        b.setContentView(R.layout.subreddit_list);
        RecyclerView rv= (RecyclerView) b.findViewById(R.id.subreddit_popup_rv);
        rv.setLayoutManager(new LinearLayoutManager(b.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter=new SubRedditAdapter(mdata,b);
        /*for(int i=0;i<10;i++){
            String t= String.valueOf(i);
            data.add(new SubRedditObjects(t,"r/fake","fakeURL/"+ (10-i)));
        }*/
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return b;
    }
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
    public SubRedditAdapter getAdapter(){
        return adapter;
    }

    public List<SubRedditObjects> fetchSubReddits(){
        List<SubRedditObjects> subList=new ArrayList<SubRedditObjects>();
        //Log.d("fetchSubreddits()","Attempting to fetch");
        String url="https://www.reddit.com/subreddits/mine.json";
        if(ThreeFragment.redditCookie==null){
            url="https://www.reddit.com/subreddits/default.json";
        }
        //String url="http://www.reddit.com/r/askreddit/"+".json";
        String raw=RemoteData.readContents(url);
        try{
            JSONObject data =new JSONObject(raw).getJSONObject("data");
            JSONArray children=data.getJSONArray("children");
            for(int i=0;i < children.length();i++){
                                    String name=children.getJSONObject(i).getJSONObject("data").optString("display_name");
                                    String URL=children.getJSONObject(i).getJSONObject("data").optString("url");
                                    String ID=children.getJSONObject(i).getJSONObject("data").optString("id");
                //Log.d("NAME:",name);
                subList.add(new SubRedditObjects(name,URL,ID));
            }
            return subList;
        }
        catch (Exception e){
            Log.d("fetchSubReddits()",e.toString());
        }
        return subList;
    }

}
