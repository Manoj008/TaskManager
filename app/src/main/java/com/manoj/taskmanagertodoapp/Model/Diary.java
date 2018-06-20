package com.manoj.taskmanagertodoapp.Model;

/**
 * Created by MANOJ on 04-Jun-18.
 */

public class Diary {

    String name;
    String note;
    String date;
    int id;

    public Diary(int id,String name, String note, String date) {
        this.name = name;
        this.note = note;
        this.date = date;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
