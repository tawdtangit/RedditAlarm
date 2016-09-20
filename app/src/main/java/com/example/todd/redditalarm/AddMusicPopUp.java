package com.example.todd.redditalarm;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.todd.redditalarm.ADAPTERS.fileBrowserAdapter;
import com.example.todd.redditalarm.ListObjects.MusicObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Todd on 6/23/2016.
 */
public class AddMusicPopUp extends DialogFragment {
    private fileBrowserAdapter adapter;
    private DialogInterface.OnDismissListener onDismissListener;

    public static List<String> data= new ArrayList<String>();
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog b = new Dialog(getActivity());
        b.setTitle("Find a Tune");
        b.setContentView(R.layout.browse_popup);
        RecyclerView rv= (RecyclerView) b.findViewById(R.id.browse_popup_rv);
        rv.setLayoutManager(new LinearLayoutManager(b.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter=new fileBrowserAdapter(data,b);
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
    public fileBrowserAdapter getAdapter(){
        return adapter;
    }
}
