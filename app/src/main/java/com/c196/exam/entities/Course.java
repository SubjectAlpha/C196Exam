package com.c196.exam.entities;

import java.time.Instant;
import java.util.ArrayList;

public class Course {
    private int id;
    private String title;
    private String start;
    private String end;
    private String status;
    private String instructorFirstName;
    private String instructorLastName;
    private String instructorEmail;
    private String instructorPhone;
    private Integer termId;
    private ArrayList<Assessment> assessments;

    public Course(Integer termId, String title, String start, String end){
        this.termId = termId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.status = Statuses.PENDING.name();
    }

    public Course(String title,
                  String start,
                  String end,
                  String status,
                  String instructorFirstName,
                  String instructorLastName,
                  String instructorEmail,
                  String instructorPhone,
                  Integer termId) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.status = status;
        this.instructorFirstName = instructorFirstName;
        this.instructorLastName = instructorLastName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
        this.termId = termId;
        this.status = Statuses.PENDING.name();
    }

    public Course(String title, String start, String end, String status,
                  String instructorFirstName, String instructorLastName, String instructorEmail,
                  String instructorPhone, Integer termId, ArrayList<Assessment> assessments) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.status = status;
        this.instructorFirstName = instructorFirstName;
        this.instructorLastName = instructorLastName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
        this.termId = termId;
        this.assessments = assessments;
        this.status = Statuses.PENDING.name();
    }

    public Course(Integer id, String title, String start, String end, String status,
                  String instructorFirstName, String instructorLastName, String instructorEmail,
                  String instructorPhone, Integer termId, ArrayList<Assessment> assessments) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.status = status;
        this.instructorFirstName = instructorFirstName;
        this.instructorLastName = instructorLastName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
        this.termId = termId;
        this.assessments = assessments;
        this.status = Statuses.PENDING.name();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructorFirstName() {
        return instructorFirstName;
    }

    public void setInstructorFirstName(String instructorFirstName) {
        this.instructorFirstName = instructorFirstName;
    }

    public String getInstructorLastName() {
        return instructorLastName;
    }

    public void setInstructorLastName(String instructorLastName) {
        this.instructorLastName = instructorLastName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public ArrayList<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(ArrayList<Assessment> assessments) {
        this.assessments = assessments;
    }

    public int getId() {
        return id;
    }

    public enum Statuses {
        PENDING,
        IN_PROGRESS,
        COMPLETE,
        DROPPED
    }
}
