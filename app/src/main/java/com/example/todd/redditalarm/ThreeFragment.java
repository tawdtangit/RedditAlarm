package com.example.todd.redditalarm;

/**
 * Created by Todd on 6/14/2016.
 */


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todd.redditalarm.ADAPTERS.SubRedditAdapter;
import com.example.todd.redditalarm.ListObjects.SubRedditObjects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class ThreeFragment extends Fragment implements View.OnClickListener{
    private Button loginReddit;
    private EditText Rusername, Rpassword;
    private View view;
    private AlertDialog alertDialog;
    private List<SubRedditObjects> mdata;
    private SubRedditAdapter adapter;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Unable Login");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
       if(redditCookie==null) {
            view = inflater.inflate(R.layout.fragment_three, container, false);
            loginReddit = (Button) view.findViewById(R.id.loginButton);
            loginReddit.setOnClickListener(this);
            // Inflate the layout for this fragment
            return view;
       }
        else{
            view = inflater.inflate(R.layout.fragment_three_logged_in,container,false);

            /*Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    mdata=fetchSubRedditList();
                }
            });
           t.start();
           try {
               t.join();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }*/
           RecyclerView rv= (RecyclerView) view.findViewById(R.id.frag_three_rv);
           rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
           rv.setItemAnimator(new DefaultItemAnimator());
           mdata= new ArrayList<SubRedditObjects>();
           adapter=new SubRedditAdapter(mdata,null);
           rv.setAdapter(adapter);

           new AsyncCaller().execute();
           return view;
        }

    }
    @Override
    public void onClick(View view){
        Log.d("Login","Attempting to login");
        Thread t= new Thread(new Runnable() {
            public void run() {
                login();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (redditCookie==null){
            alertDialog.show();
        }
        else{
            alertDialog.setMessage("Login Successful");
            alertDialog.show();
            replaceFragment();
        }


    }
    private void replaceFragment(){
        Fragment currentFragment = this;
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }
    private List<SubRedditObjects> fetchSubRedditList(){
        List<SubRedditObjects> subList=new ArrayList<SubRedditObjects>();
        //Log.d("fetchSubreddits()","Attempting to fetch");
        String url="https://www.reddit.com/subreddits/mine.json";
        if(ThreeFragment.redditCookie==null){
            url="https://www.reddit.com/subreddits/default.json";
        }
        String raw = RemoteData.readContents(url);
        try{
            JSONObject data= new JSONObject(raw).getJSONObject("data");
            JSONArray children= data.getJSONArray("children");
            for(int i=0 ; i<children.length();i++){
                String name=children.getJSONObject(i).getJSONObject("data").optString("display_name");
                String URL=children.getJSONObject(i).getJSONObject("data").optString("url");
                String ID=children.getJSONObject(i).getJSONObject("data").optString("id");
                subList.add(new SubRedditObjects(name,URL,ID));
            }
        }
        catch (Exception e){
            Log.d("fetchSubReddits()",e.toString());
        }
        return subList;
    }
    private HttpURLConnection getConnection(String url){
        URL temp=null;
        try{
            temp= new URL(url);
        }catch (MalformedURLException e){
            Log.d("Invalid URL",url);
            return null;
        }
        HttpURLConnection connection=null;
        try{
            connection=(HttpURLConnection) temp.openConnection();
        }catch (IOException e){
            Log.d("Unable to Connect",url);
            return null;
        }
        connection.setReadTimeout(30000);
        connection.setDoOutput(true);
        return connection;
    }
    private boolean writeToConnection(HttpURLConnection con, String data){
        try{
            PrintWriter pw=new PrintWriter(
                    new OutputStreamWriter(
                            con.getOutputStream()
                    )
            );
            pw.write(data);
            pw.close();
            return true;
        }catch(IOException e){
            Log.d("Unable to write", e.toString());
            return false;
        }
    }
    // The login API URL
    private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

    // The Reddit cookie string
// This should be used by other methods after a successful login.
    public static String redditCookie = null;

    // This method lets you log in to Reddit.
// It fetches the cookie which can be used in subsequent calls
// to the Reddit API.
    private boolean login(){
        String username=((EditText) view.findViewById(R.id.RedditUsername)).getText().toString();
        String password=((EditText) view.findViewById(R.id.RedditPassword)).getText().toString();
        HttpURLConnection connection = getConnection(REDDIT_LOGIN_URL);
        redditCookie=null;
        if(connection == null)
            return false;

        //Parameters that the API needs
        String data="user="+username+"&passwd="+password;

        if(!writeToConnection(connection, data))
            return false;

        String cookie=connection.getHeaderField("set-cookie");

        if(cookie==null)
            return false;

        cookie=cookie.split(";")[0];
        if(cookie.startsWith("reddit_first")){
            // Login failed
        }else if(cookie.startsWith("reddit_session")){
            // Login success
            Log.d("Success", cookie);
            redditCookie = cookie;
            //Toast.makeText(view.getContext(),"SUCCESSFULLY LOGGED IN",Toast.LENGTH_LONG);
            return true;
        }
        Log.d("Error", "Unable to login.");
        return false;
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        List<SubRedditObjects> subList=new ArrayList<SubRedditObjects>();

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
            subList=fetchSubRedditList();

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
