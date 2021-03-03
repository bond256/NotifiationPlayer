package com.example.notifiationplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;

public class AudioNotificationService extends Service {

    private static final String TAG = "tag";
    private MediaPlayer mediaPlayer;
    private int count = 0;
    private ArrayList<String> musicLocation;
    private ArrayList<String> musicTitle;
    private Handler handler;
    private Intent intentTime;

    @Override
    public void onCreate() {
        super.onCreate();
        musicLocation = new ArrayList<>();
        musicTitle=new ArrayList<>();
        //mediaPlayer = MediaPlayer.create(AudioNotificationService.this, musicList.get(count));
        handler = new Handler(Looper.getMainLooper());
        intentTime = new Intent("sent");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                switch (intent.getAction()) {
                    case StatesPlayer.PLAY:
                        try {
                            mediaPlayer.setDataSource(musicLocation.get(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                        intentTime.putExtra("Duration", mediaPlayer.getDuration());
                        LocalBroadcastManager.getInstance(AudioNotificationService.this).sendBroadcast(intentTime);
                        handler.postDelayed(updateSeekBar, 0);
                        break;
                    case StatesPlayer.PAUSE:
                        mediaPlayer.pause();
                        break;
                    case StatesPlayer.NEXT:
                        if (count < musicList.size() - 1) {
                            mediaPlayer.stop();
                            mediaPlayer = MediaPlayer.create(AudioNotificationService.this, musicList.get(++count));
                            intentTime.putExtra("Duration", mediaPlayer.getDuration());
                            LocalBroadcastManager.getInstance(AudioNotificationService.this).sendBroadcast(intentTime);
                            mediaPlayer.start();
                        }
                        break;

                    case StatesPlayer.PREVIOUS:
                        if (count != 0) {
                            mediaPlayer.stop();
                            mediaPlayer = MediaPlayer.create(AudioNotificationService.this, musicList.get(--count));
                            intentTime.putExtra("Duration", mediaPlayer.getDuration());
                            LocalBroadcastManager.getInstance(AudioNotificationService.this).sendBroadcast(intentTime);
                            mediaPlayer.start();
                        }
                        break;

                    case StatesPlayer.SEEK:
                        int position = intent.getIntExtra("position", 0);
                        mediaPlayer.seekTo(position);
                        break;
                }
            }
        }).start();
        Log.d(TAG, "onStartCommand: ");
        return START_REDELIVER_INTENT;
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            intentTime.putExtra("CurrentPosition", mediaPlayer.getCurrentPosition());
            LocalBroadcastManager.getInstance(AudioNotificationService.this).sendBroadcast(intentTime);

            handler.postDelayed(this, 1000);
            if (mediaPlayer.getCurrentPosition() > mediaPlayer.getDuration()) {
                handler.removeCallbacks(this);
                if (count < musicList.size()&& count<musicList.size()-1) {
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(AudioNotificationService.this, musicList.get(++count));
                    intentTime.putExtra("Duration", mediaPlayer.getDuration());
                    LocalBroadcastManager.getInstance(AudioNotificationService.this).sendBroadcast(intentTime);
                    mediaPlayer.start();
                    handler.postDelayed(this, 0);
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
