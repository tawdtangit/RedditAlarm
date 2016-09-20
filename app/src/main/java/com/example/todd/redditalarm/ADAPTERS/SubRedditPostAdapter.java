package com.example.todd.redditalarm.ADAPTERS;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todd.redditalarm.R;
import com.example.todd.redditalarm.ListObjects.SubRedditPostObject;

import java.util.List;

/**
 * Created by Todd on 6/22/2016.
 */
public class SubRedditPostAdapter extends RecyclerView.Adapter<SubRedditPostAdapter.ViewHolder> {
    private List<SubRedditPostObject> mData;
    private View v;
    private MediaPlayer mp;
    public SubRedditPostAdapter(List<SubRedditPostObject> list){
        mData=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.subreddit_item_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position).getTitle());
        holder.subreddit.setText(mData.get(position).getURL());
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holderClick(position);
            }
        });
        holder.subreddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holderClick(position);
            }
        });
    }
    public void holderClick(int position){
        if(mp!=null) {
            mp.stop();
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getURL()));
        v.getContext().startActivity(browserIntent);
    }
    public void setMusicPlayer(MediaPlayer mp){
        this.mp=mp;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Title;
        public TextView subreddit;
        public ViewHolder(View itemView) {
            super(itemView);
            Title=(TextView) itemView.findViewById(R.id.SubTitle);
            subreddit=(TextView) itemView.findViewById(R.id.subreddit);
        }
    }
}
