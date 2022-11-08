package com.example.villageplanner_teaminfiniteloop;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlertNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent ){

        PendingIntent pIntent = PendingIntent.getActivity(
                context,
                0,
                new Intent( context, Reminder.class ),
                0
        );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( context );

        mBuilder.setSmallIcon( R.drawable.notification_icon )
                .setContentTitle( "title" )
                .setContentText( "date" )
                .setDefaults( NotificationCompat.DEFAULT_SOUND )
                .setContentIntent( pIntent );

        NotificationManager mNotificationManager = ( NotificationManager )
                context.getSystemService( Context.NOTIFICATION_SERVICE );

        Log.d( "alarm", "true" );

        mNotificationManager.notify( 0, mBuilder.build() );

    }
}
