package com.manoj.taskmanagertodoapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.manoj.taskmanagertodoapp.fragments.AddBirthday;
import com.manoj.taskmanagertodoapp.fragments.AddNote;
import com.manoj.taskmanagertodoapp.fragments.AddTask;
import com.manoj.taskmanagertodoapp.fragments.Setting;

public class AddEvent extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Bundle bundle = new Bundle();
    int id = -1;
    String fragment = "task";
    boolean addBday = false, addEvent = false;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),AllTask.class));
//            }
//        });


        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id",-1);
            fragment = bundle.getString("fragment");
        }


        if (fragment.equals("setting")) {
            toolbar.setTitle("Setting");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.newEvent,new Setting());
            transaction.commit();
        } else if (fragment.equals("task")) {
            toolbar.setTitle("New Task");
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.newEvent, AddTask.newInstance(id));
            transaction.commit();
            addEvent = true;
            id = -1;
        } else if (fragment.equals("bday")) {
            toolbar.setTitle("New Birthday");
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.newEvent, AddBirthday.newInstance(id));
            transaction.commit();
            addBday = true;
            id = -1;
        } else if (fragment.equals("note")) {
            toolbar.setTitle("New Note");
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.newEvent, AddNote.newInstance(id));
            transaction.commit();
            id = -1;
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Main2Activity.class));
    }
}
