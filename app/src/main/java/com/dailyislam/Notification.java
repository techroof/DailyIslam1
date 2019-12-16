package com.dailyislam;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.widget.RemoteViews;

public class Notification extends ContextWrapper {
    public static final String CHANNEL_ID = "com.dailyislam",CHANNEL_NAME="dailyislam";
    private NotificationManager notificationManager;


    public Notification(Context base) {
        super(base);
        createChannel();
    }
    private void createChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            channel.enableVibration(false);
            getManager().createNotificationChannel(channel);
        }


    }
        public NotificationManager getManager(){
        if (notificationManager==null)
            notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
        }

        public android.app.Notification.Builder getChannelNotification(RemoteViews views){

                return new android.app.Notification.Builder(getApplicationContext(),CHANNEL_ID)
                       // .setContentText(body)
                        //.setContentTitle(title)
                        .setContent(views)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setAutoCancel(true);

        }
}
