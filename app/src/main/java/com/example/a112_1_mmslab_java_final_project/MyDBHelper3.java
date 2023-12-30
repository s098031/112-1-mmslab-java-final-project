package com.example.a112_1_mmslab_java_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper3 extends SQLiteOpenHelper {
    private static final String name = "mdatabase3.db";
    private static final int version =1;
    MyDBHelper3(Context context){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db3)
    {
        db3.execSQL("CREATE TABLE myTable(_id INTEGER PRIMARY KEY AUTOINCREMENT,lend text NOT NULL,borrow text NOT NULL,item text NOT NULL, price integer NOT NULL, date text NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db3, int oldVersion, int newVersion) {
        db3.execSQL("DROP TABLE IF EXISTS myTable");
        onCreate(db3);
    }
}