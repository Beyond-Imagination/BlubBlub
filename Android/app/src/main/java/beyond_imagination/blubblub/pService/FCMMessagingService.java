package beyond_imagination.blubblub.pService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;

/**
 * Created by laggu on 2017-07-24.
 */
/**
 * @file FCMMessagingService.java
 * @breif
 * Class about FCM service
 * If you receive FCM message, onMessageReceived() execute. and handle FCM message according to message type
 * @author Yehun Park
 */
public class FCMMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // FCM console을 사용해서 메시지를 보내면, 포그라운드시에만 함수가 실행되고 백그라운드시에는 함수가 실행되지않는다.
    // 따라서, 따로 FCM api를 직접 실행시켜줘야한다. 그러면 해당 함수 사용가능.
    // 참고 : http://trandent.com/board/Android/detail/744
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.d("FCMMessagingService", "onMessageReceived 실행");

        if (remoteMessage.getData().get("type").equals("대화") == false) {
            receiveNotification(remoteMessage.getData().get("body"));
        }
        receiveData(remoteMessage.getData().get("type"), remoteMessage.getData().get("body"));
    }

    private void receiveNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
        );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.main_icon)
                .setContentTitle("상태를 확인해주세요!")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    // 어떤 상태이상인지 확인.
    private void receiveData(String type, String body) {
        ((MainActivity) MainActivity.getInstance()).onControlMessage(type, body);

        // check how many FCM message("먹이") accumulate.
        if (type.equals("먹이"))
            ((MainActivity) MainActivity.getInstance()).countAccumulate();
    }
}
