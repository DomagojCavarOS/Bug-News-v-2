package com.d42gmail.cavar.bugnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Enigma on 24.11.2015..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Bugdata.db";
    public static final String TABLE_NAME="bugtable";
    public static final String c_1="ID";
    public static final String c_2="TITLE";
    public static final String c_3="DESCRIPTION";
    public static final String c_4="LINK";
    public static final String c_5="CATEGORY";
    public static final String c_6="IMAGEURL";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT, DESCRIPTION TEXT, LINK TEXT, CATEGORY TEXT, IMAGEURL TEXT) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(db);
    }
    public void addData(Bug bug,Context ctx){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(c_2,bug.getTitle());
        contentValues.put(c_3,bug.getDescription());
        contentValues.put(c_4,bug.getLink());
        contentValues.put(c_5, bug.getCategory());
        contentValues.put(c_6, bug.getImageurl());
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            Log.i("baza","fail");
        }
        else{
            Log.i("baza","check");
        }

    }

    public ArrayList<Bug> getResultDB(){
        ArrayList<Bug> resultDB=new ArrayList<Bug>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Bug bug=new Bug(cursor.getString(1),cursor.getString(2),cursor.getString(3)
                        ,cursor.getString(4),cursor.getString(5));
                resultDB.add(bug);

                Log.i("baza",""+cursor.getString(1)+cursor.getString(2)+cursor.getString(3)
                        +cursor.getString(4)+cursor.getString(5));
            }
            while(cursor.moveToNext());
        }
        db.close();
        Log.i("baza", "prošao bazu");
        return resultDB;
    }

    public void deleteDatabase(ArrayList<Bug> listaZaBrisat) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id=0;
        for(Bug bug:listaZaBrisat) {

            String[] arg = new String[] { String.valueOf(id) };
            db.delete(TABLE_NAME, c_1 + "=?",arg);
            id++;
        }
        db.close();
    }

}
