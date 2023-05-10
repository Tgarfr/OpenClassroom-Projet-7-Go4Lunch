package com.startup.go4lunch.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.startup.go4lunch.R;

public class NotificationWorker extends Worker {

    private static final String NOTIFICATION_TITLE = "G4Lunch : Your restaurant today";
    private static final String NOTIFICATION_ID_CHANNEL = "Notification G4Lunch";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String message = getInputData().getString("message");

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_ID_CHANNEL)
                        .setSmallIcon(R.drawable.icon_notification)
                        .setContentTitle(NOTIFICATION_TITLE)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID_CHANNEL, NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, notification.build());
        }

        return Result.success();
    }
}