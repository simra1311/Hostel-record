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
    TextView year1;
    StudentOpenHelper openHelper;

    Student student;
    String add;
    String email;
    int roll;
    int room;
    String mob;
    String father_no;
    String father;
    int year;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_records);

        name =(TextView) findViewById(R.id.name);
        father1 =(TextView) findViewById(R.id.father);
        fatherNo1 =(TextView) findViewById(R.id.fatherNo);
        email1 =(TextView) findViewById(R.id.email);
        mobile =(TextView) findViewById(R.id.mobile);
        rollNo =(TextView) findViewById(R.id.roll);
        roomNo =(TextView) findViewById(R.id.room);
        address =(TextView) findViewById(R.id.address);
        year1 = (TextView)findViewById(R.id.year);


        Intent intent = getIntent();
        String student_name = intent.getStringExtra("name");
        roll = intent.getIntExtra("year",0);
        room = intent.getIntExtra("room",0);
        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
        final SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Contract.TABLE_NAME,null,Contract.STUDENT_NAME +" = ? ",new String[]{student_name},null,null,null);
        //query(Contract.ATTENDANCE_TABLE_NAME,null,Contract.NAME + " = ? ", new String[]{student_name},null,null,null);

//        if (cursor.moveToFirst()!= true) {
//            Toast.makeText(this,"Record does not exist!!!",Toast.LENGTH_LONG).show();
//            return;
//        }

        if(cursor!=null)
        if (cursor.moveToNext()) {
           // Toast.makeText(this,"Check",Toast.LENGTH_SHORT).show();
            roll = cursor.getInt(cursor.getColumnIndex(Contract.ROLL_NO));
            room = cursor.getInt(cursor.getColumnIndex(Contract.ROOM_NO));
            mob = cursor.getString(cursor.getColumnIndex(Contract.MOBILE_NO));
            father_no = cursor.getString(cursor.getColumnIndex(Contract.FATHER_NO));
            father = cursor.getString(cursor.getColumnIndex(Contract.FATHER_NAME));
            email = cursor.getString(cursor.getColumnIndex(Contract.EMAIL));
            id = cursor.getInt(cursor.getColumnIndex(Contract.STUDENT_ID));
            add = cursor.getString(cursor.getColumnIndex(Contract.ADDRESS));
            year = cursor.getInt(cursor.getColumnIndex(Contract.YEAR));
            student = new Student(student_name, roll, room, email, father, father_no, add, mob,year, id);
        }

    //    Toast.makeText(this,student.getRollNo()+"helo",Toast.LENGTH_SHORT).show();

          name.setText(student.getName());
          address.setText(student.getAddress());
          email1.setText(student.getEmail());
          father1.setText(student.getFather());
          fatherNo1.setText(student.getFather_no()+"");
          mobile.setText(student.getMobile()+"");
          rollNo.setText(roll + "");
          roomNo.setText(room + "");
          year1.setText(year+"");
    }
}
