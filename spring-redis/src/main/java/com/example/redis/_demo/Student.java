package com.example.redis._demo;

public class Student {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private int id;

    public Student(int id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    private String name;
    private String sex;


}
