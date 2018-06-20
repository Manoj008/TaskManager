package com.manoj.taskmanagertodoapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Week extends Fragment implements OnEmptyListener{

    List<Details> details, overdueList, todayList, tomorrowList, thisWeekList, nextWeekList, thisMonthList, nextMonthList, restList, toDeleteList;
    RecyclerView overdueRecycler, todayRecycler, tomorrowRecycler, thisWeekRecycler, nextWeekRecycler, thisMonthRecycler, nextMonthRecycler, thisYearRecycler, restRecycler;
    MyAdapter overdueAdapter, todayAdapter, tomorrowAdapter, thisWeekAdapter, nextWeekAdapter, thisMonthAdapter, nextMonthAdapter, restAdapter;
    View view;
    TextView remain, overdue, today, tomorrow, thisWeek, nextWeek, thisMonth, nextMonth, rest;



    public Week() {
        // Required empty public constructor
    }

    public void empty(){

        Log.e("excc","wewe");
    }


    public static Week newInstance() {
        Week fragment = new Week();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        DatabaseTask db = new DatabaseTask(getContext());
        view = inflater.inflate(R.layout.fragment_week, container, false);
        // Inflate the layout for this fragment

        remain = view.findViewById(R.id.remain);
        remain.setVisibility(View.GONE);

        overdue = view.findViewById(R.id.overdue);
        today = view.findViewById(R.id.today);
        tomorrow = view.findViewById(R.id.tomorrow);
        thisWeek = view.findViewById(R.id.thisWeek);
        nextWeek = view.findViewById(R.id.nextWeek);
        thisMonth = view.findViewById(R.id.thisMonth);
        nextMonth = view.findViewById(R.id.nextMonth);
        rest = view.findViewById(R.id.rest);

        overdueRecycler = view.findViewById(R.id.overdueRecycler);
        overdueRecycler.hasFixedSize();
        overdueRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        todayRecycler = view.findViewById(R.id.todayRecycler);
        todayRecycler.hasFixedSize();
        todayRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        tomorrowRecycler = view.findViewById(R.id.tomorrowRecycler);
        tomorrowRecycler.hasFixedSize();
        tomorrowRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        thisWeekRecycler = view.findViewById(R.id.thisWeekRecycler);
        thisWeekRecycler.hasFixedSize();
        thisWeekRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        nextWeekRecycler = view.findViewById(R.id.nextWeekRecycler);
        nextWeekRecycler.hasFixedSize();
        nextWeekRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        thisMonthRecycler = view.findViewById(R.id.thisMonthRecycler);
        thisMonthRecycler.hasFixedSize();
        thisMonthRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        nextMonthRecycler = view.findViewById(R.id.nextMonthRecycler);
        nextMonthRecycler.hasFixedSize();
        nextMonthRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        restRecycler = view.findViewById(R.id.restRecycler);
        restRecycler.hasFixedSize();
        restRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        restList = db.getAllEvent();
        toDeleteList = new ArrayList<>();
        overdueList = new ArrayList<>();
        todayList = new ArrayList<>();
        tomorrowList = new ArrayList<>();
        thisWeekList = new ArrayList<>();
        nextWeekList = new ArrayList<>();
        thisMonthList = new ArrayList<>();
        nextMonthList = new ArrayList<>();


        if (restList.isEmpty()) {
            remain.setVisibility(View.VISIBLE);

            overdue.setVisibility(View.GONE);
            today.setVisibility(View.GONE);
            tomorrow.setVisibility(View.GONE);
            thisWeek.setVisibility(View.GONE);
            nextWeek.setVisibility(View.GONE);
            thisMonth.setVisibility(View.GONE);
            nextMonth.setVisibility(View.GONE);
            rest.setVisibility(View.GONE);

            overdueRecycler.setVisibility(View.GONE);
            todayRecycler.setVisibility(View.GONE);
            tomorrowRecycler.setVisibility(View.GONE);
            thisWeekRecycler.setVisibility(View.GONE);
            nextWeekRecycler.setVisibility(View.GONE);
            thisMonthRecycler.setVisibility(View.GONE);
            nextMonthRecycler.setVisibility(View.GONE);
            restRecycler.setVisibility(View.GONE);
        }

        Date d1 = new Date();
        int todayDate = d1.getDate();
        int todayMonth = d1.getMonth();
        int todayYear = d1.getYear();
        long nowTime = d1.getTime();


        //sorting the task according to the time
        for (int i = restList.size() - 1; i > 0; i--) {
            for (int j = restList.size() - 1; j > 0; j--) {
                Date oldTime = new Date(restList.get(j).getDate());
                Date newTime = new Date(restList.get(j - 1).getDate());
                if (oldTime.compareTo(newTime) < 0) {
                    Date tempDate = oldTime;
                    oldTime = newTime;
                    newTime = tempDate;
                    Details temprestList = restList.get(j);
                    restList.set(j, restList.get(j - 1));
                    restList.set(j - 1, temprestList);

                }
            }
        }


        //assigning pending and today tomorrow and yesterday of date
        for (Details det : restList) {
            Date d2 = new Date(det.getDate());

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(d2);
            c1.setFirstDayOfWeek(Calendar.MONDAY);
            c2.setFirstDayOfWeek(Calendar.MONDAY);
            if (c1.after(c2)) {
                det.setTask(det.getTask() + "(PENDING)");
                overdueList.add(det);
                toDeleteList.add(det);
                if (c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == 1
                        || c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == -364
                        || c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR) == -365) {
                    det.setDate("YESTERDAY " + det.getDate().substring(18));
                }
                if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                    det.setDate("Today " + det.getDate().substring(18));
                }

            } else if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                det.setDate("Today " + det.getDate().substring(18));
                todayList.add(det);
                toDeleteList.add(det);
            } else if (c1.before(c2)) {
                if (c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR) == 1
                        || ((c1.get(Calendar.DAY_OF_YEAR) == 365 || c1.get(Calendar.DAY_OF_YEAR) == 366) && c2.get(Calendar.DAY_OF_YEAR) == 1)) {
                    det.setDate("TOMORROW " + det.getDate().substring(18));
                    tomorrowList.add(det);
                    toDeleteList.add(det);
                }
                if (c2.get(Calendar.WEEK_OF_MONTH) == c1.get(Calendar.WEEK_OF_MONTH)) {
                    if ((c2.get(Calendar.DAY_OF_WEEK) - c1.get(Calendar.DAY_OF_WEEK) > 1)) {
                        thisWeekList.add(det);
                        toDeleteList.add(det);
                    }
                }
                if ((c2.get(Calendar.WEEK_OF_YEAR) - c1.get(Calendar.WEEK_OF_YEAR)) == 1 ||
                        (c2.get(Calendar.WEEK_OF_YEAR) < 2 && c1.get(Calendar.WEEK_OF_YEAR) > 51)) {
                    nextWeekList.add(det);
                    toDeleteList.add(det);
                }
                if ((c2.get(Calendar.MONTH) == c1.get(Calendar.MONTH))) {
                    if((c2.get(Calendar.WEEK_OF_MONTH)-c1.get(Calendar.WEEK_OF_MONTH))>1) {
                        thisMonthList.add(det);
                        toDeleteList.add(det);
                    }
                }
                if ((c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH)) == 1
                        || (c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH))==-11) {
                        nextMonthList.add(det);
                        toDeleteList.add(det);
                }
            }
        }

        restList.removeAll(toDeleteList);

        overdueAdapter = new MyAdapter(getContext(), overdueList);
        overdueAdapter.setListener(this);
        overdueRecycler.setAdapter(overdueAdapter);

        todayAdapter = new MyAdapter(getContext(), todayList);
        todayAdapter.setListener(this);
        todayRecycler.setAdapter(todayAdapter);

        tomorrowAdapter = new MyAdapter(getContext(), tomorrowList);
        tomorrowAdapter.setListener(this);
        tomorrowRecycler.setAdapter(tomorrowAdapter);

        thisWeekAdapter = new MyAdapter(getContext(), thisWeekList);
        thisWeekAdapter.setListener(this);
        thisWeekRecycler.setAdapter(thisWeekAdapter);

        nextWeekAdapter = new MyAdapter(getContext(), nextWeekList);
        nextWeekAdapter.setListener(this);
        nextWeekRecycler.setAdapter(nextWeekAdapter);

        thisMonthAdapter = new MyAdapter(getContext(), thisMonthList);
        thisMonthAdapter.setListener(this);
        thisMonthRecycler.setAdapter(thisMonthAdapter);

        nextMonthAdapter = new MyAdapter(getContext(), nextMonthList);
        nextMonthAdapter.setListener(this);
        nextMonthRecycler.setAdapter(nextMonthAdapter);

        restAdapter = new MyAdapter(getContext(), restList);
        restAdapter.setListener(this);
        restRecycler.setAdapter(restAdapter);




        ifEmpty();
        return view;
    }

    public void ifEmpty(){
        if (overdueList.isEmpty()) {
            overdue.setVisibility(View.GONE);
            overdueRecycler.setVisibility(View.GONE);
        }

        if (todayList.isEmpty()) {
            today.setVisibility(View.GONE);
            todayRecycler.setVisibility(View.GONE);
        }
        if (tomorrowList.isEmpty()) {
            tomorrow.setVisibility(View.GONE);
            tomorrowRecycler.setVisibility(View.GONE);
        }
        if (thisWeekList.isEmpty()) {
            thisWeek.setVisibility(View.GONE);
            thisWeekRecycler.setVisibility(View.GONE);
        }
        if (nextWeekList.isEmpty()) {
            nextWeek.setVisibility(View.GONE);
            nextWeekRecycler.setVisibility(View.GONE);
        }
        if (thisMonthList.isEmpty()) {
            thisMonth.setVisibility(View.GONE);
            thisMonthRecycler.setVisibility(View.GONE);
        }
        if (nextMonthList.isEmpty()) {
            nextMonth.setVisibility(View.GONE);
            nextMonthRecycler.setVisibility(View.GONE);
        }
        if (restList.isEmpty()) {
            rest.setVisibility(View.GONE);
            restRecycler.setVisibility(View.GONE);
        }
        if(overdueList.isEmpty()&& todayList.isEmpty() && thisWeekList.isEmpty()&& nextWeekList.isEmpty()&&thisMonthList.isEmpty()
                && nextMonthList.isEmpty()&& restList.isEmpty()){
            remain.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onEmpty() {
        ifEmpty();
    }
}
