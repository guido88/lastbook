package com.example.acer.lastbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by acer on 07/07/2016.
 */
public class DBHelper extends SQLiteOpenHelper{


    public DBHelper(Context context){

        super(context,"books",null,1);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table books"+
                "(id integer primary key,titolo text,link text)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }

    public void insertBook(String titolo,String link){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("titolo",titolo);
        values.put("link",link);

        db.insert("books",null,values);
    }


    public Cursor query()
    {
        Cursor crs=null;
        try
        {
            SQLiteDatabase db=this.getReadableDatabase();
            crs=db.query("books", null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }
}
