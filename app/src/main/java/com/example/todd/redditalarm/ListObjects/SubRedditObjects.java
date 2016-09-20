package com.example.todd.redditalarm.ListObjects;

import com.example.todd.redditalarm.ADAPTERS.SubRedditAdapter;

/**
 * Created by Todd on 7/31/2016.
 */
public class SubRedditObjects {
    private String str,URL,ID;
    public SubRedditObjects(String str, String URL, String ID){
        this.str=str;
        this.URL=URL;
        this.ID=ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    public String toString(){
        return "This is Subreddit has the name " + getID() + " at " + getURL() + " with the ID:" + getID();
    }

}
