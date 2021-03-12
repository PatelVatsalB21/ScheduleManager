package com.example.schedulemanager.email;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class NetworkReceiver extends BroadcastReceiver{
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("NETWORKRECEIVER","CALLED");
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();


            if (ni != null && ni.isAvailable() && ni.isConnected()) {
                Log.e("NETWORKRECIEVER", "Network " + ni.getTypeName() + " connected");
                context.startService(new Intent(context,EmailService.class));

            }
            else{

                SystemClock.sleep(10000);

                if (ni != null && ni.isAvailable() &&ni.isConnected()) {
                    Intent serviceIntent =new Intent(context,EmailService.class);
                    serviceIntent.putExtra("networkOn",true);
                    context.startService(serviceIntent);
                    Log.e("NETWORKRECIEVER", "Network " + ni.getTypeName() + " connected");
                } else{
                Log.e("NETWORKRECIEVER", "There's no network connectivity");
            }
        }

    }
}
