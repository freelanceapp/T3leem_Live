package com.t3leem_live.notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.t3leem_live.R;
import com.t3leem_live.models.NotFireModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_student.activity_notification.NotificationActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.getInstance();
    private Map<String, String> map;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }
        String notification_type = map.get("notification_type");


        if (notification_type.equals("chat")) {

        } else {
            manageNotification(map);

        }
    }

    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewNotificationVersion(map);
        } else {
            createOldNotificationVersion(map);

        }

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);


    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNewNotificationVersion(Map<String, String> map) {
        String notification_type = map.get("notification_type");

        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);

        String title = "";
        String body = "";

        if (notification_type.equals("chat")) {


        } else if (notification_type.equals("admin_to_users")){

            title = getString(R.string.admin);
            body = map.get("title");

        }
        else if (notification_type.equals("relationship")) {

            String data = map.get("message");
            if (data.equals("new_request")) {
                title = getString(R.string.new_request);
                body = getString(R.string.new_req_parent);

            } else if (data.equals("your_request_accepted")) {
                title = getString(R.string.accept);
                body = getString(R.string.req_accepted);


            } else if (data.equals("your_request_refused")) {

                title = getString(R.string.refuse);
                body = getString(R.string.req_refused);

            }
        }
        else if (notification_type.equals("share_teacher")) {
            title = getString(R.string.share_techer);
            body = map.get("title");
        }
        else if (notification_type.equals("share_center")) {
            title = getString(R.string.share_center);
            body = map.get("title");
        }
        else if (notification_type.equals("live_stream")) {
            title = getString(R.string.live_stream);
            body = map.get("title");
        }
        else if (notification_type.equals("exam")) {
            title = getString(R.string.new_exam);
            body = map.get("title");
        }

        Log.e("title",title+"__");
        Log.e("body",body);

        builder.setContentTitle(title);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        builder.setLargeIcon(bitmap);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {

            manager.createNotificationChannel(channel);
            manager.notify(Tags.not_tag, Tags.not_id, builder.build());
            EventBus.getDefault().post(new NotFireModel(true));


        }



    }

    private void createOldNotificationVersion(Map<String, String> map) {






    }


    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getRoom() {
        return preferences.getRoomId(this);
    }


}
