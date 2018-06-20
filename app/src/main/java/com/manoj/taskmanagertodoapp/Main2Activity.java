package com.manoj.taskmanagertodoapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;
import com.manoj.taskmanagertodoapp.fragments.AllBdays;
import com.manoj.taskmanagertodoapp.fragments.AllNotes;
import com.manoj.taskmanagertodoapp.fragments.Finished;
import com.manoj.taskmanagertodoapp.fragments.Today;
import com.manoj.taskmanagertodoapp.fragments.Week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    Spinner dropDown;
    BottomNavigationView bnv;
    int id = -1;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Task Manager");
        setSupportActionBar(toolbar);

        final SharedPreferences sp = getSharedPreferences("permission", MODE_PRIVATE);
        boolean permission = sp.getBoolean("permission", false);

        if (!permission) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Allow AutoStart for Task Manager to work it properly");
            builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addAutoStartup();
                    sp.edit().putBoolean("permission", true).commit();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        dropDown = findViewById(R.id.dropDown);
        updateStatus(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bnv = findViewById(R.id.bnv);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()) {

                    case R.id.eventMenu:
                        selected = Week.newInstance();
                        dropDown.setSelection(0);
                        break;

                    case R.id.birthdayMenu:
                        selected = AllBdays.newInstance();
                        break;
                    case R.id.notesMenu:
                        selected = AllNotes.newInstance();
                        break;
                }

                dropDown.setVisibility(View.GONE);

                if (selected instanceof Week) {
                    dropDown.setVisibility(View.VISIBLE);
                }

                Log.w("excccc", "indise");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, selected);
                transaction.commit();
                return true;
            }
        });

        setFrag(bundle);


        List<String> taskList = new ArrayList<>();
        taskList.add("All Task");
        taskList.add("Today");
        taskList.add("finished");

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, taskList);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Fragment selectedFragment = null;
        switch (position) {
            case 0:
                selectedFragment = Week.newInstance();
                break;
            case 1:
                selectedFragment = Today.newInstance();
                break;
            case 2:
                selectedFragment = Finished.newInstance();

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag, selectedFragment);
        if (bundle != null)
            return;
        transaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setFrag(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFrag(bundle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addTaskMenu) {
            Intent intent = new Intent(this, AddEvent.class);
            intent.putExtra("fragment", "task");
            intent.putExtra("id", -1);
            startActivity(intent);
        } else if (id == R.id.addBdayMenu) {
            Intent intent = new Intent(this, AddEvent.class);
            intent.putExtra("fragment", "bday");
            intent.putExtra("id", -1);
            startActivity(intent);
        } else if (id == R.id.addNoteMenu) {
            Intent intent = new Intent(this, AddEvent.class);
            intent.putExtra("fragment", "note");
            intent.putExtra("id", -1);
            startActivity(intent);
        } else if (id == R.id.tools) {
            Intent intent = new Intent(this, AddEvent.class);
            intent.putExtra("fragment", "setting");
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void setFrag(Bundle bundle) {
        if (bundle != null) {
            Fragment fragment;
            String frag = bundle.getString("fragment");
            Log.e("excccc", frag);
            dropDown.setVisibility(View.GONE);
            if (frag.equals("note")) {
                fragment = AllNotes.newInstance();
                bnv.setSelectedItemId(R.id.notesMenu);
            } else if (frag.equals("bday")) {
                fragment = AllBdays.newInstance();
                bnv.setSelectedItemId(R.id.birthdayMenu);
            } else {
                fragment = Week.newInstance();
                bnv.setSelectedItemId(R.id.eventMenu);
                dropDown.setVisibility(View.VISIBLE);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frag, fragment);
            transaction.commit();
        }

    }

    public static void updateStatus(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean stat = preferences.getBoolean("status", true);
        boolean pendingBoolean = false;
        int pending = 0;
        if (stat) {

            boolean bdayy = false;
            String name = "";
            int count = 0;
            Log.e("exccc", "inside daily");
            DatabaseTask databaseTask = new DatabaseTask(context);
            Calendar c = Calendar.getInstance();
            Calendar cc = Calendar.getInstance();

            for (Details d : databaseTask.getAllBday()) {
                Date date = new Date(d.getDate());
                cc.setTime(date);
                if (c.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR)) {
                    Log.e("exccc", "inside bday" + d.getTask());
                    bdayy = true;
                    name = d.getTask();
                }
            }

            for (Details d : databaseTask.getAllEvent()) {
                Date date = new Date(d.getDate());
                cc.setTime(date);
                if (c.getTime().after(cc.getTime()) || c.getTime().equals(cc.getTime())) {
                    pendingBoolean = true;
                    pending++;
                }
                if (c.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) && c.get(Calendar.HOUR_OF_DAY) <= cc.get(Calendar.HOUR_OF_DAY)) {
                    if (c.get(Calendar.HOUR_OF_DAY) == cc.get(Calendar.HOUR_OF_DAY)) {
                        if (c.get(Calendar.MINUTE) < cc.get(Calendar.MINUTE)) {
                            count++;
                        }
                    } else {
                        count++;
                    }
                }
            }

            Intent intentHome = new Intent(context, Main2Activity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntentHome = PendingIntent.getActivity(context, 0, intentHome, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentAdd = new Intent(context, AddEvent.class);
            intentAdd.putExtra("fragment", "task");
            intentAdd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntentAdd = PendingIntent.getActivity(context, 1, intentAdd, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentSetting = new Intent(context, AddEvent.class);
            intentSetting.putExtra("fragment", "setting");
            intentSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntentSetting = PendingIntent.getActivity(context, 2, intentSetting, PendingIntent.FLAG_UPDATE_CURRENT);


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.custom);
            views.setImageViewResource(R.id.cust_icon, R.drawable.todo);
            if (pendingBoolean) {
                views.setTextViewText(R.id.cust_text, "" + pending + " Tasks Pending");
            } else {
                views.setTextViewText(R.id.cust_text, "" + count + " Tasks Today");
            }
            views.setOnClickPendingIntent(R.id.cust_add, pendingIntentAdd);
            views.setOnClickPendingIntent(R.id.cust_setting, pendingIntentSetting);
            if (pendingBoolean) {
                views.setViewVisibility(R.id.cust_count, View.VISIBLE);
                views.setTextViewText(R.id.cust_count, "" + pending);
            }
            else if (count > 0) {
                views.setViewVisibility(R.id.cust_count, View.VISIBLE);
                views.setTextViewText(R.id.cust_count, "" + count);
            }
            else {
                views.setViewVisibility(R.id.cust_count, View.GONE);
            }

            if (bdayy) {
                views.setTextViewText(R.id.cust_title, "Today is " + name + " Bday");
            } else {
                views.setTextViewText(R.id.cust_title, "Task Manager");
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentText("" + count + " Tasks today")
                    .setContentTitle("Task Manager")
                    .setContentIntent(pendingIntentHome)
                    .setCustomContentView(views)
                    .setContent(views)
                    .setSmallIcon(R.drawable.todo);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            builder.setOngoing(true);
            Notification notification = builder.build();
            notificationManager.notify(32000, notification);

        }
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }
}
