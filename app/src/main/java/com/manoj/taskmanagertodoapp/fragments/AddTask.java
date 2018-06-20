package com.manoj.taskmanagertodoapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.manoj.taskmanagertodoapp.Main2Activity;
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.Reciever;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class AddTask extends Fragment {

    LinearLayout date, time, linear;
    EditText name, desc, place;
    TextView date1, time1;
    Spinner reminder, repeat;
    CheckBox checkBox1;
    List<String> reminderList, repeatList;
    String r1, r2;
    int rem = 0, rep = 0;
    FloatingActionButton save;
    int arg = -1;
    boolean oldDate=false,oldTime=false;

    int y, mon, da, ho, min;
    int ny = 0, nmon, nda, nho, nmin;


    public AddTask() {
        // Required empty public constructor
    }

    public static AddTask newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        AddTask fragment = new AddTask();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            arg = getArguments().getInt("id");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        // Inflate the layout for this fragment

        final int[] ddd = new int[1];
        final int[] hhh = new int[1];
        final int[] mmm = new int[1];

        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        name = view.findViewById(R.id.name);
        place = view.findViewById(R.id.place);
        desc = view.findViewById(R.id.description);
        linear = view.findViewById(R.id.linear);
        checkBox1 = view.findViewById(R.id.checkbox1);

        date1 = view.findViewById(R.id.date1);
        time1 = view.findViewById(R.id.time1);

        reminder = view.findViewById(R.id.reminder);
        repeat = view.findViewById(R.id.repeat);

        //checking if it is the new task or old if arg>=0 means old task

        Calendar ccl = Calendar.getInstance();
        Date date123;
        Calendar ccl2 = Calendar.getInstance();
        if (arg >= 0) {
            DatabaseTask db = new DatabaseTask(getContext());
            Details detail = db.getEvent(arg);

            date123 = new Date(detail.getDate());
            ccl2.setTime(date123);

            ddd[0]=ccl2.get(Calendar.DAY_OF_YEAR);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");

            String ret = detail.getDate().substring(0, 17);

            String now = sdf.format(Calendar.getInstance().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tom = sdf.format(calendar.getTime());

            calendar.add(Calendar.DAY_OF_YEAR, -2);
            String yest = sdf.format(calendar.getTime());

            String retTime = detail.getDate().substring(18, detail.getDate().length());
            String yet = sdf.format(calendar.getTime());

            time.setVisibility(View.VISIBLE);
            linear.setVisibility(View.VISIBLE);
            name.setText(detail.getTask());
            if (ret.equals(now)) {
                date1.setText("Today");
                oldDate = false;
                ddd[0]=Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            } else if (ret.equals(tom)) {
                date1.setText("Tomorrow");
                oldDate = false;
                ddd[0]=calendar.get(Calendar.DAY_OF_YEAR)+2;
            } else if (ret.equals(yest)) {
                date1.setText("Yesterday");
                oldDate = true;
                ddd[0]=calendar.get(Calendar.DAY_OF_YEAR);
            } else {
                date1.setText(ret);
            }

            if (ccl.getTime().after(ccl2.getTime()) && !ret.equals(now)) {
                oldDate = true;
                date1.setTextColor(Color.RED);
                oldTime = true;
                time1.setTextColor(Color.RED);
            } else if (ccl.getTime().after(ccl2.getTime()) && ret.equals(now)) {
                oldTime = true;
                time1.setTextColor(Color.RED);
            }
            Log.e("excccctoday", ccl.getTime() + "  " + ccl2.getTime() + " " + oldDate);

            time1.setText(detail.getDate().substring(18, detail.getDate().length()));
            place.setText(detail.getPlace());
            desc.setText(detail.getDescription());
            rem = Integer.parseInt(detail.getReminder());
            rep = Integer.parseInt(detail.getRepeat());
        }

        //removing the task fom
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linear.animate().translationX(3000).setDuration(1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseTask db = new DatabaseTask(getContext());
                            db.deleteEvent(arg);
                            gotoTask();
                        }
                    }, 600);

                }

            }
        });



        //getting date from user
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date1.setTextColor(Color.BLACK);
                oldDate=false;
                final Calendar calendar = Calendar.getInstance();
                int _year = calendar.get(Calendar.YEAR);
                int _month = calendar.get(Calendar.MONTH);
                int _date = calendar.get(Calendar.DAY_OF_MONTH);
                ddd[0] =_date;
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String monthName = "";
                                String dayString = "" + dayOfMonth;
                                ddd[0] =dayOfMonth;
                                if (dayOfMonth < 10) {
                                    dayString = "0" + dayString;
                                }
                                switch (month) {
                                    case 0:
                                        monthName = "Jan";
                                        break;
                                    case 1:
                                        monthName = "Feb";
                                        break;
                                    case 2:
                                        monthName = "Mar";
                                        break;
                                    case 3:
                                        monthName = "Apr";
                                        break;
                                    case 4:
                                        monthName = "May";
                                        break;
                                    case 5:
                                        monthName = "Jun";
                                        break;
                                    case 6:
                                        monthName = "Jul";
                                        break;
                                    case 7:
                                        monthName = "Aug";
                                        break;
                                    case 8:
                                        monthName = "Sep";
                                        break;
                                    case 9:
                                        monthName = "Oct";
                                        break;
                                    case 10:
                                        monthName = "Nov";
                                        break;
                                    case 11:
                                        monthName = "Dec";
                                        break;
                                }

                                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth - 1);
                                int day = cal.get(cal.DAY_OF_WEEK);

                                String dayName = "";
                                switch (day) {
                                    case 1:
                                        dayName = "Mon";
                                        break;
                                    case 2:
                                        dayName = "Tue";
                                        break;
                                    case 3:
                                        dayName = "Wed";
                                        break;
                                    case 4:
                                        dayName = "Thu";
                                        break;
                                    case 5:
                                        dayName = "Fri";
                                        break;
                                    case 6:
                                        dayName = "Sat";
                                        break;
                                    case 7:
                                        dayName = "Sun";
                                        break;
                                }
                                date1.setText(String.valueOf(dayName + ", " + monthName + " " + dayString + ", " + year));
                                Calendar c1=Calendar.getInstance();
                                c1.set(year,month, Integer.parseInt(dayString));
                                Calendar c=Calendar.getInstance();
                                time.setVisibility(View.VISIBLE);
                                if(c1.before(c)){
                                    date1.setTextColor(Color.RED);
                                    oldDate=true;
                                }
                                if (c1.get(Calendar.DAY_OF_YEAR)==c.get(Calendar.DAY_OF_YEAR)){
                                    date1.setText("Today");
                                }
                                if (c1.get(Calendar.DAY_OF_YEAR)==c.get(Calendar.DAY_OF_YEAR)+1){
                                    date1.setText("Tomorrow");
                                }

                            }
                        }, _year, _month, _date);

                datePickerDialog.show();
            }
        });


        //getting time from user
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time1.setTextColor(Color.BLACK);
                oldTime=false;
                Calendar timer = Calendar.getInstance();
                int _hour = timer.get(Calendar.HOUR_OF_DAY);
                final int _min = timer.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String t = "";
                                String m = "";
                                String h = "";

                                hhh[0] =hourOfDay;
                                mmm[0] =minute;

                                ho=hourOfDay;
                                min=minute;

                                if(hourOfDay>11) {
                                    t = "PM";
                                }
                                else {
                                    t="AM";
                                }
                                if (hourOfDay > 12) {
                                    hourOfDay = hourOfDay % 12;
                                }

                                if (hourOfDay < 10) {
                                    h = "0" + hourOfDay;
                                }
                                else {
                                    h=""+hourOfDay;
                                }
                                if (minute < 10) {
                                    m = "0" + minute;
                                } else {
                                    m = "" + minute;
                                }
                                if (hourOfDay == 0) {
                                    h = ""+12;
                                }

                                time1.setText(String.valueOf(h + ":" + m + "  " + t));
                                Calendar c1=Calendar.getInstance();
                                c1.set(Calendar.HOUR_OF_DAY, hhh[0]);
                                c1.set(Calendar.MINUTE,minute);
                                c1.set(Calendar.DAY_OF_MONTH, ddd[0]);
                                Log.e("excaa","fs"+c1.getTime().toString());
                                Calendar c=Calendar.getInstance();
                                Log.e("excaa",c.getTime().toString());
                                if(c1.before(c) ){
                                    time1.setTextColor(Color.RED);
                                    oldTime=true;
                                }


                            }
                        }, _hour, _min, false);

                timePickerDialog.show();
            }
        });


        reminderList = new ArrayList<>();
        reminderList.add("OnTime");
        reminderList.add("5 Mins before Task");
        reminderList.add("15 Mins before Task");
        reminderList.add("30 Mins before Task");
        reminderList.add("1 Hour before Task");
        reminderList.add("2 Hours before Task");
        reminderList.add("3 Hours before Task");
        reminderList.add("5 Hours before Task");


        repeatList = new ArrayList<>();
        repeatList.add("No Repeat");;
        repeatList.add("Everday(mon-fri)");
        repeatList.add("Everyday(mon-sat)");
        repeatList.add("Everyday");
        repeatList.add("Once A Weak");
        repeatList.add("Once A Month");

        reminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                r1 = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                r1 = "" + 0;
            }
        });

        repeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                r2 = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                r2 = repeatList.get(0);
            }
        });

        ArrayAdapter reminderAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, reminderList);
        reminder.setAdapter(reminderAdapter);

        ArrayAdapter repeatAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, repeatList);
        repeat.setAdapter(repeatAdapter);

        if (arg >= 0) {
            reminder.setSelection(rem, true);
            repeat.setSelection(rep, true);
        }


        save = view.findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n=name.getText().toString();
                if(n==null|| n.equals("")){
                    Toast.makeText(getActivity(),"please enter task name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(time1.getText()==null||time1.getText().toString()==""){
                    Toast.makeText(getActivity(),"please enter time",Toast.LENGTH_LONG).show();
                    return;
                }

                if (oldDate || oldTime){
                    Toast.makeText(getActivity(),"please enter valid time of task",Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getActivity(), "event Added", Toast.LENGTH_LONG);
                String dateStr = date1.getText().toString();
                String timeStr = time1.getText().toString();
                Calendar c=Calendar.getInstance();
                String d= String.valueOf(c.getTime());
                //assigning default date if date is not entered
                if (dateStr.length() < 6) {
                    dateStr = d.toString().substring(0, 3).concat(",").concat(d.toString().substring(3, 10)).concat(",")
                            .concat(d.toString().substring(d.toString().length() - 5));
                }
                c.add(Calendar.DAY_OF_YEAR,+1);
                d= String.valueOf(c.getTime());
                if (dateStr.length() < 9) {
                    dateStr = d.toString().substring(0, 3).concat(",").concat(d.toString().substring(3, 10)).concat(",")
                            .concat(d.toString().substring(d.toString().length() - 5));
                }
                Log.e("excxc",""+dateStr);
                da=Integer.parseInt(dateStr.substring(9,11));
                y=Integer.parseInt(dateStr.substring(13,17));
                switch (dateStr.substring(5,8)){
                    case "Jan":mon=0;
                        break;
                    case "Feb":mon=1;
                        break;
                    case "Mar":mon=2;
                        break;
                    case "Apr":mon=3;
                        break;
                    case "May":mon=4;
                        break;
                    case "Jun":mon=5;
                        break;
                    case "Jul":mon=6;
                        break;
                    case "Aug":mon=7;
                        break;
                    case "Sep":mon=8;
                        break;
                    case "Oct":mon=9;
                        break;
                    case "Nov":mon=10;
                        break;
                    case "Dec":mon=11;
                        break;

                }

                Calendar c1=Calendar.getInstance();
                c1.set(Calendar.HOUR_OF_DAY, hhh[0]);
                c1.set(Calendar.MINUTE,mmm[0]);
                c1.set(Calendar.DAY_OF_MONTH, ddd[0]);
                Log.e("excaa","fs"+c1.getTime().toString());
                Calendar cc=Calendar.getInstance();
                Log.e("excaa",c.getTime().toString());
                if(c1.before(cc) ){
                    time1.setTextColor(Color.RED);
                    oldTime=true;
                    Toast.makeText(getActivity(),"Please enter valid time",Toast.LENGTH_LONG).show();
                    return;
                }



                DatabaseTask db = new DatabaseTask(getContext());
                Calendar remindTime=Calendar.getInstance();
                remindTime.set(y,mon,da,ho,min);
                if (arg >= 0) {
                    db.updateEvent(arg, name.getText().toString(), desc.getText().toString()
                            , dateStr + "  " + timeStr, place.getText().toString(), r1, r2,0);

                    linear.setVisibility(View.VISIBLE);
                    int remind= reminder.getSelectedItemPosition();
                    Log.e("exccc","remindd  "+remind);
                    switch (remind){
                        case 0:
                            break;
                        case 1:
                            remindTime.add(Calendar.MINUTE,-5);
                            break;
                        case 2:
                            remindTime.add(Calendar.MINUTE,-15);
                            break;
                        case 3:
                            remindTime.add(Calendar.MINUTE,-30);
                            break;
                        case 4:
                            remindTime.add(Calendar.HOUR_OF_DAY,-1);
                            break;
                        case 5:
                            remindTime.add(Calendar.HOUR_OF_DAY,-2);
                            break;
                        case 6:
                            remindTime.add(Calendar.HOUR_OF_DAY,-3);
                            break;
                        case 7:
                            remindTime.add(Calendar.HOUR_OF_DAY,-5);
                            break;
                    }

                }
                else {


                    db.addEvent(name.getText().toString(), desc.getText().toString()
                            , dateStr + "  " + timeStr, place.getText().toString(), r1, r2);

                    int remind= reminder.getSelectedItemPosition();
                    switch (remind){
                        case 0:
                            break;
                        case 1:
                            remindTime.add(Calendar.MINUTE,-5);
                            break;
                        case 2:
                            remindTime.add(Calendar.MINUTE,-15);
                            break;
                        case 3:
                            remindTime.add(Calendar.MINUTE,-30);
                            break;
                        case 4:
                            remindTime.add(Calendar.HOUR_OF_DAY,-1);
                            break;
                        case 5:
                            remindTime.add(Calendar.HOUR_OF_DAY,-2);
                            break;
                        case 6:
                            remindTime.add(Calendar.HOUR_OF_DAY,-3);
                            break;
                        case 7:
                            remindTime.add(Calendar.HOUR_OF_DAY,-5);
                            break;
                    }

                }
                Reciever.setReminder(getContext(), Reciever.class,remindTime.get(Calendar.YEAR), remindTime.get(Calendar.MONTH)
                        , remindTime.get(Calendar.DAY_OF_MONTH), remindTime.get(Calendar.HOUR_OF_DAY), remindTime.get(Calendar.MINUTE),"Your Task Notification"
                        ,"you have "+name.getText().toString() +" task to do");


                gotoTask();
            }
        });


        return view;
    }

    private void gotoTask() {
        Main2Activity.updateStatus(getContext());
        Intent intent=new Intent(getContext(),Main2Activity.class);
        intent.putExtra("fragment","task");
        startActivity(intent);
    }
}
