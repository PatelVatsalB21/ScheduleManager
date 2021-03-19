package com.example.schedulemanager.Utils;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class NotifierService extends Service {
    MediaPlayer player;
    Vibrator vibrator;
    AudioManager audioManager;
    NotificationManager notificationManager;
    Uri ring;
    long[] pattern = {0, 1000, 1000};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        player = MediaPlayer.create(this, ring);
        player.setLooping(true);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(NotifierService.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == 0) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        player.start();
        vibrator.vibrate(pattern, 0);
    }


    @Override
    public void onDestroy() {
        if (vibrator != null) vibrator.cancel();
        if (player != null) player.stop();
        if (notificationManager != null)notificationManager.cancel(1111);
        super.onDestroy();
    }
}
