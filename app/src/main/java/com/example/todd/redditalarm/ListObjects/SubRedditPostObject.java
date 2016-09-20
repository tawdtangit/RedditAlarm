package com.example.todd.redditalarm.ListObjects;


import java.io.Serializable;

/**
 * Created by Todd on 6/22/2016.
 */
public class SubRedditPostObject implements Serializable {
    private String description;
    private final String Title;
    private final String URL;
    public SubRedditPostObject(String Title, String URL){

        this.Title = Title;
        this.URL =URL;
    }
    public String getTitle(){
        return Title;
    }
    public String getURL(){
        return URL;
    }
}
