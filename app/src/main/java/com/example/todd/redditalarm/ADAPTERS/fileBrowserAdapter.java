package com.example.todd.redditalarm.ADAPTERS;

import android.app.Dialog;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todd.redditalarm.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Todd on 6/23/2016.
 */
public class fileBrowserAdapter extends RecyclerView.Adapter<fileBrowserAdapter.ViewHolder> {
    private List<String> mdata;
    private List<String> path;
    private String root;
    private String SelectedPath;
    private String SelectedFileName;
    private Dialog dialog;
    public fileBrowserAdapter(List<String> data, Dialog dialog){
        mdata=data;
        this.dialog=dialog;
        path= new ArrayList<String>();
        //TODO BEWARE ONLY KNOW IT WORKS FOR ANDROID NEXUS AND SAMSUNG
        root ="/storage";


        getDir(root);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_file_object_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.fileName.setText(mdata.get(position));
        holder.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file=new File(path.get(position));
                if(file.isDirectory()){
                    if(file.canRead())
                        getDir(path.get(position));
                    else
                        Toast.makeText(view.getContext(), "CANNOT READ", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(view.getContext(), mdata.get(position), Toast.LENGTH_SHORT).show();
                    SelectedPath=path.get(position);
                    SelectedFileName=mdata.get(position);
                    dialog.dismiss();
                }
            }
        });
    }
    public String getSelectedPath(){
        return SelectedPath;
    }
    public String getSelectedFileName(){
        return SelectedFileName;
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fileName;
        public ViewHolder(View view){
            super(view);
            fileName = (TextView) view.findViewById(R.id.file_object_view);
        }
    }

    //FILE BROWSING METHODS
    public  void getDir(String dirPath){
        String deviceName = android.os.Build.MODEL;
        Log.d("DEVICE", deviceName);
        mdata.clear();

        path.clear();
        File file=new File(dirPath);

        File[] filelist=file.listFiles();
        //TODO NEED MORE ABSTRACT WAY OF FINDING STORAGE
        if(deviceName.equals("Nexus 5") && root.equals("/storage")){
            root="/sdcard/Download";
            getDir(root);
            return;
        }
        if(!dirPath.equals(root)){
            mdata.add("/");
            path.add(root);
            mdata.add("../");
            path.add(file.getParent());
            Log.d("PARENT:",dirPath);
        }

        for(int i=0; i<filelist.length;i++){
            File temp=filelist[i];
            if(!temp.isHidden() && temp.canRead()) {
                String filenameArray[] = temp.toString().split("\\.");
                String extension = filenameArray[filenameArray.length-1];

                if (temp.isDirectory()) {
                    mdata.add(temp.getName() + "/");
                    path.add(temp.getPath());
                } else if (extension.equals("mp3")) {
                    mdata.add(temp.getName());
                    path.add(temp.getPath());
                }
            }

        }
        notifyDataSetChanged();
    }
}
