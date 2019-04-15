package com.example.cymoh.notepad.database;

import android.print.PrinterId;
import android.provider.FontsContract;

public class Note {
    public static final String TABLE_NAME ="note_pad" ;

    public  static final String COLUMN_ID = "id";
    public  static  final String COLUMN_TIME = "timestamp";
    public static  final  String COLUMN_NOTE = "note";
    public static final String COLUMN_NOTEDETAIL = "notDetail";

    private int id;
    private  String note;
    private String note_detail;
    private String timeStamp;

    //query for creating a table
    public static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME +"("
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +COLUMN_NOTE+ " TEXT ,"
            +COLUMN_NOTEDETAIL+" TEXT ,"
            +COLUMN_TIME+" DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
    public Note() {

    }

    public Note(int id, String note, String note_detail, String timeStamp) {
        this.id = id;
        this.note = note;
        this.note_detail = note_detail;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_detail() {
        return note_detail;
    }

    public void setNote_detail(String note_detail) {
        this.note_detail = note_detail;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
