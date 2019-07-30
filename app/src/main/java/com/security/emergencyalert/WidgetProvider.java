package com.security.emergencyalert;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Created by Meghana Mokashi
 * Copyright (c) 2018. All rights reserved.
 */
public class WidgetProvider extends AppWidgetProvider {
    public static String ACTION_WIDGET_CLICK = "ActionWidgetClick";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId: appWidgetIds) {
            callOnEmergency(context, appWidgetManager, widgetId);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_WIDGET_CLICK)) {
            Uri alarmType = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            MediaPlayer mp = MediaPlayer.create(context, alarmType);
            mp.start();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mp.stop();

            SharedPreferences preferences = context.getSharedPreferences(MainActivity.PREFERENCE, Context.MODE_PRIVATE);
            String number = preferences.getString(MainActivity.NUMBER, "");
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel: " + number));
            context.startActivity(callIntent);
        }
    }

    private void callOnEmergency(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(ACTION_WIDGET_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}
