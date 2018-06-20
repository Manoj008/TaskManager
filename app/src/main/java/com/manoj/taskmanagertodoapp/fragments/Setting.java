package com.manoj.taskmanagertodoapp.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.Nullable;
import android.util.Log;

import com.manoj.taskmanagertodoapp.Main2Activity;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.Reciever;

/**
 * Created by MANOJ on 13-Jun-18.
 */

public class Setting extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);

    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        RingtonePreference ringtonePreference = (RingtonePreference) findPreference("tone");
        String uri = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("tone"
                , String.valueOf(RingtoneManager.getActualDefaultRingtoneUri(getActivity(), Notification.DEFAULT_SOUND)));
        int start = uri.lastIndexOf('/') + 1;
        int end = uri.lastIndexOf('.');
        Log.e("exccccc", uri);
        if (uri.length() > 1)
            ringtonePreference.setSummary(uri.substring(start));
        else {
            ringtonePreference.setSummary("Silent");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e("exccc", "status");

        sp = getActivity().getSharedPreferences("daily", Context.MODE_PRIVATE);

        if (key.equals("day_summary")) {
            boolean dailySummary = sharedPreferences.getBoolean(key, false);
            if (dailySummary && !sharedPreferences.getBoolean("status", false)) {
                Log.e("excccc", "day summary");
                Reciever.dailyAlarm(getActivity());
            } else if (!dailySummary) {
                Log.e("excccc", "day cancel");
                Reciever.cancelReminder(getActivity());
            }
        }

        if (key.equals("status")) {

//            int count = 0;
//            Log.e("exccc", "inside daily");
//            DatabaseTask databaseTask = new DatabaseTask(getActivity());
//            Calendar c = Calendar.getInstance();
//            Calendar cc = Calendar.getInstance();
//            for (Details d : databaseTask.getAllEvent()) {
//                Date date = new Date(d.getDate());
//                cc.setTime(date);
//                if (c.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) && c.get(Calendar.HOUR_OF_DAY) >= cc.get(Calendar.HOUR_OF_DAY)) {
//                    count++;
//                }
//            }
//
//
//            Intent intentHome = new Intent(getActivity(), Main2Activity.class);
//            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntentHome = PendingIntent.getActivity(getActivity(), 0, intentHome, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Intent intentAdd = new Intent(getActivity(), AddEvent.class);
//            intentAdd.putExtra("fragment", "task");
//            intentAdd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntentAdd = PendingIntent.getActivity(getActivity(), 1, intentAdd, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Intent intentSetting = new Intent(getActivity(), AddEvent.class);
//            intentSetting.putExtra("fragment", "setting");
//            intentSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntentSetting = PendingIntent.getActivity(getActivity(), 2, intentSetting, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            RemoteViews views = new RemoteViews(getActivity().getPackageName(), R.layout.custom);
//            views.setImageViewResource(R.id.cust_icon, R.drawable.todo);
//            views.setOnClickPendingIntent(R.id.cust_add, pendingIntentAdd);
//            views.setTextViewText(R.id.cust_text, "" + count + " Tasks Today");
//            views.setOnClickPendingIntent(R.id.cust_setting, pendingIntentSetting);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
//            builder.setContentTitle("status").setContentText("ongoing")
//                    .setContentIntent(pendingIntentHome)
//                    .setCustomContentView(views)
//                    .setPriority(Notification.PRIORITY_MAX)
//                    .setSmallIcon(R.drawable.todo);
//            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            if (sharedPreferences.getBoolean("status", false)) {
                Main2Activity.updateStatus(getActivity());
                Log.e("excccc", "day cancel");
                Reciever.cancelReminder(getActivity());

            } else {
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(32000);
                boolean dailySummary = sharedPreferences.getBoolean(key, false);
                if (dailySummary) {
                    Log.e("excccc", "day summary");
                    Reciever.dailyAlarm(getActivity());
                }
            }

        }
    }
}
