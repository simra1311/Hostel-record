package com.simra.HostelRecords;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Simra Afreen on 03-10-2017.
 */

public class CustomAdapter extends ArrayAdapter<Student> {

    Context mcontext;
    ArrayList<Student> mList;

    public CustomAdapter(@NonNull Context context, ArrayList<Student> list) {
        super(context, 0);
        mcontext = context;
        mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.row_layout, null);
            holder = new ViewHolder();
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView roll = (TextView)convertView.findViewById(R.id.roll);
            TextView room = (TextView)convertView.findViewById(R.id.room);
            holder.name = name;
            holder.roll = roll;
            holder.room = room;
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
        Student student = mList.get(position);
        holder.name.setText(student.getName());
        holder.roll.setText(student.getRollNo()+"");
        //holder.roll.setText(student.getRollNo());
        holder.room.setText(student.getRoomNo()+"");
        return convertView;
    }


    static class ViewHolder {

        TextView name;
        TextView roll;
        TextView room;
        Button button;
    }
}

