package com.simra.HostelRecords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simra Afreen on 02-10-2017.
 */

public class StudentOpenHelper  extends SQLiteOpenHelper {


    public static StudentOpenHelper instance;

    public static StudentOpenHelper getInstance(Context context) {
        if (instance == null){
            instance = new StudentOpenHelper(context);
        }
        return instance;
    }

    public StudentOpenHelper(Context context) {
        super(context,"Hostel_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE "  + Contract.TABLE_NAME  + " ( " +
                Contract.STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.STUDENT_NAME + " TEXT, " +
                Contract.ROLL_NO + " INTEGER, "+
                Contract.ROOM_NO + " INTEGER, " +
                Contract.EMAIL + " TEXT, " +
                Contract.FATHER_NAME + " TEXT, " +
                Contract.FATHER_NO + " TEXT, " +
                Contract.MOBILE_NO + " TEXT, " +
                Contract.YEAR + " INTEGER, " +
                Contract.ADDRESS + " TEXT)" ;
        sqLiteDatabase.execSQL(query);

        String query1 = "CREATE TABLE "  + Contract.ATTENDANCE_TABLE_NAME  + " ( " +
                Contract.ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.NAME + " TEXT, " +
                Contract.ATTENDANCE + " INTEGER, "+
                Contract.ABSENT_DAYS + " INTEGER, " +
                Contract.LEAVE_DAYS + " INTEGER, " +
                Contract.PENDING_FINE + " INTEGER)";

        sqLiteDatabase.execSQL(query1);

        String query2 = "CREATE TABLE " + Contract.DAILY_RECORD + " ( " +
                Contract.DATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.STUDENT_NAME + " TEXT, " +
                Contract.DATE + " INTEGER) ";

        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
