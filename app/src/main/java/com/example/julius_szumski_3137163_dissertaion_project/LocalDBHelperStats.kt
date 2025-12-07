package com.example.julius_szumski_3137163_dissertaion_project

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDBHelperStats (context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,version: Int): SQLiteOpenHelper(context,name,factory,version){
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DROP_TABLE)
        db?.execSQL(CREATE_TABLE)
    }

    private val CREATE_TABLE: String = "create table if not Exists Stats("+"ID integer primary key autoincrement,"+"CURRENTSTEPSSEARCH integer,"+"CURRENTSTEPSGOAL integer,"+"TODAYSSTEPS integer,"+"TOTALSTEPS integer"+")"


    private val DROP_TABLE: String = "drop Table todoLists"
}