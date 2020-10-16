package com.app.erldriver;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.app.erldriver.model.entity.info.FcmData;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.utilities.utils.StringHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Date;


public class FCMInstanceIDService extends FirebaseMessagingService {
    FcmData data = null;
    Context mContext;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mContext = this;
        JSONObject obj = new JSONObject(remoteMessage.getData());
        Log.e("test", "DATA:" + obj.toString());
        try {
            data = new FcmData();
            data.setNotification_type(remoteMessage.getData().get("notification_type"));
            data.setUser_id(remoteMessage.getData().get("user_id"));
            data.setBody(remoteMessage.getData().get("body"));
            data.setIcon(remoteMessage.getData().get("icon"));
            data.setType(remoteMessage.getData().get("type"));
            data.setTimestamp(remoteMessage.getData().get("timestamp"));
            data.setSound(remoteMessage.getData().get("sound"));
            data.setTitle(remoteMessage.getData().get("title"));

            if (!StringHelper.isEmpty(data.getNotification_type())
                    && data.getNotification_type().equals("5000")) {
                if (!AppConstant.isOpenChatScreen)
                    sendNotification(data);
                Intent chatIntent = new Intent(AppConstant.Action.UPDATE_CHAT_DATA);
//                chatIntent.putExtra(AppConstant.IntentKey.LAST_MESSAGE_ID, AppConstant.Action.UPDATE_CHAT_DATA);
                chatIntent.putExtra("action", AppConstant.Action.UPDATE_CHAT_DATA);
                this.sendBroadcast(chatIntent);
            } else {
                sendNotification(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(FcmData data) {
        String CHANNEL_ID = getResources().getString(R.string.CHANNEL_ID);
        PendingIntent pendingIntent = redirectToActivity(data);
        if (pendingIntent != null) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_app_white_120)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(Html.fromHtml(data.getTitle()))
//                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(Html.fromHtml(data.getBody()))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(data.getBody())))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        AppConstant.EXTRA_CHANNEL_SID,
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
        }
    }

    private PendingIntent redirectToActivity(FcmData data) {
        Intent intent = null;
        if (AppUtils.getUserPrefrence(FCMInstanceIDService.this) == null) {
            Log.e("test", "3");
            intent = new Intent("com.app.erldriver.view.activity.LoginActivity");
        } else {
            intent = AppUtils.getFcmIntent(data);
        }

        if (intent != null) {
            intent.putExtra(AppConstant.IntentKey.IS_FROM_NOTIFICATION, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            return pendingIntent;
        }
        return null;
    }


}
