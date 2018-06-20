package com.manoj.taskmanagertodoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.manoj.taskmanagertodoapp.Model.Details;
import com.manoj.taskmanagertodoapp.Model.Diary;
import com.manoj.taskmanagertodoapp.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MANOJ on 01-Jun-18.
 */

public class DatabaseTask extends SQLiteOpenHelper {

    SQLiteDatabase db;
    Cursor cursor;
    Context context;

    public DatabaseTask(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE = "CREATE TABLE " + Util.Table_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.KEY_NAME + " TEXT,"
                + Util.KEY_DESC + " TEXT,"
                + Util.KEY_DATE + " TEXT,"
                + Util.KEY_PLACE + " TEXT,"
                + Util.KEY_REMINDER + " TEXT,"
                + Util.KEY_REPEAT + " TEXT,"
                + Util.RING+" INTEGER)";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE1 = "CREATE TABLE " + Util.FIN_Table_NAME + "("
                + Util.FIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.FIN_NAME + " TEXT,"
                + Util.KEY_DATE + " TEXT,"
                + Util.KEY_PLACE + " TEXT)";
        db.execSQL(CREATE_TABLE1);

        String CREATE_TABLE2 = "CREATE TABLE " + Util.BDAY_Table_NAME + "("
                + Util.BDAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.BDAY_NAME + " TEXT,"
                + Util.BDAY_DATE + " TEXT,"
                + Util.BDAY_REMINDER + " TEXT,"
                + Util.RING+" INTEGER)";
        db.execSQL(CREATE_TABLE2);

        String CREATE_TABLE3 = "CREATE TABLE " + Util.NOTE_Table_NAME + "("
                + Util.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.NOTE_NAME + " TEXT,"
                + Util.NOTE_NOTE + " TEXT,"
                + Util.NOTE_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE3);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addEvent(String name, String description, String date, String place, String reminder, String repeat) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, name);
        values.put(Util.KEY_DESC, description);
        values.put(Util.KEY_DATE, date);
        values.put(Util.KEY_PLACE, place);
        values.put(Util.KEY_REMINDER, reminder);
        values.put(Util.KEY_REPEAT, repeat);
        values.put(Util.RING, 0);

        db.insert(Util.Table_NAME, null, values);
        cursor = db.rawQuery("SELECT * FROM " + Util.Table_NAME, null);
        cursor.moveToLast();
        db.close();
    }

    public Details getEvent(int id) {
        db = this.getReadableDatabase();

        cursor = db.query(Util.Table_NAME, new String[]{Util.KEY_NAME, Util.KEY_DESC, Util.KEY_DATE, Util.KEY_PLACE
                        , Util.KEY_REMINDER, Util.KEY_REPEAT,Util.RING}, Util.KEY_ID + "=?", new String[]{String.valueOf(id)}
                , null, null, null, "1");

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Details details = new Details(id, cursor.getString(0), cursor.getString(1)
                , cursor.getString(2), cursor.getString(3)
                , cursor.getString(4), cursor.getString(5)
                ,cursor.getInt(6));

        return details;
    }

    public List<Details> getAllEvent() {
        db = this.getReadableDatabase();

        //db.delete(Util.Table_NAME,null,null);

        List<Details> details = new ArrayList<>();
        cursor = db.rawQuery("select * from " + Util.Table_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Details detail = new Details(cursor.getInt(0), cursor.getString(1)
                        , cursor.getString(2), cursor.getString(3)
                        , cursor.getString(4), cursor.getString(5)
                        , cursor.getString(6),cursor.getInt(7));
                details.add(detail);

            } while (cursor.moveToNext());
        }
        return details;
    }

    public void deleteEvent(int id) {
        db = this.getWritableDatabase();
        addFinished(id);
    }

    public void updateEvent(int id, String name, String description, String date, String place, String reminder, String repeat,int ring) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, name);
        values.put(Util.KEY_DESC, description);
        values.put(Util.KEY_DATE, date);
        values.put(Util.KEY_PLACE, place);
        values.put(Util.KEY_REMINDER, reminder);
        values.put(Util.KEY_REPEAT, repeat);
        values.put(Util.RING, ring);

        db.update(Util.Table_NAME, values, Util.KEY_ID + "=?", new String[]{String.valueOf(id)});

        cursor = db.rawQuery("SELECT * FROM " + Util.Table_NAME, null);
        cursor.moveToLast();
        db.close();
    }

    public void addFinished(int id) {

        DatabaseTask tasks = new DatabaseTask(context);
        Details details = tasks.getEvent(id);


        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.FIN_NAME, details.getTask());
        values.put(Util.FIN_DATE, details.getDate());
        values.put(Util.FIN_PLACE, details.getPlace());
        db.insert(Util.FIN_Table_NAME, null, values);
        cursor = db.rawQuery("SELECT * FROM " + Util.FIN_Table_NAME, null);
        cursor.moveToLast();
        db.delete(Util.Table_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Details> getAllFinished() {
        db = this.getReadableDatabase();

        List<Details> details = new ArrayList<>();
        cursor = db.rawQuery("select * from " + Util.FIN_Table_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Details detail = new Details(cursor.getInt(0), cursor.getString(1)
                        , "", cursor.getString(2)
                        , cursor.getString(3), "0", "0",0);
                details.add(detail);

            } while (cursor.moveToNext());
        }
        return details;
    }

    public void deleteFinished(int id) {
        db = this.getWritableDatabase();
        db.delete(Util.FIN_Table_NAME, Util.FIN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleleteAllFinished() {
        db = this.getWritableDatabase();
        db.delete(Util.FIN_Table_NAME, null, null);
        db.rawQuery("vacuum", null);
    }

    public void addBday(String name, String date, String reminder) {
        Log.e("exccc","add bday reminder"+reminder);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.BDAY_NAME, name);
        values.put(Util.BDAY_DATE, date);
        values.put(Util.BDAY_REMINDER, reminder);
        values.put(Util.RING,0);

        db.insert(Util.BDAY_Table_NAME, null, values);
        cursor = db.rawQuery("SELECT * FROM " + Util.BDAY_Table_NAME, null);
        cursor.moveToLast();
        db.close();
        Log.w("excc", "birthday");
    }

    public Details getBday(int id) {
        db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + Util.BDAY_Table_NAME + " WHERE " + Util.BDAY_ID + " =" + id, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Details details = new Details(cursor.getInt(0), cursor.getString(1), ""
                , cursor.getString(2), "", cursor.getString(3), "",cursor.getInt(4));
        return details;
    }

    public void deleteBday(int id){
        db=this.getWritableDatabase();
        db.delete(Util.BDAY_Table_NAME,Util.BDAY_ID+" =?",new String[]{String.valueOf(id)});
    }
    public void updateBday(int id, String name, String date, String reminder,int ring) {
        db = this.getWritableDatabase();
        Log.e("exccc","add bday reminder"+reminder);

        ContentValues values = new ContentValues();
        values.put(Util.BDAY_NAME, name);
        values.put(Util.BDAY_DATE, date);
        values.put(Util.BDAY_REMINDER, reminder);
        values.put(Util.RING, ring);

        db.update(Util.BDAY_Table_NAME, values, Util.BDAY_ID + "=?", new String[]{String.valueOf(id)});

        cursor = db.rawQuery("SELECT * FROM " + Util.BDAY_Table_NAME, null);
        cursor.moveToLast();
        db.close();
    }

    public List<Details> getAllBday() {
        db = this.getReadableDatabase();
        List<Details> details = new ArrayList<>();
        cursor = db.rawQuery("select * from " + Util.BDAY_Table_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Details detail = new Details(cursor.getInt(0), cursor.getString(1)
                        , "", cursor.getString(2), "", cursor.getString(3)
                        , "",cursor.getInt(4));
                details.add(detail);

            } while (cursor.moveToNext());
        }
        return details;
    }

    public void addNote(String name, String note, String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.NOTE_NAME, name);
        values.put(Util.NOTE_NOTE, note);
        values.put(Util.NOTE_DATE, date);
        db.insert(Util.NOTE_Table_NAME, null, values);

        cursor = db.rawQuery("select * from " + Util.NOTE_Table_NAME, null);
        cursor.moveToLast();
        db.close();
    }

    public List<Diary> allNotes() {
        db = this.getReadableDatabase();
        cursor = db.rawQuery("select * from " + Util.NOTE_Table_NAME, null);

        List<Diary> allNotes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Diary note = new Diary(cursor.getInt(0), cursor.getString(1)
                        , cursor.getString(2), cursor.getString(3));
                allNotes.add(note);
                Log.e("exccc",note.getName().toString()+ "  "+note.getId());
            } while (cursor.moveToNext());
        }
        Log.e("excc","all note called");
        return allNotes;
    }

    public Diary getNote(int id) {
        db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + Util.NOTE_Table_NAME + " WHERE " + Util.NOTE_ID + " =" + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Diary note = new Diary(cursor.getInt(0), cursor.getString(1)
                , cursor.getString(2), cursor.getString(3));
        return note;
    }

    public void updateNote(int id, String name, String note, String date) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.NOTE_NAME, name);
        values.put(Util.NOTE_NOTE, note);
        values.put(Util.NOTE_DATE, date);
        db.update(Util.NOTE_Table_NAME, values, Util.NOTE_ID + "=?", new String[]{String.valueOf(id)});

        cursor = db.rawQuery("select * from " + Util.NOTE_Table_NAME, null);
        cursor.moveToLast();
        db.close();
    }

    public void deleteNote(int id) {
        db=this.getWritableDatabase();
        db.delete(Util.NOTE_Table_NAME,Util.NOTE_ID+" =?",new String[]{String.valueOf(id)});
    }
}
