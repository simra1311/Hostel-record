package com.simra.HostelRecords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalisedRecords extends AppCompatActivity {

    StudentOpenHelper openHelper;
    TextView name;
    TextView attendance;
    TextView leaves;
    TextView absent;
    TextView fine;
    Button mark;
    Button leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalised_records);

        name = (TextView)findViewById(R.id.name);
        attendance = (TextView)findViewById(R.id.total_present);
        leaves = (TextView)findViewById(R.id.total_absent);
        absent = (TextView)findViewById(R.id.total_absent);
        fine = (TextView)findViewById(R.id.fine);

        Intent intent = getIntent();

        Student student = (Student)intent.getSerializableExtra(Contract.TABLE_NAME);

        String student_name = student.getName();

        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Contract.ATTENDANCE_TABLE_NAME,null,Contract.NAME +" = ? ",new String[]{},null,null,null);
                //query(Contract.ATTENDANCE_TABLE_NAME,null,Contract.NAME + " = ? ", new String[]{student_name},null,null,null);

        StudentRecord studentRecord;
        int total_attendance;
        int total_absent;
        int tot_leaves;
        int tot_fine;
        long id;

        if (cursor.moveToFirst()!= true) {
            Toast.makeText(this,"Record does not exist!!!",Toast.LENGTH_LONG).show();
            return;
        }
            total_attendance = cursor.getInt(cursor.getColumnIndex(Contract.ATTENDANCE));
            total_absent = cursor.getInt(cursor.getColumnIndex(Contract.ABSENT_DAYS));
            tot_leaves = cursor.getInt(cursor.getColumnIndex(Contract.LEAVE_DAYS));
            tot_fine = cursor.getInt(cursor.getColumnIndex(Contract.PENDING_FINE));
            id = cursor.getLong(cursor.getColumnIndex(Contract.ATTENDANCE_ID));
            studentRecord = new StudentRecord(id,student_name,total_attendance,tot_leaves,total_absent,tot_fine);

        name.setText(student_name);
        attendance.setText( studentRecord.getTot_attendance()+"");
        leaves.setText(studentRecord.getLeaves()+"");
        absent.setText(studentRecord.getAbsents()+"");
        fine.setText(studentRecord.getFine()+"");
    }
}
