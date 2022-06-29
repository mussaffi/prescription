package com.example.notmyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class SplashS extends AppCompatActivity {
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
                        MediaPlayer m=MediaPlayer.create(SplashS.this,R.raw.sound);
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
                Intent i = new Intent(SplashS.this, MainActivity.class);
                startActivity(i);
            }
        };
        mSplashThread.start();
        if(!isConnectedToInternet())
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashS.this);
            builder1.setMessage("internet connection needed");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


    }
    private class BroadCastBattery extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int battery = intent.getIntExtra("level", 0);
            if (battery < 30)
            {
                Toast.makeText(SplashS.this, "Your battery is low please charge your phone", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadCastBattery);
    }

    // Check  internet connection
    public boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

}