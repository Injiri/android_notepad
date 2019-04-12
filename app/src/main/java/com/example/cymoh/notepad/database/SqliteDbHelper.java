package com.example.cymoh.notepad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "nptepad_db";

    public SqliteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
    }

    //updating db .
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        //create table again
        onCreate(db);
    }

    public long insertNote(String note, String noteDetail) {
        //get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // id ,timestamp values willl be inserted automatically
        values.put(Note.COLUMN_NOTE, note);
        values.put(Note.COLUMN_NOTEDETAIL, noteDetail);

        //insert one row
        long id = db.insert(Note.TABLE_NAME, null, values);

        //close db connection
        db.close();
        //newly inserted row id
        return id;
    }

    //prepare a note object
    public Note getNote(long id) {
        //get a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_NOTEDETAIL, Note.COLUMN_TIME
                }, Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        //prepare a note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTEDETAIL)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIME)));

        cursor.close();

        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        //select all
        String selectQuery = "SELECT*FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIME + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        //loop through all the rows appending to the list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setNote_detail(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTEDETAIL)));
                note.setTimeStamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIME)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    public  int getNotesCount(){

        String countQueary  = "SELECT * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQueary, null);

        int count = cursor.getCount();
        cursor.close();

        return count;

    }

    public int updateNote(Note note ){
    SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Note.COLUMN_NOTE,note.getNote());
            values.put(Note.COLUMN_NOTEDETAIL, note.getNote_detail());
            //update a row
        return db.update(Note.TABLE_NAME,values,Note.COLUMN_ID+"= ?",
                new String[]{String.valueOf(note.getId())});
            }
 public  void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
 }
}

