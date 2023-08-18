package com.c196.exam.entities;

public class Assessment {
    private int id;
    private String title;
    private String start;
    private String end;
    private boolean is_performance;
    private Integer courseId;

    public Assessment(int id, String title, String start, String end, boolean is_performance, int courseId) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.is_performance = is_performance;
        this.courseId = courseId;
    }

    public Assessment(String title, String start, String end, boolean is_performance, int courseId) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.is_performance = is_performance;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean is_performance() {
        return is_performance;
    }

    public void setIs_performance(boolean is_performance) {
        this.is_performance = is_performance;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
