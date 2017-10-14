package com.simra.HostelRecords;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Simra Afreen on 02-10-2017.
 */


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    StudentOpenHelper openHelper;


    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.roll) EditText _roll;
    @Bind(R.id.room) EditText _room;
    @Bind(R.id.input_father) EditText _father;
    @Bind(R.id.input_fatherNo) EditText _father_no;
    @Bind(R.id.btn_signup) Button _signupButton;
//    @Bind(R.id.link_login) TextView _loginLink;
    EditText roll;
    EditText room;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        roll = (EditText)findViewById(R.id.roll);
        room = (EditText)findViewById(R.id.room);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                Intent intent = new Intent();
            }
        });

//        _loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Finish the registration screen and return to the Login activity
//                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
       // int mobile = Integer.parseInt(_mobileText.getEditableText().toString());
        String mobile = _mobileText.getText().toString();
        String father = _father.getText().toString();
       // int father_no = Integer.parseInt(_father_no.getEditableText().toString());
        String father_no = _father_no.getText().toString();
        int rollNo = Integer.parseInt(roll.getEditableText().toString());
        int roomNo = Integer.parseInt(room.getEditableText().toString());

        // TODO: Implement signup logic here.

        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.STUDENT_NAME,name);
        contentValues.put(Contract.EMAIL,email);
        contentValues.put(Contract.MOBILE_NO,mobile);
        contentValues.put(Contract.ADDRESS,address);
        contentValues.put(Contract.FATHER_NAME,father);
        contentValues.put(Contract.FATHER_NO,father_no);
        contentValues.put(Contract.ROLL_NO,rollNo);
        contentValues.put(Contract.ROOM_NO,roomNo);


        long id = database.insert(Contract.TABLE_NAME,null,contentValues);

        StudentOpenHelper attendanceOpenHelper = StudentOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = attendanceOpenHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Contract.NAME,name);
        cv.put(Contract.ATTENDANCE,0);
        cv.put(Contract.ABSENT_DAYS,0);
        cv.put(Contract.LEAVE_DAYS,0);
        cv.put(Contract.PENDING_FINE,0);

       long att_id =  sqLiteDatabase.insert(Contract.ATTENDANCE_TABLE_NAME,null,cv);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String father = _father.getText().toString();
        String father_no = _father_no.getText().toString();
        String rollNo = _roll.getText().toString();
        String room = _room.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (rollNo.isEmpty()){
            _roll.setError("enter a valid roll no");
            valid = false;
        }
        else {
            _roll.setError(null);
        }

        if (room.isEmpty()){
            _room.setError("enter valid room no");
            valid = false;
        }
        else {
            _room.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (father.isEmpty() || father.length() < 3 ) {
            _father.setError("at least 3 characters");
            valid = false;
        } else {
            _father.setError(null);
        }

        if (father_no.isEmpty() || father_no.length() != 10 ) {
            _father_no.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _father_no.setError(null);
        }

        return valid;
    }
}