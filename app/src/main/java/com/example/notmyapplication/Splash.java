package com.example.notmyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    BroadCastBattery broadCastBattery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        Thread mSplashThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized (this)
                    {
                        MediaPlayer m=MediaPlayer.create(Splash.this,R.raw.sound);
                        m.start();
                        wait(4000);
                        m.stop();
                        broadCastBattery = new BroadCastBattery();
                        registerReceiver(broadCastBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finish();
                Intent i = new Intent(Splash.this, SplashS.class);
                startActivity(i);
            }
        };
        mSplashThread.start();
    }
    private class BroadCastBattery extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int battery = intent.getIntExtra("level", 0);
            if (battery < 30)
            {
                Toast.makeText(Splash.this, "Your battery is low please charge your phone", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadCastBattery);
    }

}