package com.manoj.taskmanagertodoapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by MANOJ on 05-Jun-18.
 */

public class Reciever extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    static String title, msg;
    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Trigger the notification
        Main2Activity.updateStatus(context);
        Log.e("excc", "alarm Recieved");
        setNotification(context, Main2Activity.class, title, msg);
        if (intent.getAction() != null) {
            if (intent.getAction().equals("true")) {
                int count = 0;
                Log.e("exccc", "inside daily reciever");
                DatabaseTask databaseTask = new DatabaseTask(context);
                Calendar c = Calendar.getInstance();
                Calendar cc = Calendar.getInstance();

                for (Details d : databaseTask.getAllEvent()) {
                    Date date = new Date(d.getDate());
                    cc.setTime(date);
                    if (c.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) && c.get(Calendar.HOUR_OF_DAY) <= cc.get(Calendar.HOUR_OF_DAY)) {
                        if(c.get(Calendar.HOUR_OF_DAY) == cc.get(Calendar.HOUR_OF_DAY)){
                            if(c.get(Calendar.MINUTE)<cc.get(Calendar.MINUTE)){
                                count++;
                            }
                        }
                        else{
                            count++;
                        }
                    }
                }

                Intent in = new Intent(context, Main2Activity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 31200, in, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new NotificationCompat.Builder(context)
                        .setContentTitle("Today's Task")
                        .setContentText("" + count + " tasks to do")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.todo).build();

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(31200, notification);

            }

        }

    }

    private void setNotification(Context context, Class<Main2Activity> cls, String title, String msg) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(new Date());

        SimpleDateFormat not = new SimpleDateFormat("HH:mm");
        SimpleDateFormat notic = new SimpleDateFormat("hh:mm a");

        DatabaseTask databaseTask = new DatabaseTask(context);
        List<Details> allTasks = databaseTask.getAllEvent();


        for (Details detail : allTasks) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(detail.getDate()));
            Calendar cnew = Calendar.getInstance();
            cnew.setTime(new Date(detail.getDate()));
            Calendar cl = Calendar.getInstance();
            int ring = detail.getRing();

            Log.w("excc", "outside task" + ring);
            Log.w("exccdate", "" + c.getTime());
            Log.w("exccdate1", "" + cl.getTime());
            Log.w("exccdateffdgfs", "" + detail.getDate());

            switch (Integer.valueOf(detail.getReminder())) {
                case 0:
                    break;
                case 1:
                    cnew.add(Calendar.MINUTE, -5);
                    break;
                case 2:
                    cnew.add(Calendar.MINUTE, -15);
                    break;
                case 3:
                    cnew.add(Calendar.MINUTE, -30);
                    break;
                case 4:
                    cnew.add(Calendar.HOUR_OF_DAY, -1);
                    break;
                case 5:
                    cnew.add(Calendar.HOUR_OF_DAY, -2);
                    break;
                case 6:
                    cnew.add(Calendar.HOUR_OF_DAY, -3);
                    break;
                case 7:
                    cnew.add(Calendar.HOUR_OF_DAY, -5);
                    break;
            }

            Log.w("exccdate", "" + c.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, hh:mm a");
            String c1String = sdf.format(cl.getTime());
            String cnewString = sdf.format(cnew.getTime());
            String cString = sdf.format(c.getTime());


            if (c1String.equals(cString) || (cl.getTime().after(c.getTime())) && ring < 2) {
                Log.e("excaaa", "inside mmmm");
                show(context, title, msg);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM dd, yyyy hh:mm a");
                switch (Integer.parseInt(detail.getRepeat())) {
                    case 0:
                        break;
                    case 1:
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                            cal.add(Calendar.DAY_OF_YEAR, 1);
                        else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                            cal.add(Calendar.DAY_OF_YEAR, 2);
                        break;

                    case 2:
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                            cal.add(Calendar.DAY_OF_YEAR, 2);
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                            cal.add(Calendar.DAY_OF_YEAR, 1);
                        break;

                    case 3:
                        break;

                    case 4:
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case 5:
                        cal.add(Calendar.MONTH, 1);
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                }

                Log.e("excc", "out repeat");
                if (Integer.parseInt(detail.getRepeat()) == 0) {

                } else {
                    Log.e("excc", "inside repeat");
                    Reciever.setReminder(context, Reciever.class, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)
                            , cal.get(Calendar.DAY_OF_MONTH), cl.get(Calendar.HOUR_OF_DAY), cl.get(Calendar.MINUTE), title, msg);
                    databaseTask.addEvent(detail.getTask(), detail.getDescription(), dateFormat2.format(cal.getTime())
                            , detail.getPlace(), detail.getReminder(), detail.getRepeat());

                }
                databaseTask.updateEvent(detail.getId(), detail.getTask(), detail.getDescription(), detail.getDate()
                        , detail.getPlace(), detail.getReminder(), detail.getRepeat(), 2);

            } else if (c1String.equals(cnewString) || (cl.getTime().after(cnew.getTime()) && ring == 0)) {
                Log.w("excc", "inside Minyute");
                Log.w("exccdate", "" + c.getTime());
                show(context, title, msg);
                setReminder(context, Reciever.class
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
                        , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), title, msg);
                databaseTask.updateEvent(detail.getId(), detail.getTask(), detail.getDescription(), detail.getDate()
                        , detail.getPlace(), detail.getReminder(), detail.getRepeat(), 1);
            }
        }

        Calendar tom = Calendar.getInstance();
        tom.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrow = dateFormat.format(tom.getTime());

        List<Details> bdays = databaseTask.getAllBday();
        for (Details detail : bdays) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(detail.getDate()));
            Calendar cnew = Calendar.getInstance();
            cnew.setTime(new Date(detail.getDate()));
            String date = dateFormat.format(c.getTime());
            Log.w("excc", "outside bday");
            Log.w("exccmin", "" + tomorrow + ":" + date);
            Log.w("exccdate", "" + c.getTime());
            Calendar cl = Calendar.getInstance();
            int ring = detail.getRing();
            int remind = Integer.parseInt(detail.getReminder());
            switch (remind) {
                case 0:
                    cnew.add(Calendar.MINUTE, -5);
                    break;
                case 1:
                    cnew.add(Calendar.MINUTE, -15);
                    break;
                case 2:
                    cnew.add(Calendar.MINUTE, -30);
                    break;
                case 3:
                    cnew.add(Calendar.HOUR_OF_DAY, -1);
                    break;
                case 4:
                    cnew.add(Calendar.HOUR_OF_DAY, -2);
                    break;
                case 5:
                    cnew.add(Calendar.DAY_OF_YEAR, -1);
                    break;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, hh:mm a");
            String clString = sdf.format(cl.getTime());
            String cString = sdf.format(c.getTime());
            String cnewString = sdf.format(cnew.getTime());


            if (clString.equals(cString) || (cl.getTime().after(c.getTime())) && ring < 2) {
                Log.e("excccins", "inside bday");
                show(context, title, msg);
                c.add(Calendar.YEAR, 1);
                databaseTask.updateBday(detail.getId(), detail.getTask(), detail.getDate(), detail.getReminder(), 0);
            } else if (clString.equals(cnewString) || (cl.getTime().after(cnew.getTime()) && ring == 0)) {
                c.add(Calendar.MINUTE, -5);
                setReminder(context, Reciever.class, c.get(Calendar.YEAR), c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), title, msg);
                show(context, title, msg);
                databaseTask.updateBday(detail.getId(), detail.getTask(), detail.getDate(), detail.getReminder(), 1);
            }
        }


    }

    private void show(Context context, String title, String msg) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean vibration = preferences.getBoolean("vibration", true);
        boolean status = preferences.getBoolean("status", true);
        Uri uri = Uri.parse(preferences.getString("tone", String.valueOf(RingtoneManager.getActualDefaultRingtoneUri(context, Notification.DEFAULT_SOUND))));

        Log.e("excc", "showing notification");
        SharedPreferences sharedPreferences = context.getSharedPreferences("unique", Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt("id", 30);
        sharedPreferences.edit().putInt("id", id + 1).commit();

        if (id > 3000) {
            sharedPreferences.edit().putInt("id", 0).commit();
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", id);
        intent.setAction(String.valueOf("action" + id));


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setDefaults(Notification.DEFAULT_LIGHTS)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.todo)
                .setSound(uri)
                .setContentTitle(title)
                .setContentText(msg);
        if (vibration) {
            builder.setVibrate(new long[]{100, 1000, 500, 1000, 500, 1000});
        }

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);


    }

    public static void setReminder(Context context, Class<?> cls, int year, int month, int day, int hour, int min
            , String t, String m) {
        Reciever.title = t;
        Reciever.msg = m;
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.YEAR, year);
        setcalendar.set(Calendar.MONTH, month);
        setcalendar.set(Calendar.DAY_OF_MONTH, day);
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 1);
        // cancel already scheduled reminders
        //cancelReminder(context,cls);

        Log.e("excc", "calender " + setcalendar.getTime());
        Log.e("excc", "inside alarm Manager" + Calendar.getInstance().getTime());
        SharedPreferences sharedPreferences = context.getSharedPreferences("unique", Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt("id", 30);

        if (id > 3000) {
            sharedPreferences.edit().putInt("id", 0).commit();
        }
        sharedPreferences.edit().putInt("id", id + 1).commit();

        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("id", id);
        intent1.setAction("action" + id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent1, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), pendingIntent);
    }

    public static void cancelReminder(Context context) {
        // Disable a receiver
        Intent intent1 = new Intent(context, Reciever.class);
        intent1.putExtra("bar", "true");
        intent1.setAction("true");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                31000, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void dailyAlarm(Context context) {
        Log.w("exccc","inside alarm daily");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 01);
        calendar.add(Calendar.DAY_OF_YEAR,+1);
        Intent intent1 = new Intent(context, Reciever.class);
        intent1.putExtra("bar", "true");
        intent1.setAction("true");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 31000, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.e("exccc","ind"+calendar.getTime());

    }
}
