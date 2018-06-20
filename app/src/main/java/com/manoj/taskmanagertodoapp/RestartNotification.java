package com.manoj.taskmanagertodoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MANOJ on 05-Jun-18.
 */

public class RestartNotification extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    static String title, msg;
    SharedPreferences preferences;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Trigger the notification
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.REBOOT")) {
                Log.e("excc", "boot completed");
                Toast.makeText(context,"BOOT COMPLETED",Toast.LENGTH_LONG);
                setNotificationTime(context);
                Main2Activity.updateStatus(context);
                preferences= PreferenceManager.getDefaultSharedPreferences(context);
                boolean dailySummary =preferences.getBoolean("day_summary", false);
                boolean stat=preferences.getBoolean("status",false);
                if (dailySummary && !stat) {
                    Log.e("excccc", "day summary");
                    Reciever.dailyAlarm(context);
                }
                return;
            }
        }

    }

    private void setNotificationTime(Context context) {

        DatabaseTask databaseTask = new DatabaseTask(context);
        List<Details> allTasks = databaseTask.getAllEvent();


        for (Details detail : allTasks) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(detail.getDate()));
            Calendar cl = Calendar.getInstance();
            if (cl.getTime().before(c.getTime())) {
                Log.w("excc", "outside task");
                Log.w("exccdate", "" + c.getTime());
                switch (Integer.valueOf(detail.getReminder())) {
                    case 1:
                        c.add(Calendar.MINUTE, -5);
                        break;
                    case 2:
                        c.add(Calendar.MINUTE, -15);
                        break;
                    case 3:
                        c.add(Calendar.MINUTE, -30);
                        break;
                    case 4:
                        c.add(Calendar.HOUR_OF_DAY, -1);
                        break;
                    case 5:
                        c.add(Calendar.HOUR_OF_DAY, -2);
                        break;
                    case 6:
                        c.add(Calendar.HOUR_OF_DAY, -3);
                        break;
                    case 7:
                        c.add(Calendar.HOUR_OF_DAY, -5);
                        break;
                }

                Reciever.setReminder(context, Reciever.class
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY)
                        , c.get(Calendar.MINUTE), title, msg);

                databaseTask.updateEvent(detail.getId(), detail.getTask(), detail.getDescription(), detail.getDate()
                        , detail.getPlace(), detail.getReminder(), detail.getRepeat(), 0);

                Log.w("excc", "inside");
                Log.w("exccdate", "" + c.getTime());
            }
        }

        List<Details> bdays = databaseTask.getAllBday();

        for (Details detail : bdays) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(detail.getDate()));
            Calendar cl=Calendar.getInstance();

            if (cl.getTime().before(c.getTime())) {

                int remind = Integer.parseInt(detail.getReminder());
                switch (remind) {
                    case 0:
                        c.add(Calendar.MINUTE,-5);
                        break;
                    case 1:
                        c.add(Calendar.MINUTE,-15);
                        break;
                    case 2:
                        c.add(Calendar.MINUTE,-30);
                        break;
                    case 3:
                        c.add(Calendar.HOUR_OF_DAY,-1);
                        break;
                    case 4:
                        c.add(Calendar.HOUR_OF_DAY,-2);
                        break;
                    case 5:
                        c.add(Calendar.DAY_OF_YEAR,-1);
                        break;
                }
                Reciever.setReminder(context, Reciever.class
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY)
                        , c.get(Calendar.MINUTE), title, msg);
                databaseTask.updateBday(detail.getId(), detail.getTask(), detail.getDate(), detail.getReminder(),0);

            }
        }
    }
}
