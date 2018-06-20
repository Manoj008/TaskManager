package com.manoj.taskmanagertodoapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.Adapter.MyTaskAdapter;
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AllBdays extends Fragment {

    RecyclerView allBdayRecycler;
    MyTaskAdapter adapter;
    List<Details> details;
    List<Details> rest, sorted;
    TextView remain;


    public AllBdays() {
        // Required empty public constructor
    }

    public static AllBdays newInstance() {
        AllBdays fragment = new AllBdays();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_bdays, container, false);
        // Inflate the layout for this fragment

        DatabaseTask db = new DatabaseTask(getContext());

        remain = view.findViewById(R.id.remain);
        details = db.getAllBday();
        rest = new ArrayList<>();
        sorted = new ArrayList<>();

        if (details.isEmpty()) {
            remain.setVisibility(View.VISIBLE);
        }


        allBdayRecycler = view.findViewById(R.id.allBdayRecycler);
        allBdayRecycler.hasFixedSize();
        allBdayRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Date d1 = new Date();
        int todayDate = d1.getDate();
        int todayMonth = d1.getMonth();


        //sorting the task according to the date
        for (int i = details.size() - 1; i > 0; i--) {
            for (int j = details.size() - 1; j > 0; j--) {
                Date oldTime = new Date(details.get(j).getDate());
                Date newTime = new Date(details.get(j - 1).getDate());
                if (oldTime.compareTo(newTime) < 0) {
                    Date tempDate = oldTime;
                    oldTime = newTime;
                    newTime = tempDate;
                    Details tempdetails = details.get(j);
                    details.set(j, details.get(j - 1));
                    details.set(j - 1, tempdetails);

                }
            }
        }


        //assigning pending and today tomorrow and yesterday of date
        for (Details det : details) {
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date(det.getDate()));
            int diff = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);

            if (diff == 0) {
                det.setDate("Today");
                rest.add(det);
            } else if (diff == 1 || (diff == -364 || diff == -365)) {
                det.setDate("YESTERDAY");
                rest.add(det);
            } else if (diff == -1 || diff == 364 || diff == 365) {
                det.setDate("TOMORROW");
                rest.add(det);
            }

        }

        details.removeAll(rest);
        sorted.addAll(rest);
        sorted.addAll(details);

        adapter = new MyTaskAdapter(getContext(), sorted);
        allBdayRecycler.setAdapter(adapter);

        return view;
    }

}
