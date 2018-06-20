package com.manoj.taskmanagertodoapp.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.Adapter.MyFinAdapter;
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.Date;
import java.util.List;


public class Finished extends Fragment {

    RecyclerView finishRecycler;
    List<Details> details;
    RecyclerView.Adapter finAdapter;
    TextView remain;
    FloatingActionButton fab;

    public Finished() {

        // Required empty public constructor
    }

    public static Finished newInstance() {
        Finished fragment = new Finished();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_finished, container, false);
        // Inflate the layout for this fragment

        final DatabaseTask db = new DatabaseTask(getContext());

        finishRecycler = view.findViewById(R.id.finishedRecycler);
        finishRecycler.hasFixedSize();
        finishRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        details = db.getAllFinished();
        remain = view.findViewById(R.id.remain);
        fab=view.findViewById(R.id.fab);

        Date d1 = new Date();
        int todayDate = d1.getDate();
        int todayMonth = d1.getMonth();
        int todayYear = d1.getYear();
        long nowTime = d1.getTime();



        //sorting the task according to the time
        for (int i = details.size() - 1; i > 0; i--) {
            for (int j = details.size() - 1; j > 0; j--) {
                Date oldTime = new Date(details.get(j).getDate());
                Date newTime = new Date(details.get(j - 1).getDate());
                if (oldTime.compareTo(newTime) > 0) {
                    Date tempDate = oldTime;
                    oldTime = newTime;
                    newTime = tempDate;
                    Details tempdetails = details.get(j);
                    details.set(j, details.get(j - 1));
                    details.set(j - 1, tempdetails);

                }
            }
        }

        for (Details det : details) {
            Date d2 = new Date(det.getDate());
            int todoDate = d2.getDate();
            int todoMonth = d2.getMonth();
            int todoYear = d2.getYear();
            long todoTime = d2.getTime();

            if (todoDate < todayDate) {
                if (todayDate - todoDate == 1) {
                    det.setDate("YESTERDAY " + det.getDate().substring(18));
                }
            } else if (todoDate == todayDate) {
                if (todoTime < nowTime) {
                    det.setDate("Today " + det.getDate().substring(18, det.getDate().length()));

                } else if (todoTime > nowTime) {
                    det.setDate("Today " + det.getDate().substring(18, det.getDate().length()));
                }


                if (details.isEmpty()) {
                    remain.setVisibility(View.VISIBLE);
                }
            }
        }


        finAdapter = new MyFinAdapter(getContext(), details);
        finishRecycler.setAdapter(finAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRecycler.animate().scaleX(0).scaleY(0).alpha(0).setDuration(2000);
                db.deleleteAllFinished();
            }
        });
        return view;
    }
}
