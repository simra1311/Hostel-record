package com.simra.HostelRecords;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Simra Afreen on 22-10-2017.
 */
@Database(entities = {Student.class}, version = 1)
public abstract class StudentDatabase extends RoomDatabase{

    public static StudentDatabase INSTANCE;

    public static StudentDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    StudentDatabase.class,"hostelrec")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract DaoClass getDao();
}
