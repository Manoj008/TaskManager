package com.manoj.taskmanagertodoapp.Model;

/**
 * Created by MANOJ on 01-Jun-18.
 */

public class Details {
    int id;
    String task;
    String description;
    String date;
    String time;
    String place;
    String reminder;
    String repeat;
    int ring;



    public Details(int id, String task, String description, String date, String place, String reminder, String repeat,int ring) {

        this.id=id;
        this.task = task;
        this.description = description;
        this.date = date;
        this.place = place;
        this.reminder = reminder;
        this.repeat = repeat;
        this.ring=ring;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {return task;}

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

}
