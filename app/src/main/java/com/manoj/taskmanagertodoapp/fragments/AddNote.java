package com.manoj.taskmanagertodoapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.manoj.taskmanagertodoapp.Main2Activity;
import com.manoj.taskmanagertodoapp.Model.Diary;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.Date;

public class AddNote extends Fragment {

    private static int id=-1;
    EditText name,note;
    FloatingActionButton save;
    DatabaseTask db;
    CheckBox delNote;
    LinearLayout linear;

    public AddNote() {
        // Required empty public constructor
    }


    public static AddNote newInstance(int id) {
        AddNote fragment = new AddNote();
        Bundle args = new Bundle();
        args.putInt("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_note, container, false);

        db=new DatabaseTask(getContext());
        name=view.findViewById(R.id.name);
        note=view.findViewById(R.id.note);
        save=view.findViewById(R.id.save);
        delNote=view.findViewById(R.id.delNote);
        linear=view.findViewById(R.id.linear);


        if(id>=0){
            linear.setVisibility(View.VISIBLE);
            Log.w("excc",""+id);
            Diary diary=db.getNote(id);
            name.setText(diary.getName());
            note.setText(diary.getNote());
        }

        delNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linear.animate().translationX(3000).setDuration(1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseTask db = new DatabaseTask(getContext());
                            db.deleteNote(id);
                            gotoBday();
                        }
                    }, 600);

                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date date=new Date();
                String dateStr=date.toString().substring(0,11).concat(date.toString().substring(date.toString().length()-5));

                if(id>=0){
                    db.updateNote(id,name.getText().toString(),note.getText().toString(),db.getNote(id).getDate());
                }
                else {
                    db.addNote(name.getText().toString(), note.getText().toString(), dateStr);
                }
                gotoBday();
            }
        });


        return view;
    }

    private void gotoBday() {
        Intent intent=new Intent(getContext(),Main2Activity.class);
        intent.putExtra("fragment","note");
        startActivity(intent);
    }

}
