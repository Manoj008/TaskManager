package com.manoj.taskmanagertodoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences sharedPreferences=getSharedPreferences("unique",MODE_PRIVATE);
        sharedPreferences.edit().putInt("id",0).commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        },2000);
    }
}
