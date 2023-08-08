package com.c196.exam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.entities.Term;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "C196.db";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTermsQuery = "CREATE TABLE '%1$s' ( '%2$s' INTEGER PRIMARY KEY, '%3$s' TEXT, '" +
                "%4$s' VARCHAR, '%5$s' VARCHAR)";

        String createCourseQuery = "CREATE TABLE '%1$s' ( " +
                "'%2$s' INTEGER PRIMARY KEY, '%3$s' TEXT, '%4$s' VARCHAR, '%5$s' VARCHAR, " +
                "'%6$s' TEXT, '%7$s' NUMBER, '%8$s' VARCHAR, '%9$s' VARCHAR, '%10$s' VARCHAR, " +
                "'%11$s' VARCHAR, FOREIGN KEY('%7$s') REFERENCES '%12$s'('%13$s'))";

        String createCourseNoteQuery = "CREATE TABLE '%1$s' (" +
                "'%2$s' INTEGER PRIMARY KEY, " +
                "'%3$s' VARCHAR, '%4$s' VARCHAR, '%5$s' NUMBER, FOREIGN KEY('%5$s') REFERENCES '%6$s'('%7$s'))";

        String createAssessmentQuery = "CREATE TABLE '%1$s' (" +
                "'%2$s' INTEGER PRIMARY KEY, " +
                "'%3$s' VARCHAR, '%4$s' VARCHAR, '%5$s' BOOLEAN, '%6$s' NUMBER, FOREIGN KEY('%6$s') REFERENCES '%7$s'('%8$s'))";

        try{
            createTermsQuery = String.format(createTermsQuery,
                TermTable.NAME,
                TermTable._ID,
                TermTable.TITLE_COLUMN,
                TermTable.START_COLUMN,
                TermTable.END_COLUMN
            );

            createCourseQuery = String.format(createCourseQuery,
                CourseTable.NAME,
                CourseTable._ID,
                CourseTable.TITLE,
                CourseTable.START,
                CourseTable.END,
                CourseTable.STATUS,
                CourseTable.TERM_ID,
                CourseTable.INSTRUCTOR_FIRST_NAME,
                CourseTable.INSTRUCTOR_LAST_NAME,
                CourseTable.INSTRUCTOR_EMAIL,
                CourseTable.INSTRUCTOR_PHONE,
                TermTable.NAME,
                TermTable._ID
            );

            createCourseNoteQuery = String.format(createCourseNoteQuery,
                CourseNoteTable.NAME,
                CourseNoteTable._ID,
                CourseNoteTable.TITLE,
                CourseNoteTable.CONTENT,
                CourseNoteTable.COURSE_ID,
                CourseTable.NAME,
                CourseTable._ID
            );

            createAssessmentQuery = String.format(createAssessmentQuery,
                AssessmentTable.NAME,
                AssessmentTable._ID,
                AssessmentTable.TITLE,
                AssessmentTable.START,
                AssessmentTable.END,
                AssessmentTable.IS_PERFORMANCE,
                AssessmentTable.COURSE_ID,
                CourseTable.NAME,
                CourseTable._ID
            );
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        db.execSQL(createTermsQuery);
        db.execSQL(createCourseQuery);
        db.execSQL(createCourseNoteQuery);
        db.execSQL(createAssessmentQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteTermsQuery = "DROP TABLE IF EXISTS " + TermTable.NAME;
        String deleteCourseQuery = "DROP TABLE IF EXISTS " + CourseTable.NAME;

        db.execSQL(deleteCourseQuery);
        db.execSQL(deleteTermsQuery);

        onCreate(db);
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> termList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TermTable.NAME + ";", null);

        if(c.moveToFirst()){
            int index = 0;
            do {
                Term t = new Term(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                termList.add(t);
                index++;
            } while(c.move(index));
        }
        c.close();
        return termList;
    }

    public Course getCourse(int id) {
        Course course = null;
        String query = "SELECT * FROM " + CourseTable.NAME + " WHERE " + CourseTable._ID +" = '%1$s';";
        query = String.format(query, id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){

            int courseIdIdx = c.getColumnIndex(CourseTable._ID);
            int termIdIdx = c.getColumnIndex(CourseTable.TERM_ID);
            int titleIdx = c.getColumnIndex(CourseTable.TITLE);
            int startIdx = c.getColumnIndex(CourseTable.START);
            int endIdx = c.getColumnIndex(CourseTable.END);
            int statusIdx = c.getColumnIndex(CourseTable.STATUS);
            int ciFNameIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_FIRST_NAME);
            int ciLNameIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_LAST_NAME);
            int ciEmailIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_EMAIL);
            int ciPhoneIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_PHONE);

            ArrayList<Assessment> assessments = new ArrayList<>();

            try{
                course = new Course(
                    c.getInt(courseIdIdx),
                    c.getString(titleIdx),
                    c.getString(startIdx),
                    c.getString(endIdx),
                    c.getString(statusIdx),
                    c.getString(ciFNameIdx),
                    c.getString(ciLNameIdx),
                    c.getString(ciEmailIdx),
                    c.getString(ciPhoneIdx),
                    c.getInt(termIdIdx),
                    assessments
                );
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }


        }
        c.close();
        return course;
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courseList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CourseTable.NAME + ";", null);

        if(c.moveToFirst()){
            int index = 0;
            do {

                int courseIdIdx = c.getColumnIndex(CourseTable._ID);
                int termIdIdx = c.getColumnIndex(CourseTable.TERM_ID);
                int titleIdx = c.getColumnIndex(CourseTable.TITLE);
                int startIdx = c.getColumnIndex(CourseTable.START);
                int endIdx = c.getColumnIndex(CourseTable.END);
                int statusIdx = c.getColumnIndex(CourseTable.STATUS);
                int ciFNameIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_FIRST_NAME);
                int ciLNameIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_LAST_NAME);
                int ciEmailIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_EMAIL);
                int ciPhoneIdx = c.getColumnIndex(CourseTable.INSTRUCTOR_PHONE);

                ArrayList<Assessment> assessments = new ArrayList<>();

                Course course = new Course(
                        c.getInt(courseIdIdx),
                        c.getString(titleIdx),
                        c.getString(startIdx),
                        c.getString(endIdx),
                        c.getString(statusIdx),
                        c.getString(ciFNameIdx),
                        c.getString(ciLNameIdx),
                        c.getString(ciEmailIdx),
                        c.getString(ciPhoneIdx),
                        c.getInt(termIdIdx),
                        assessments
                );
                courseList.add(course);
                index++;
            } while(c.move(index));
        }
        c.close();
        return courseList;
    }

    public boolean addTerm(Term t) {
        boolean success = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TermTable.TITLE_COLUMN, t.getTitle());
        cv.put(TermTable.START_COLUMN, t.getStart());
        cv.put(TermTable.END_COLUMN, t.getEnd());

        long result = db.insert(TermTable.NAME, null, cv);
        if(result > 0){
            success = true;
        }
        return success;
    }

    public boolean addCourse(Course c) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CourseTable.TITLE, c.getTitle());
        cv.put(CourseTable.START, c.getStart());
        cv.put(CourseTable.END, c.getEnd());
        cv.put(CourseTable.TERM_ID, c.getTermId());
        cv.put(CourseTable.STATUS, c.getStatus());
        cv.put(CourseTable.INSTRUCTOR_FIRST_NAME, c.getInstructorFirstName());
        cv.put(CourseTable.INSTRUCTOR_LAST_NAME, c.getInstructorLastName());
        cv.put(CourseTable.INSTRUCTOR_EMAIL, c.getInstructorEmail());
        cv.put(CourseTable.INSTRUCTOR_PHONE, c.getInstructorPhone());

        long result = db.insert(CourseTable.NAME, null, cv);
        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean addCourseNote(CourseNote note) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CourseNoteTable.TITLE, note.getTitle());

        long result = db.insert(CourseNoteTable.NAME, null, cv);
        if(result > 0){
            success = true;
        }

        return success;
    }

    private static class TermTable implements BaseColumns {
        public static final String NAME = "Terms";
        public static final String TITLE_COLUMN = "title";
        public static final String START_COLUMN = "start";
        public static final String END_COLUMN = "end";
    }

    private static class CourseTable implements BaseColumns {
        public static final String NAME = "Courses";
        public static final String TITLE = "title";
        public static final String START = "start";
        public static final String END = "end";
        public static final String TERM_ID = "termId";
        public static final String STATUS = "status";
        public static final String INSTRUCTOR_FIRST_NAME = "instructorFirstName";
        public static final String INSTRUCTOR_LAST_NAME = "instructorLastName";
        public static final String INSTRUCTOR_EMAIL = "instructorEmail";
        public static final String INSTRUCTOR_PHONE= "instructorPhone";
    }

    private static class CourseNoteTable implements BaseColumns {
        public static final String NAME = "CourseNotes";
        public static final String COURSE_ID = "courseId";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
    }

    private static class AssessmentTable implements BaseColumns {
        public static final String NAME = "Assessments";
        public static final String TITLE = "title";
        public static final String START = "start";
        public static final String END = "end";
        public static final String IS_PERFORMANCE = "is_performance";
        public static final String COURSE_ID = "courseId";
    }
}
