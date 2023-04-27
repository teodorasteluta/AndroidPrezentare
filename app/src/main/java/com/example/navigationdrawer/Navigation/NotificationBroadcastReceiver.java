package com.example.navigationdrawer.Navigation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.navigationdrawer.R;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TITLE_EXTRA = "title_extra";
    public static final String MESSAGE_EXTRA = "message_extra";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(TITLE_EXTRA);
        String message = intent.getStringExtra(MESSAGE_EXTRA);
        if (title != null && message != null) {
            showNotification(context, title, message);
        }
    }

    private void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.google_signin_button);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
