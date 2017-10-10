package com.simra.HostelRecords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailedRecords extends AppCompatActivity {

    TextView name;

    TextView email1;
    TextView father1;
    TextView fatherNo1;
    TextView mobile;
    TextView rollNo;
    TextView roomNo;
    TextView address;
    StudentOpenHelper openHelper;

    Student studentRecord;
    String add;
    String email;
    int roll;
    int room;
    int mob;
    int father_no;
    String father;
    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_records);


        name =(TextView) findViewById(R.id.name);
        father1 =(TextView) findViewById(R.id.father);
        fatherNo1 =(TextView) findViewById(R.id.fatherNo);
        email1 =(TextView) findViewById(R.id.email);
        mobile =(TextView) findViewById(R.id.mobile);
        rollNo =(TextView) findViewById(roll);
        roomNo =(TextView) findViewById(R.id.room);
        address =(TextView) findViewById(R.id.address);

        Intent intent = getIntent();
        String student_name = intent.getStringExtra("name");
        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
        final SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();



        Cursor cursor = sqLiteDatabase.query(Contract.TABLE_NAME,null,Contract.STUDENT_NAME +" = ? ",new String[]{student_name},null,null,null);
        //query(Contract.ATTENDANCE_TABLE_NAME,null,Contract.NAME + " = ? ", new String[]{student_name},null,null,null);

//        if (cursor.moveToFirst()!= true) {
//            Toast.makeText(this,"Record does not exist!!!",Toast.LENGTH_LONG).show();
//            return;
//        }

        if (cursor.moveToNext()) {
            roll = cursor.getInt(cursor.getColumnIndex(Contract.ROLL_NO));
            room = cursor.getInt(cursor.getColumnIndex(Contract.ROOM_NO));
            mob = cursor.getInt(cursor.getColumnIndex(Contract.MOBILE_NO));
            father_no = cursor.getInt(cursor.getColumnIndex(Contract.FATHER_NO));
            father = cursor.getString(cursor.getColumnIndex(Contract.FATHER_NAME));
            email = cursor.getString(cursor.getColumnIndex(Contract.EMAIL));
            id = cursor.getLong(cursor.getColumnIndex(Contract.ATTENDANCE_ID));
            add = cursor.getString(cursor.getColumnIndex(Contract.ADDRESS));
            studentRecord = new Student(student_name, roll, room, email, father, father_no, add, mob, id);
        }

        cursor.close();

        name.setText(studentRecord.getName());
        address.setText(studentRecord.getAddress());
        email1.setText(studentRecord.getEmail());
       father1.setText(studentRecord.getFather());
        fatherNo1.setText(studentRecord.getFather_no());
        mobile.setText(studentRecord.getMobile());
        rollNo.setText(studentRecord.getRollNo());
        roomNo.setText(studentRecord.getRoomNo());

    }
}
