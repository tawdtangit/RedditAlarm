package com.example.todd.redditalarm.ADAPTERS;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.todd.redditalarm.ItemTouchHelperViewHolder;
import com.example.todd.redditalarm.ListObjects.SubRedditObjects;
import com.example.todd.redditalarm.ListObjects.listAlarm;
import com.example.todd.redditalarm.R;

import java.util.List;

/**
 * Created by Todd on 7/31/2016.
 */
public class SubRedditAdapter extends RecyclerView.Adapter<SubRedditAdapter.ViewHolder> {
    private List<SubRedditObjects> mData;
    private View v;
    private Dialog dialog;
    private SubRedditObjects SRO;
    public SubRedditAdapter(List<SubRedditObjects> data, Dialog d) {
        mData=data;
        dialog=d;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView SubReddit;
        // each data item is just a string in this case
        public ViewHolder(View itemView) {
            super(itemView);
            SubReddit=(TextView) itemView.findViewById(R.id.subreddit_list_object);
        }
    }
    @Override
    public SubRedditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.subreddit_list_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SubRedditAdapter.ViewHolder holder, final int position) {
        holder.SubReddit.setText(mData.get(position).getStr());
        if(dialog!=null) {
            holder.SubReddit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SRO = mData.get(position);
                    dialog.dismiss();
                }
            });
        }
        else {
            holder.SubReddit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Attempting to connect to:", mData.get(position).getURL());
                    holderClick(position);
                }
            });
        }
    }
    public void holderClick(int position){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com"+mData.get(position).getURL()));
        v.getContext().startActivity(browserIntent);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public SubRedditObjects getSubRedditObjects(){
        return SRO;
    }
}
