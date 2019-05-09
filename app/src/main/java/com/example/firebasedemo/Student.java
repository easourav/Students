package com.example.firebasedemo;

public class Student {
    String schoolLat;
    String schoolLng;


    String studentLat;
    String studentLng;

    String keyNode;
    String schoolName;
    String className;
    String studentName;
    String studentRoll;
    String studentCaneWay;
    String schoolKey;


    public Student() {
    }

    public Student(String studentLat, String studentLng, String keyNode, String schoolName, String className,
                   String studentName, String studentRoll, String studentCaneWay, String schoolKey) {
        this.studentLat = studentLat;
        this.studentLng = studentLng;
        this.keyNode = keyNode;
        this.schoolName = schoolName;
        this.className = className;
        this.studentName = studentName;
        this.studentRoll = studentRoll;
        this.studentCaneWay = studentCaneWay;
        this.schoolKey = schoolKey;
    }

    public Student(String schoolLat, String schoolLng, String studentLat, String studentLng, String keyNode, String schoolName, String className, String studentName, String studentRoll, String studentCaneWay, String schoolKey) {
        this.schoolLat = schoolLat;
        this.schoolLng = schoolLng;
        this.studentLat = studentLat;
        this.studentLng = studentLng;
        this.keyNode = keyNode;
        this.schoolName = schoolName;
        this.className = className;
        this.studentName = studentName;
        this.studentRoll = studentRoll;
        this.studentCaneWay = studentCaneWay;
        this.schoolKey = schoolKey;
    }

    public String getSchoolLat() {
        return schoolLat;
    }

    public void setSchoolLat(String schoolLat) {
        this.schoolLat = schoolLat;
    }

    public String getSchoolLng() {
        return schoolLng;
    }

    public void setSchoolLng(String schoolLng) {
        this.schoolLng = schoolLng;
    }

    public String getStudentLat() {
        return studentLat;
    }

    public void setStudentLat(String studentLat) {
        this.studentLat = studentLat;
    }

    public String getStudentLng() {
        return studentLng;
    }

    public void setStudentLng(String studentLng) {
        this.studentLng = studentLng;
    }

    public String getKeyNode() {
        return keyNode;
    }

    public void setKeyNode(String keyNode) {
        this.keyNode = keyNode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
    }

    public String getStudentCaneWay() {
        return studentCaneWay;
    }

    public void setStudentCaneWay(String studentCaneWay) {
        this.studentCaneWay = studentCaneWay;
    }

    public String getSchoolKey() {
        return schoolKey;
    }

    public void setSchoolKey(String schoolKey) {
        this.schoolKey = schoolKey;
    }
}
