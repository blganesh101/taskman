package com.blganesh.taskman.timers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.blganesh.taskman.R;
import com.blganesh.taskman.activities.TaskTimerActivity;
import com.blganesh.taskman.util.TimeActivityPresenceIndicator;


public final class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_TIME_UP = "com.bastienleonard.tomate.actions.TIME_UP";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TimeActivityPresenceIndicator.isOpen(context)) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifIntent = new Intent(context, TaskTimerActivity.class);
        notifIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifIntent.putExtra(TaskTimerActivity.EXTRA_SKIP_NEXT_SOUND, true);
        PendingIntent notifPendingIntent = PendingIntent.getActivity(context,
                0,
                notifIntent,
                PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentTitle(context.getString(R.string.timer_notification_title))
                .setContentText(context.getString(R.string.timer_notification_text))
                .setContentIntent(notifPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
}
