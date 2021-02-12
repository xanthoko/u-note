package com.uni.notes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

//δεκτης του σηματος για ειδοποιηση. Δημιοιυργει την ειδοποιηση
public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context , Intent intent){
        String subName = intent.getStringExtra("name_of_sub");
        int code = intent.getIntExtra("code" , -1);

        intent = new Intent(context , DatesActivity.class);
        intent.putExtra("receiver", subName);         //στελνει το ονομα του μαθηματος

        PendingIntent pIntent = PendingIntent.getActivity(context , code , intent , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_social_notifications_none)
                .setContentIntent(pIntent)
                .setContentTitle("u-note")
                .setContentText("Σημερινές σημειώσεις απο ("+ subName +") ?");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(code, mBuilder.build());
    }
}
