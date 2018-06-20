package com.manoj.taskmanagertodoapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.manoj.taskmanagertodoapp.Main2Activity;
import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.R;
import com.manoj.taskmanagertodoapp.Reciever;
import com.manoj.taskmanagertodoapp.database.DatabaseTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddBirthday extends Fragment {
    EditText bname;
    LinearLayout bdate;
    TextView bdate1;
    Spinner breminder;
    List<String> bReminderList;
    FloatingActionButton save;
    int id = -1, brem = 0;
    String remind;
    CheckBox delBday;
    LinearLayout linear;

    public AddBirthday() {
        // Required empty public constructor
    }


    public static AddBirthday newInstance(int id) {
        AddBirthday fragment = new AddBirthday();
        Bundle args = new Bundle();
        args.putInt("id", id);
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

    private void gotoBday() {
        Main2Activity.updateStatus(getContext());
        Intent intent = new Intent(getContext(), Main2Activity.class);
        intent.putExtra("fragment", "bday");
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_birthday, container, false);
        // Inflate the layout for this fragment
        bname = view.findViewById(R.id.bname);
        bdate = view.findViewById(R.id.bdate);
        bdate1 = view.findViewById(R.id.bdate1);
        breminder = view.findViewById(R.id.breminder);
        delBday = view.findViewById(R.id.delBday);
        save = view.findViewById(R.id.save);
        linear = view.findViewById(R.id.linear);

        if (id >= 0) {
            linear.setVisibility(View.VISIBLE);
        }


        delBday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linear.animate().translationX(3000).setDuration(1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseTask db = new DatabaseTask(getContext());
                            db.deleteBday(id);
                            gotoBday();
                        }
                    }, 600);

                }

            }
        });


        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int _year = calendar.get(Calendar.YEAR);
                int _month = calendar.get(Calendar.MONTH);
                int _date = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String monthName = "";
                                String dayString = "" + dayOfMonth;

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
                                bdate1.setText(String.valueOf(monthName + " " + dayString));
                            }
                        }, _year, _month, _date);

                datePickerDialog.show();
            }
        });

        bReminderList = new ArrayList<>();
        breminder = view.findViewById(R.id.breminder);

        bReminderList = new ArrayList<>();
        bReminderList.add("5 Minutes Before MidNiight");
        bReminderList.add("15 Minutes Before MidNiight");
        bReminderList.add("30 Minutes Before MidNight");
        bReminderList.add("1 Hour Before MidNight");
        bReminderList.add("2 Hours Before BirthDay");
        bReminderList.add("1 Day Before BirthDay");


        breminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                remind = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, bReminderList);
        breminder.setAdapter(adapter);
        if (id >= 0) {
            DatabaseTask db = new DatabaseTask(getContext());
            Details detail = db.getBday(id);
            bname.setText(detail.getTask());
            bdate1.setText(detail.getDate().substring(0, 6));
            breminder.setSelection(Integer.parseInt(detail.getReminder()));
            remind = detail.getReminder();
            Log.e("exccc", "remindd  " + remind);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = bname.getText().toString();
                if (name == null || name.equals("")) {
                    Toast.makeText(getActivity(), "please enter name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bdate1.getText() == null || bdate1.getText().toString() == "") {
                    Toast.makeText(getActivity(), "please enter date of birth", Toast.LENGTH_LONG).show();
                    return;
                }

                int da, y, mon = 0;
                Date date = new Date();
                String dob = bdate1.getText().toString() + ", " + date.toString().substring(date.toString().length() - 4);
                remind = String.valueOf(breminder.getSelectedItemPosition());
                if (new Date(dob).before(date)) {
                    int newDate = Integer.parseInt(date.toString().substring(date.toString().length() - 4));
                    dob.replace(String.valueOf(newDate), String.valueOf(newDate + 1));

                }
                DatabaseTask db = new DatabaseTask(getContext());

                Log.e("excc", dob.substring(0, 11));
                da = Integer.parseInt(dob.substring(4, 6));
                y = Integer.parseInt(dob.substring(8, 12));

                switch (dob.substring(0, 3)) {
                    case "Jan":
                        mon = 0;
                        break;
                    case "Feb":
                        mon = 1;
                        break;
                    case "Mar":
                        mon = 2;
                        break;
                    case "Apr":
                        mon = 3;
                        break;
                    case "May":
                        mon = 4;
                        break;
                    case "Jun":
                        mon = 5;
                        break;
                    case "Jul":
                        mon = 6;
                        break;
                    case "Aug":
                        mon = 7;
                        break;
                    case "Sep":
                        mon = 8;
                        break;
                    case "Oct":
                        mon = 9;
                        break;
                    case "Nov":
                        mon = 10;
                        break;
                    case "Dec":
                        mon = 11;
                        break;
                    default:
                        Log.e("excc", "inside swicth");

                }

                Calendar remindTime = Calendar.getInstance();
                remindTime.set(y, mon, da, 0, 0);
                Log.e("Exccc", "da+mon+y" + da + " " + mon + " " + y);

                if (id >= 0) {
                    Log.e("exccc", "remindd  " + remind);
                    db.updateBday(id, name, dob, remind, 0);
                    switch (Integer.parseInt(remind)) {
                        case 0:
                        case 1:
                            remindTime.add(Calendar.MINUTE, -15);
                            break;
                        case 2:
                            remindTime.add(Calendar.MINUTE, -30);
                            break;
                        case 3:
                            remindTime.add(Calendar.HOUR_OF_DAY, -1);
                            break;
                        case 4:
                            remindTime.add(Calendar.HOUR_OF_DAY, -2);
                            break;
                        case 5:
                            remindTime.add(Calendar.DAY_OF_MONTH, -1);
                            break;
                        case 7:
                            remindTime.add(Calendar.HOUR_OF_DAY, -5);
                            break;
                    }
                } else {
                    db.addBday(name, dob, remind);

                    switch (Integer.parseInt(remind)) {
                        case 0:
                            break;
                        case 1:
                            remindTime.add(Calendar.MINUTE, -15);
                            break;
                        case 2:
                            remindTime.add(Calendar.MINUTE, -30);
                            break;
                        case 3:
                            remindTime.add(Calendar.HOUR_OF_DAY, -1);
                            break;
                        case 4:
                            remindTime.add(Calendar.HOUR_OF_DAY, -2);
                            break;
                        case 5:
                            remindTime.add(Calendar.DAY_OF_MONTH, -1);
                            break;
                        case 7:
                            remindTime.add(Calendar.HOUR_OF_DAY, -5);
                            break;
                    }
                }

                Reciever.setReminder(getContext(), Reciever.class, remindTime.get(Calendar.YEAR), remindTime.get(Calendar.MONTH)
                        , remindTime.get(Calendar.DAY_OF_MONTH), remindTime.get(Calendar.HOUR_OF_DAY), remindTime.get(Calendar.MINUTE), "Birthday Notification"
                        , "dont forget to wish birthday to " + bname.getText().toString());
                gotoBday();
            }
        });


        return view;
    }
}
