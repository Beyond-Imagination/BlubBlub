package beyond_imagination.blubblub.pWebConnection;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.Setting;

/**
 * Created by cru65 on 2017-10-25.
 */

/**
 * @file AutoService.java
 * @breif
 * Background service. If user set auto, bowl maintaining will work in situation that application is finished.
 * Get condition data, compare with user setting value and send request to bowl.
 * @author Yehun Park
 */
public class AutoService extends Service {
    /****************/
    /*** Variable ***/
    /****************/
    private Setting setting;

    private GetConditionDataService getConditionDataService;

    /****************/
    /*** Function ***/
    /****************/
    @Override
    public void onCreate() {
        Log.d("AutoService", "onCreate()");
        getConditionDataService = new GetConditionDataService();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AutoService", "onStartCommand");

        startForeground(1, new Notification());

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.main_icon)
                .setContentTitle("")
                .setContentText("")
                .build();


        nm.notify(startId, notification);
        nm.cancel(startId);

        if (intent == null) {
            return Service.START_STICKY;
        } else {
            setting = intent.getParcelableExtra("setting");
            getConditionDataService.setSetting(setting);
            getConditionDataService.getDataHandler().setToken(intent.getStringExtra("token"));
            getConditionDataService.getDataHandler().setSecret(intent.getStringExtra("secret"));

            if (getConditionDataService.isAlive() == false) {
                Log.d("AutoService", "getConditionData start()");
                getConditionDataService.start();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        getConditionDataService.setRunning(false);
        Log.d("AutoService", "onDestroy()");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("AutoService", "onBind()");
        return null;
    }
}
