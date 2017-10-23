package com.simra.HostelRecords;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Simra Afreen on 02-10-2017.
 */

@Entity(tableName = "student")
public class Student implements Serializable {

    private String name;
    private int rollNo;
    private int roomNo;
    private String email;
    private String father;
    private String address;
    private String mobile;
    private String father_no;
    @PrimaryKey(autoGenerate = true)
    private long id;

    public Student(String name, int rollNo, int roomNo, String email, String father,String father_no, String address, String mobile, long id) {
        this.name = name;
        this.rollNo = rollNo;
        this.roomNo = roomNo;
        this.email = email;
        this.father = father;
        this.father_no = father_no;
        this.address = address;
        this.mobile = mobile;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFather_no() {
        return father_no;
    }

    public void setFather_no(String father_no) {
        this.father_no = father_no;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
