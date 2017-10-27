package com.simra.HostelRecords;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Simra Afreen on 20-10-2017.
 */
@Dao
public interface DaoClass {


    @Query("SELECT * FROM student")
    List<Student> getStudentList();

    @Insert
    long addStudent(Student student);

}
