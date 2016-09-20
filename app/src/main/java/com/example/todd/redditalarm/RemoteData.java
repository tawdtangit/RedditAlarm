package com.example.todd.redditalarm;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Todd on 8/1/2016.
 */
public class RemoteData {
    public static HttpURLConnection getConnection(String url){
        System.out.print("Attempting to Connect with: " + url);
        HttpURLConnection connection=null;
        try{
            connection= (HttpURLConnection) new URL(url).openConnection();
            //connection.setReadTimeout(30000);
            connection.setRequestProperty("Cookie",ThreeFragment.redditCookie);
        }
        catch(MalformedURLException e){
            Log.e("getConnection()", "Invalid URL: "+e.toString());
        }
        catch (IOException e){
            Log.e("getConnection()", "Could not connect: "+e.toString());
        }
        return connection;
    }
    public static String readContents(String url){
        HttpURLConnection connection=getConnection(url);
        if(connection==null){
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder(8192);
            String tmp="";
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((tmp=br.readLine())!=null){
                sb.append(tmp).append("/n");
            }
            br.close();
            return sb.toString();
        }
        catch (IOException e){
            Log.d("READ FAILED", e.toString());
            return null;
        }
    }
}
