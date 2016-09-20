package com.example.todd.redditalarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Todd on 6/22/2016.
 */
public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        Intent temp = new Intent(this, LockActivity.class);
        temp.putExtra("MusicPath",intent.getStringExtra("MusicPath"));
        temp.putExtra("SubRedditURL",intent.getStringExtra("SubRedditURL"));
        temp.putExtra("Volume",intent.getFloatExtra("Volume",0));
        temp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(temp);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
