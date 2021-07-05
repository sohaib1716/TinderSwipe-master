package com.papanews.ak;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.papanews.R;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class MediaPlayerService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int playPause = intent.getIntExtra("play", 0);
        int pause = intent.getIntExtra("pause", 0);
        Log.e("pleaseplease", String.valueOf(playPause));

        if(global.mMediaPlayer.isPlaying()){
            global.mMediaPlayer.pause();
        }else{
            global.mMediaPlayer.start();
        }

        if(playPause==1){
            global.mMediaPlayer.start();
            Log.e("pleaseplease", String.valueOf(playPause));
        }
        if(pause==1){
            global.mMediaPlayer.pause();
        }

        // if you want cancel notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }
}