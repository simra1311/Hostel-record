package com.simra.HostelRecords;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String father_no ;
    String student_name;
    int roll;
    int room;
    int year;
    long id;
    String email,father,add,mobile;

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

         student_name = student.getName();
         roll = student.getRollNo();
         room = student.getRoomNo();
         father_no = student.getFather_no();
         father = student.getFather();
        email = student.getEmail();
        mobile = student.getMobile();
        add  = student.getAddress();
        id = student.getId();
        year = student.getYear();

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

        if (!cursor.moveToFirst()) {
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

//                int att = Integer.parseInt(attendance.getText().toString());
//                attendance.setText(att+1 +" ");

                Intent i = new Intent(PersonalisedRecords.this,CustomCalendar.class);
                startActivity(i);
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
                           father_no = cursor.getString(cursor.getColumnIndex(Contract.FATHER_NO));
                        }
                        cursor.close();
                        sendSMS(father_no);
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change fine

            }
        });
    }




    private void sendSMS(String father_no) {

        phoneNo = father_no;
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
        else{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(PersonalisedRecords.this,"in else",Toast.LENGTH_SHORT).show();
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
                }  else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again...", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    public void Fine(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalisedRecords.this);
        View view1 = getLayoutInflater().inflate(R.layout.change_fine,null);
        TextView textView = (TextView)view1.findViewById(R.id.title);
        final EditText editText = (EditText)view1.findViewById(R.id.fine);
        textView.setText("Change Fine amount?");
        builder.setView(view1);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int fine = Integer.parseInt(editText.getEditableText().toString());

                StudentOpenHelper openHelper = StudentOpenHelper.getInstance(PersonalisedRecords.this);
                SQLiteDatabase database = openHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.PENDING_FINE,fine);
//                contentValues.put(Contract.STUDENT_NAME,student_name);
//                contentValues.put(Contract.MOBILE_NO, mobile);
//                contentValues.put(Contract.EMAIL,email);
//                contentValues.put(Contract.FATHER_NAME,father);
//                contentValues.put(Contract.ADDRESS,add);
//                contentValues.put(Contract.YEAR,year);
//                contentValues.put(Contract.STUDENT_ID,id);
//                contentValues.put(Contract.FATHER_NO,father_no);
//                contentValues.put(Contract.ROLL_NO,roll);
//                contentValues.put(Contract.ROOM_NO,room);
                database.update(Contract.ATTENDANCE_TABLE_NAME,contentValues,Contract.STUDENT_NAME + " = ? ", new String[]{student_name});

                Toast.makeText(PersonalisedRecords.this,"Fine updated!",Toast.LENGTH_SHORT).show();

                sendSMSIntent(father_no,fine);


            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    protected void sendSMSIntent(String father_no,int fine) {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String (father_no));
        smsIntent.putExtra("sms_body"  , "Your ward " + name.getText().toString() + " has been fined Rs." + fine +"/- Please pay the fine as soon as possible");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PersonalisedRecords.this,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.personalised_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_login) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivityForResult(intent,1);
//
//            return true;
//        }

        if (id == R.id.detail){
            Intent intent = new Intent(this, DetailedRecords.class);
            intent.putExtra("name",student_name);
            intent.putExtra("year",roll);
            intent.putExtra("room",room);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
