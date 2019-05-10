package geyer.sensorlab.psychapp101;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class EventDetection extends Service {
    private final IBinder mBinder = new myBinder();
    BroadcastReceiver screenReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callNotification();
        initializeBroadcastReceiver();
        return START_STICKY;
    }

    private void callNotification() {
        Notification note = initializeService();
        startForeground(101, note);
    }

    private Notification initializeService() {

        Resources resources = getResources();
        String contentTitle = resources.getString(R.string.app_name),
                contentText = resources.getString(R.string.app_name) + " is recording data";


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(contentTitle, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setSound(null,null);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            channel.setShowBadge(true);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }

        }

        NotificationCompat.Builder nfc = new NotificationCompat.Builder(getApplicationContext(),contentTitle)
                .setSmallIcon(R.drawable.ic_data_logging)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_data_logging))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET) //This hides the notification from lock screen
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setOngoing(true);


        nfc.setContentTitle(contentTitle);
        nfc.setContentText(contentText);
        nfc.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText).setBigContentTitle(contentTitle));
        nfc.setWhen(System.currentTimeMillis());

        return nfc.build();

    }

    private void initializeBroadcastReceiver() {
        final DataStorage dataStorage = new DataStorage(this);
        screenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() != null){
                    switch (intent.getAction()){
                        case Intent.ACTION_SCREEN_OFF:
                            dataStorage.screenOff();
                            break;
                        case Intent.ACTION_SCREEN_ON:
                            dataStorage.screenOn();
                            break;
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(screenReceiver, intentFilter);
    }

    public Long retrieveData(){
        final DataStorage dataStorage = new DataStorage(this);
        Log.i("BACK", "intent to return data");
        Long timeUsingPhone = dataStorage.timeUsingPhone();
        Log.i("BACK", "time used phone: "+ timeUsingPhone);
        return timeUsingPhone;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
    }

    class myBinder extends Binder {
        EventDetection getService() {
            return EventDetection.this;
        }
    }

}
