package com.simra.HostelRecords;

import java.io.Serializable;

/**
 * Created by Simra Afreen on 02-10-2017.
 */

public class Student implements Serializable {

    private String name;
    private int rollNo;
    private int roomNo;
    private String email;
    private String password;
    private String address;
    private int mobile;
    private long id;

    public Student(String name, int rollNo, int roomNo, String email, String password, String address, int mobile, long id) {
        this.name = name;
        this.rollNo = rollNo;
        this.roomNo = roomNo;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
