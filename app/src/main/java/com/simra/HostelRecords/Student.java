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
    private String father;
    private String address;
    private int mobile;
    private int father_no;
    private long id;

    public Student(String name, int rollNo, int roomNo, String email, String father,int father_no, String address, int mobile, long id) {
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

    public int getMobile() {
        return mobile;
    }

    public int getFather_no() {
        return father_no;
    }

    public void setFather_no(int father_no) {
        this.father_no = father_no;
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
