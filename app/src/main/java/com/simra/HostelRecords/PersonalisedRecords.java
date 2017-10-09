package com.simra.HostelRecords;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
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
    int father_no ;

    String phoneNo;
    String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalised_records);

        name = (TextView)findViewById(R.id.name);
        attendance = (TextView)findViewById(R.id.total_present);
        leaves = (TextView)findViewById(R.id.total_leaves);
        absent = (TextView)findViewById(R.id.total_absent);
        fine = (TextView)findViewById(R.id.fine);
        mark = (Button)findViewById(R.id.mark);
        leave = (Button)findViewById(R.id.leave);

        Intent intent = getIntent();

        Student student = (Student)intent.getSerializableExtra(Contract.TABLE_NAME);

        final String student_name = student.getName();

        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
        final SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Contract.ATTENDANCE_TABLE_NAME,null,Contract.NAME +" = ? ",new String[]{student_name},null,null,null);
                //query(Contract.ATTENDANCE_TABLE_NAME,null,Contract.NAME + " = ? ", new String[]{student_name},null,null,null);

        StudentRecord studentRecord;
        final int total_attendance;
        final int total_absent;
        final int tot_leaves;
        final int tot_fine;
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

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                openHelper = StudentOpenHelper.getInstance(getApplicationContext());
//                SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
//                Cursor cursor = sqLiteDatabase.query(Contract.TABLE_NAME,null,Contract.STUDENT_NAME =" = ? ",new String[]{student_name},null,null,null);
//                if (cursor.moveToNext()){
//
//                }
                openHelper = StudentOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ATTENDANCE,total_attendance+1);
                contentValues.put(Contract.LEAVE_DAYS,tot_leaves);
                contentValues.put(Contract.PENDING_FINE,tot_fine);
                contentValues.put(Contract.NAME,student_name);
                contentValues.put(Contract.ABSENT_DAYS,total_absent);

                database.update(Contract.ATTENDANCE_TABLE_NAME,contentValues,Contract.NAME + " = ?",new String[]{student_name});
                Toast.makeText(PersonalisedRecords.this,"Marked attendance",Toast.LENGTH_SHORT).show();

                int att = Integer.parseInt(attendance.getText().toString());
                attendance.setText(att+1 +" ");
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHelper = StudentOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ATTENDANCE,total_attendance);
                contentValues.put(Contract.LEAVE_DAYS,tot_leaves+1);
                contentValues.put(Contract.PENDING_FINE,tot_fine);
                contentValues.put(Contract.NAME,student_name);
                contentValues.put(Contract.ABSENT_DAYS,total_absent);

                database.update(Contract.ATTENDANCE_TABLE_NAME,contentValues,Contract.NAME + " = ?",new String[]{student_name});
                Toast.makeText(PersonalisedRecords.this,"Marked leave",Toast.LENGTH_SHORT).show();

                int l = Integer.parseInt(leaves.getText().toString());
                leaves.setText(l+1 +" ");


                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalisedRecords.this);
                View view1 = getLayoutInflater().inflate(R.layout.dialogue_layout,null);
                TextView textView = (TextView)view1.findViewById(R.id.title);
                textView.setText("Send SMS?");
                builder.setView(view1);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
                        SQLiteDatabase database = openHelper.getReadableDatabase();

                        Cursor cursor = database.query(Contract.TABLE_NAME,null,Contract.STUDENT_NAME +" = ? ",new String[]{student_name},null,null,null);
                        if (cursor.moveToNext()){
                           father_no = cursor.getInt(cursor.getColumnIndex(Contract.FATHER_NO));
                        }
                        sendSMS(father_no);
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void sendSMS(int father_no) {

        phoneNo = String.valueOf(father_no);
        message = "This is to inform you that your ward " + name.getText().toString() + " is on leave from hostel";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again...", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
