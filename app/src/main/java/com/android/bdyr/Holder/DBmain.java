package com.android.bdyr.Holder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBmain extends SQLiteOpenHelper {
    private static final String DBNAME="Event";
    private static final String TABLE="Event_Table";
    private static final int VAR=1;
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="create table " + TABLE + "(id integer primary key,eName text ,eDate text ,eCategory text,eNumber,eText text)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query="drop table if exists " + TABLE + "";
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
    public DBmain(Context context){
        super(context,DBNAME,null,VAR);
    }
}