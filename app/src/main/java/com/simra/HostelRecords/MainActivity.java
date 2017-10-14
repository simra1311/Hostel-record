package com.simra.HostelRecords;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        Intent i = getIntent();

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.dialogue_remove_layout,null);
                final int pos = position;
                TextView textView = (TextView)view1.findViewById(R.id.title);
                textView.setText("Remove record?");
                final String name = list.get(position).getName();
                builder.setView(view1);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        openHelper =StudentOpenHelper.getInstance(getApplicationContext());
                        SQLiteDatabase db = openHelper.getWritableDatabase();

                        db.delete(Contract.TABLE_NAME,Contract.STUDENT_ID + " = ? ",new String[]{list.get(pos).getId() + ""} );
                        list.remove(pos);
                        adapter.notifyDataSetChanged();

                        openHelper = StudentOpenHelper.getInstance(getApplicationContext());
                        SQLiteDatabase sq = openHelper.getWritableDatabase();
                        sq.delete(Contract.ATTENDANCE_TABLE_NAME,Contract.NAME + " = ? ",new String[]{name});
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
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
            String mobile = cursor.getString(cursor.getColumnIndex(Contract.MOBILE_NO));
            int roomNo = cursor.getInt(cursor.getColumnIndex(Contract.ROOM_NO));
            int rollNo = cursor.getInt(cursor.getColumnIndex(Contract.ROLL_NO));
            String father = cursor.getString(cursor.getColumnIndex(Contract.FATHER_NAME));
            String father_no = cursor.getString(cursor.getColumnIndex(Contract.FATHER_NO));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.STUDENT_ID));

            Student s = new Student(name,rollNo,roomNo,email,father,father_no,address,mobile,id);
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
//        if (id == R.id.action_login) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivityForResult(intent,1);
//
//            return true;
//        }
         if (id == R.id.signup){
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
