package com.c196.exam.entities;

public class CourseNote {
    private int id;
    private int courseId;
    private String title;
    private String content;

    public CourseNote(String title, String content, int courseId){
        this.setTitle(title);
        this.setContent(content);
        this.setCourseId(courseId);
    }

    public CourseNote(int id, String title, String content, int courseId){
        this.setId(id);
        this.setTitle(title);
        this.setContent(content);
        this.setCourseId(courseId);
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
