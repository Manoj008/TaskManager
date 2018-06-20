package com.manoj.taskmanagertodoapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manoj.taskmanagertodoapp.Adapter.MyAdapter;
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.OnEmptyListener;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class  Today extends Fragment implements OnEmptyListener {

    RecyclerView todayRecycler, overdueRecycler;
    View todayFrag = null;
    List<Details> details;
    List<Details> todayList, overdueList;
    TextView today, overdue, remain;

    public Today() {
        // Required empty public constructor
    }

    public static Today newInstance() {
        Today fragment = new Today();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        todayFrag = inflater.inflate(R.layout.fragment_today, container, false);

        remain = todayFrag.findViewById(R.id.remain);
        today = todayFrag.findViewById(R.id.today);
        overdue = todayFrag.findViewById(R.id.overdue);


        todayRecycler = todayFrag.findViewById(R.id.todayRecycler);
        todayRecycler.hasFixedSize();
        todayRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));

        overdueRecycler = todayFrag.findViewById(R.id.overdueRecycler);
        overdueRecycler.hasFixedSize();
        overdueRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));

        details = new ArrayList<>();
        todayList = new ArrayList<>();
        overdueList = new ArrayList<>();
        DatabaseTask db = new DatabaseTask(getContext());
        details = db.getAllEvent();


        Date date = new Date();
        String dateString = date.toString().substring(0, 3).concat(",").concat(date.toString().substring(3, 10)).concat(",")
                .concat(date.toString().substring(date.toString().length() - 5, date.toString().length()));


        //getting today's task
        for (Details det : details) {
            String string = det.getDate().toString().substring(0, 17);
            if (dateString.equalsIgnoreCase(string))
                todayList.add(det);
        }

        //sorting the time
        for (int i = todayList.size() - 1; i > 0; i--) {
            for (int j = todayList.size() - 1; j > 0; j--) {
                Date oldTime = new Date(todayList.get(j).getDate());
                Date newTime = new Date(todayList.get(j - 1).getDate());
                if (oldTime.compareTo(newTime) < 0) {
                    Date tempDate = oldTime;
                    oldTime = newTime;
                    newTime = tempDate;
                    Details tempToday = todayList.get(j);
                    todayList.set(j, todayList.get(j - 1));
                    todayList.set(j - 1, tempToday);

                }
            }
        }


        //getting pending tasks and naming today of date
        Date d1 = new Date();
        long nowTime = d1.getTime();

        for (Details det : todayList) {
            Date d2 = new Date(det.getDate());
            long todoTime = d2.getTime();
            if (todoTime < nowTime) {
                det.setTask(det.getTask() + "(PENDING)");
                overdueList.add(det);
            }
            det.setDate("Today " + det.getDate().substring(18, det.getDate().length()));
        }

       ifEmpty();

        todayList.removeAll(overdueList);
        MyAdapter todayAdapter = new MyAdapter(getContext(), todayList);
        MyAdapter overdueAdapter = new MyAdapter(getContext(), overdueList);
        todayAdapter.setListener(this);
        todayRecycler.setAdapter(todayAdapter);
        overdueRecycler.setAdapter(overdueAdapter);

        // Inflate the layout for this fragment
        return todayFrag;
    }

    @Override
    public void onEmpty() {
        ifEmpty();
    }

    private void ifEmpty() {
        if (todayList.isEmpty()) {
            today.setVisibility(View.GONE);
            todayRecycler.setVisibility(View.GONE);
        }
        if (overdueList.isEmpty()) {
            overdue.setVisibility(View.GONE);
            overdueRecycler.setVisibility(View.GONE);
        }
        if(todayList.isEmpty() && overdueList.isEmpty()){
            remain.setVisibility(View.VISIBLE);
        }
    }
}
