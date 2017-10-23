package com.simra.HostelRecords;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7. widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    StudentOpenHelper openHelper;
    ArrayList<Student> list;
    ListView listView;
    ArrayAdapter<Student> adapter;
    int SEE_RECORDS = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add){
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }

        if (id == R.id.start){
            //code for adding alarm notifications
            sendBroadcast();

        }
        if (id == R.id.stop){
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(MainActivity.this,AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,1,alarmIntent,0);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(MainActivity.this,"Alarm cancelled",Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.nav_import) {
            // Handle the import action
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT,"DOWNLOAD our app");
            Intent chooser = Intent.createChooser(share,"Share App");
            startActivity(chooser);

        }
        else if (id == R.id.nav_send){
            Intent feedback = new Intent();
            feedback.setAction(Intent.ACTION_SENDTO);
            feedback.setData(Uri.parse("mailto:simraafreen.13@gmail.com"));
            feedback.putExtra(Intent.EXTRA_TEXT,"FEEDBACK");
            if(feedback.resolveActivity(getPackageManager()) != null){
                startActivity(feedback);
            }
            else {

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
//        StudentDatabase studentDatabasse = StudentDatabase.getInstance(MainActivity.this);
//        list.addAll(studentDatabasse.getDao().getStudentList());
        adapter.notifyDataSetChanged();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }


    public void sendBroadcast(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this,AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,alarmIntent,0);

        Toast.makeText(MainActivity.this , "Alarm set", Toast.LENGTH_SHORT).show();
        // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ AlarmManager.INTERVAL_DAY,AlarmManager.INTERVAL_DAY,pendingIntent);

        Calendar calendar = Calendar.getInstance();
        // Time time = new Time(9,30,0);
        Date date = new Date(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),21,30);
        calendar.setTime(date);
        //  calendar.setTimeInMillis(System.currentTimeMillis());
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }
}
