package com.example.todd.redditalarm;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.todd.redditalarm.ADAPTERS.SubRedditPostAdapter;
import com.example.todd.redditalarm.ListObjects.SubRedditObjects;
import com.example.todd.redditalarm.ListObjects.SubRedditPostObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LockActivity extends AppCompatActivity {
    private SubRedditPostAdapter adapter;
    private MediaPlayer mp;
    static private List<SubRedditPostObject> subData= new ArrayList<SubRedditPostObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_lock);
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                getSubRedditPosts(getIntent().getStringExtra("SubRedditURL"));

            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RecyclerView rv= (RecyclerView) findViewById(R.id.SubredditList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter=new SubRedditPostAdapter(subData);
        rv.setAdapter(adapter);

        //INIT MUSIC
        audioPlayer(getIntent().getStringExtra("MusicPath"));
        //END INIT MUSIC
        //TODO implement REDDIT API HERE
        /*
        for(int i=0;i<10;i++){
            String t= String.valueOf(i);
            subData.add(new SubRedditPostObject(t,"r/fake"));
        }
        */

        adapter.notifyDataSetChanged();

        Button dismiss= (Button) findViewById(R.id.dismiss);
        Button stopAlarm=(Button)findViewById(R.id.StopAlarm);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudio();
                finish();

            }
        });
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudio();
            }
        });


    }
    public void audioPlayer(String path){
        mp=new MediaPlayer();

        try{
            mp.setVolume(getIntent().getFloatExtra("Volume",0),getIntent().getFloatExtra("Volume",0));
            adapter.setMusicPlayer(mp);
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopAudio(){
        if(mp!=null) {
            mp.stop();
        }
    }
    public void getSubRedditPosts(String sub){
        //String url="http://www.reddit.com/subreddits/mine.json";
        subData.clear();
        Log.d("Sub Variable",sub);
        String url="https://www.reddit.com"+sub+".json";
        String raw=RemoteData.readContents(url);
        try{
            JSONObject data =new JSONObject(raw).getJSONObject("data");
            JSONArray children=data.getJSONArray("children");
            for(int i=0;i < children.length();i++){
                String name=children.getJSONObject(i).getJSONObject("data").optString("title");
                String URL=children.getJSONObject(i).getJSONObject("data").optString("url");
                //Log.d("NAME:",name);
                subData.add(new SubRedditPostObject(name,URL));
            }
        }
        catch (Exception e){
            Log.d("fetchSubReddits()",e.toString());
        }
    }
}
