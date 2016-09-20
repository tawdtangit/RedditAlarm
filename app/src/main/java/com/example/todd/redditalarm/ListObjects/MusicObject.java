package com.example.todd.redditalarm.ListObjects;

import java.io.Serializable;

/**
 * Created by Todd on 6/23/2016.
 */
public class MusicObject implements Serializable {
    private String Name;
    private String path;
    public MusicObject(String subName, String path){
        this.Name =subName;
        this.path=path;
    }
    public String getName(){
        return Name;
    }
    public String getPath(){
        return path;
    }
}
