package com.example.notifiationplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent= new Intent(this, AudioNotificationService.class);
        //startService(intent);
        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ViewPager viewPager=findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager(),2);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ContentResolver contentResolver=getContentResolver();
        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=contentResolver.query(songUri,null,null,null);
        int songTitle=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int locSongs=cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        while (cursor.moveToNext()){
            String title=cursor.getString(songTitle);
            String locSong=cursor.getString(locSongs);
            Log.d("tag", "onCreate: "+title+" / "+locSong);
        }

//        createNotificationChannel();
//        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.natification);
//        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setSmallIcon(android.R.drawable.ic_delete)
//                .setContent(remoteViews)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .build();


    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}