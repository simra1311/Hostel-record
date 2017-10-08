package com.simra.HostelRecords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Simra Afreen on 02-10-2017.
 */


public class MainActivity extends ActionBarActivity {

    StudentOpenHelper openHelper;
    ArrayList<Student> list;
    ListView listView;
    ArrayAdapter<Student> adapter;
    int SEE_RECORDS = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        list = new ArrayList<>();
        adapter = new CustomAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = list.get(position);
                Intent intent = new Intent(MainActivity.this,PersonalisedRecords.class);

                intent.putExtra(Contract.TABLE_NAME,student);
                startActivityForResult(intent , SEE_RECORDS);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchFromDatabase();
    }

    public void fetchFromDatabase(){

        list.clear();
        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Contract.TABLE_NAME,null,null,null,null,null,null);

        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(Contract.STUDENT_NAME));
            String email = cursor.getString(cursor.getColumnIndex(Contract.EMAIL));
            String address = cursor.getString(cursor.getColumnIndex(Contract.ADDRESS));
            int mobile = cursor.getInt(cursor.getColumnIndex(Contract.MOBILE_NO));
            int roomNo = cursor.getInt(cursor.getColumnIndex(Contract.ROOM_NO));
            int rollNo = cursor.getInt(cursor.getColumnIndex(Contract.ROLL_NO));
            String password = cursor.getString(cursor.getColumnIndex(Contract.PASSWORD));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.STUDENT_ID));

            Student s = new Student(name,rollNo,roomNo,email,password,address,mobile,id);
            list.add(s);
        }
        cursor.close();
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,1);

            return true;
        }
        else if (id == R.id.signup){
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
