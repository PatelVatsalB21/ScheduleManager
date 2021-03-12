package com.example.schedulemanager.note;//package com.example.firebase.note;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.IBinder;
//import android.os.Vibrator;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.JobIntentService;
//
//public class AlarmService extends Service {
//
//    static MediaPlayer mediaPlayer;
//    static Vibrator vibrator;
//    Integer position;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null) {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//
//        mediaPlayer = MediaPlayer.create(this, alarmUri);
//        mediaPlayer.setLooping(true);
//
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("jobintent" , "onHandleWork: ");
//
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null) {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//
//        mediaPlayer = MediaPlayer.create(this, alarmUri);
//        mediaPlayer.setLooping(true);
//
//        if (intent != null) {
//            onTaskRemoved(intent);
//            position = intent.getIntExtra("position", -1);
//            Boolean restart = intent.getBooleanExtra("restart", false);
//            if (restart) {
//                if (position > -1) {
//
//                    Intent i = new Intent(AlarmService.this, AlarmReciever.class);
//                    i.putExtra("position", position);
//                }
//            }
//
//
//            if (intent.getBooleanExtra("start", false)) {
//
//                Log.d("Ringing",String.valueOf(intent.getBooleanExtra("start", false)));
//                mediaPlayer.start();
//
//                long[] pattern = {0, 600, 400, 600};
//                vibrator.vibrate(pattern, 0);
//
//
//            } else if (intent.getBooleanExtra("stop", false)) {
//
//
//                Log.d("StoppingRinging",String.valueOf(intent.getBooleanExtra("start", false)));
//
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                vibrator.cancel();
//            }
//        }
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//
////        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
////        if (alarmUri == null) {
////            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        }
////
////        mediaPlayer = MediaPlayer.create(AlarmService.this, alarmUri);
////        mediaPlayer.setLooping(true);
////        vibrator = (Vibrator) AlarmService.this.getSystemService(Context.VIBRATOR_SERVICE);
//        mediaPlayer.stop();
//        mediaPlayer.reset();
//        vibrator.cancel();
//        stopSelf();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
////    public  void destroy(){
////        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
////        if (alarmUri == null) {
////            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        }
////
////        mediaPlayer = MediaPlayer.create(this, alarmUri);
////        mediaPlayer.setLooping(true);
////        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
////        mediaPlayer.stop();
////        mediaPlayer.reset();
////        vibrator.cancel();
////
////    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        Intent i = new Intent(getApplicationContext(), this.getClass());
//        i.putExtra("restart", true);
//        i.putExtra("position", position);
//        startService(i);
//        super.onTaskRemoved(rootIntent);
//    }
//}
//
