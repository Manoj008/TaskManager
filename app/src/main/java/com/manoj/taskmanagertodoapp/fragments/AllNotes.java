package com.manoj.taskmanagertodoapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.Adapter.NotesAdapter;
import com.manoj.taskmanagertodoapp.Model.Diary;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllNotes extends Fragment {

    RecyclerView notesREcycler;
    NotesAdapter adapter;
    List<Diary> notes;
    TextView remain;

    public AllNotes() {
        // Required empty public constructor
    }

    public static AllNotes newInstance() {
        AllNotes fragment = new AllNotes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all_notes, container, false);
        remain=view.findViewById(R.id.remain);

        notesREcycler=view.findViewById(R.id.notesRecycler);
        notesREcycler.hasFixedSize();
        notesREcycler.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseTask db=new DatabaseTask(getContext());

        notes=db.allNotes();
        if(notes.isEmpty()){
            remain.setVisibility(View.VISIBLE);
        }
        for (Diary det : notes) {
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date(det.getDate()));
            int diff = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);

            if (diff == 0) {
                det.setDate("Today");
            } else if (diff == 1 || (diff == -364 || diff == -365)) {
                det.setDate("YESTERDAY");
            } else if (diff == -1 || diff == 364 || diff == 365) {
                det.setDate("TOMORROW");
            }

        }

        adapter=new NotesAdapter(getContext(),notes);
        notesREcycler.setAdapter(adapter);

        return view;
    }
}
