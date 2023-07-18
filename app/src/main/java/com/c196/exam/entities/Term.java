package com.c196.exam.entities;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Term {
    private int id;
    private String title;
    private String start;
    private String end;

    public Term() { }

    public Term(String termTitle, String termStart, String termEnd) {
        this.title = termTitle;
        this.start = termStart;
        this.end = termEnd;
    }

    public Term(int id, String termTitle, String termStart, String termEnd) {
        this.id = id;
        this.title = termTitle;
        this.start = termStart;
        this.end = termEnd;
    }

    private ArrayList<Course> courses;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
