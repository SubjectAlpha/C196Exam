package com.c196.exam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.entities.Term;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

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
                "'%11$s' VARCHAR, FOREIGN KEY('%7$s') REFERENCES '%12$s'('%13$s') ON DELETE RESTRICT ON UPDATE CASCADE)";

        String createCourseNoteQuery = "CREATE TABLE '%1$s' (" +
                "'%2$s' INTEGER PRIMARY KEY, " +
                "'%3$s' VARCHAR, '%4$s' VARCHAR, '%5$s' NUMBER, FOREIGN KEY('%5$s') REFERENCES '%6$s'('%7$s') ON DELETE CASCADE ON UPDATE CASCADE)";

        String createAssessmentQuery = "CREATE TABLE '%1$s' (" +
                "'%2$s' INTEGER PRIMARY KEY, " +
                "'%3$s' VARCHAR, '%4$s' VARCHAR, '%5$s' VARCHAR, '%6$s' BOOLEAN, '%7$s' INTEGER, FOREIGN KEY('%7$s') REFERENCES '%8$s'('%9$s') ON DELETE CASCADE ON UPDATE CASCADE)";

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
        db.execSQL("DROP TABLE IF EXISTS " + TermTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseNoteTable.NAME);

        onCreate(db);
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> termList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TermTable.NAME + ";", null);

        if(c.moveToFirst()){
            int index = 1;
            do {
                Term t = new Term(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                t.setCourses(this.getCourses(t.getId()));
                termList.add(t);
            } while(c.move(index));
        }
        c.close();
        return termList;
    }

    public Term getTerm(Integer id) {
        Term t = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TermTable.NAME + " WHERE " + TermTable._ID + " = ?;", new String[] {String.valueOf(id)});

        if(c.moveToFirst()){
            t = new Term(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
            t.setCourses(this.getCourses(t.getId()));
        }

        return t;
    }

    public Term getTerm(LocalDate date) {
        Instant selectedDateInstant = Instant.parse(date.toString() + "T00:00:00Z");
        Term selectedTerm = null;
        ArrayList<Term> terms = getTerms();

        for(int i = 0; i < terms.size(); i++) {
            Term t = terms.get(i);
            Instant start = Instant.parse(t.getStart());
            Instant end = Instant.parse(t.getEnd());

            Boolean isAfterStart = selectedDateInstant.isAfter(start);
            Boolean isBeforeEnd = selectedDateInstant.isBefore(end);
            if( isAfterStart && isBeforeEnd )
            {
                selectedTerm = t;
                break;
            }
        }

        return selectedTerm;
    }

    public Course getCourse(int id) {
        Course course = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CourseTable.NAME + " WHERE " +  CourseTable._ID + " = ?;", new String[] {String.valueOf(id)});

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
            ArrayList<CourseNote> notes = new ArrayList<>();

            Integer courseId = c.getInt(courseIdIdx);
            Cursor assessmentCursor = db.rawQuery("SELECT * FROM " + AssessmentTable.NAME + " WHERE " + AssessmentTable.COURSE_ID + " = ?;", new String[] {String.valueOf(courseId)});
            Cursor courseNoteCursor = db.rawQuery("SELECT * FROM " + CourseNoteTable.NAME + " WHERE " + CourseNoteTable.COURSE_ID + " = ?;", new String[] {String.valueOf(courseId)});

            if(assessmentCursor.moveToFirst()) {
                int idx = 1;
                int assessmentIdIdx = assessmentCursor.getColumnIndex(AssessmentTable._ID);
                int assessmentTitleIdx = assessmentCursor.getColumnIndex(AssessmentTable.TITLE);
                int assessmentStartIdx = assessmentCursor.getColumnIndex(AssessmentTable.START);
                int assessmentEndIdx = assessmentCursor.getColumnIndex(AssessmentTable.END);
                int performanceIdx = assessmentCursor.getColumnIndex(AssessmentTable.IS_PERFORMANCE);

                do {
                    int isPerformance = assessmentCursor.getInt(performanceIdx);
                    boolean isPerf = false;

                    if(isPerformance == 1){
                        isPerf = true;
                    }

                    assessments.add(new Assessment(
                            assessmentCursor.getInt(assessmentIdIdx),
                            assessmentCursor.getString(assessmentTitleIdx),
                            assessmentCursor.getString(assessmentStartIdx),
                            assessmentCursor.getString(assessmentEndIdx),
                            isPerf,
                            courseId
                    ));
                } while(assessmentCursor.move(idx));
                assessmentCursor.close();
            }

            if(courseNoteCursor.moveToFirst()) {
                int idx = 1;
                do {
                    int noteTitleIdx = courseNoteCursor.getColumnIndex(CourseNoteTable.TITLE);
                    int noteContentIdx = courseNoteCursor.getColumnIndex(CourseNoteTable.CONTENT);
                    int noteIdIdx = courseNoteCursor.getColumnIndex(CourseNoteTable._ID);

                    notes.add(new CourseNote(
                            courseNoteCursor.getInt(noteIdIdx),
                            courseNoteCursor.getString(noteTitleIdx),
                            courseNoteCursor.getString(noteContentIdx),
                            courseId
                    ));

                } while (courseNoteCursor.move(idx));
                courseNoteCursor.close();
            }

            try{
                course = new Course(
                    courseId,
                    c.getString(titleIdx),
                    c.getString(startIdx),
                    c.getString(endIdx),
                    c.getString(statusIdx),
                    c.getString(ciFNameIdx),
                    c.getString(ciLNameIdx),
                    c.getString(ciEmailIdx),
                    c.getString(ciPhoneIdx),
                    c.getInt(termIdIdx),
                    assessments,
                    notes
                );
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }


        }
        c.close();
        return course;
    }

    public ArrayList<Course> getCourses(int termId) {
        ArrayList<Course> courseList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor courseCursor = db.rawQuery("SELECT * FROM " + CourseTable.NAME + " WHERE " +  CourseTable.TERM_ID + " = ?;", new String[] {String.valueOf(termId)});

        if(courseCursor.moveToFirst()){
            int index = 1;
            do {

                int courseIdIdx = courseCursor.getColumnIndex(CourseTable._ID);
                int termIdIdx = courseCursor.getColumnIndex(CourseTable.TERM_ID);
                int titleIdx = courseCursor.getColumnIndex(CourseTable.TITLE);
                int startIdx = courseCursor.getColumnIndex(CourseTable.START);
                int endIdx = courseCursor.getColumnIndex(CourseTable.END);
                int statusIdx = courseCursor.getColumnIndex(CourseTable.STATUS);
                int ciFNameIdx = courseCursor.getColumnIndex(CourseTable.INSTRUCTOR_FIRST_NAME);
                int ciLNameIdx = courseCursor.getColumnIndex(CourseTable.INSTRUCTOR_LAST_NAME);
                int ciEmailIdx = courseCursor.getColumnIndex(CourseTable.INSTRUCTOR_EMAIL);
                int ciPhoneIdx = courseCursor.getColumnIndex(CourseTable.INSTRUCTOR_PHONE);

                ArrayList<Assessment> assessments = new ArrayList<>();
                ArrayList<CourseNote> notes = new ArrayList<>();

                int courseId = courseCursor.getInt(courseIdIdx);
                Cursor assessmentCursor = db.rawQuery("SELECT * FROM " + AssessmentTable.NAME + " WHERE " + AssessmentTable.COURSE_ID + " = ?;", new String[] {String.valueOf(courseId)});
                Cursor courseNoteCursor = db.rawQuery("SELECT * FROM " + CourseNoteTable.NAME + " WHERE " + CourseNoteTable.COURSE_ID + " = ?;", new String[] {String.valueOf(courseId)});

                if(assessmentCursor.moveToFirst()) {
                    int idx = 1;
                    int assessmentIdIdx = assessmentCursor.getColumnIndex(AssessmentTable._ID);
                    int assessmentTitleIdx = assessmentCursor.getColumnIndex(AssessmentTable.TITLE);
                    int assessmentStartIdx = assessmentCursor.getColumnIndex(AssessmentTable.START);
                    int assessmentEndIdx = assessmentCursor.getColumnIndex(AssessmentTable.END);
                    int performanceIdx = assessmentCursor.getColumnIndex(AssessmentTable.IS_PERFORMANCE);

                    do {
                        int isPerformance = assessmentCursor.getInt(performanceIdx);
                        boolean isPerf = false;

                        if(isPerformance == 1){
                            isPerf = true;
                        }

                        assessments.add(new Assessment(
                                assessmentCursor.getInt(assessmentIdIdx),
                                assessmentCursor.getString(assessmentTitleIdx),
                                assessmentCursor.getString(assessmentStartIdx),
                                assessmentCursor.getString(assessmentEndIdx),
                                isPerf,
                                courseId
                        ));
                    } while(assessmentCursor.move(idx));
                    assessmentCursor.close();
                }

                if(courseNoteCursor.moveToFirst()) {
                    int idx = 1;
                    do {
                        int noteTitleIdx = courseNoteCursor.getColumnIndex(CourseNoteTable.TITLE);
                        int noteContentIdx = courseNoteCursor.getColumnIndex(CourseNoteTable.CONTENT);
                        int noteIdIdx = courseNoteCursor.getColumnIndex(CourseNoteTable._ID);

                        notes.add(new CourseNote(
                                courseNoteCursor.getInt(noteIdIdx),
                                courseNoteCursor.getString(noteTitleIdx),
                                courseNoteCursor.getString(noteContentIdx),
                                courseId
                        ));
                    } while(courseNoteCursor.move(idx));
                    courseNoteCursor.close();
                }

                Course course = new Course(
                        courseId,
                        courseCursor.getString(titleIdx),
                        courseCursor.getString(startIdx),
                        courseCursor.getString(endIdx),
                        courseCursor.getString(statusIdx),
                        courseCursor.getString(ciFNameIdx),
                        courseCursor.getString(ciLNameIdx),
                        courseCursor.getString(ciEmailIdx),
                        courseCursor.getString(ciPhoneIdx),
                        courseCursor.getInt(termIdIdx),
                        assessments,
                        notes
                );
                courseList.add(course);
            } while(courseCursor.move(index));
        }
        courseCursor.close();
        return courseList;
    }

    public Assessment getAssessment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AssessmentTable.NAME + " WHERE " + AssessmentTable._ID + "=?;", new String[]{String.valueOf(id)});

        if(c.moveToFirst()){
            int assessmentIdIdx = c.getColumnIndex(AssessmentTable._ID);
            int assessmentTitleIdx = c.getColumnIndex(AssessmentTable.TITLE);
            int assessmentStartIdx = c.getColumnIndex(AssessmentTable.START);
            int assessmentEndIdx = c.getColumnIndex(AssessmentTable.END);
            int performanceIdx = c.getColumnIndex(AssessmentTable.IS_PERFORMANCE);
            int courseIdIdx = c.getColumnIndex(AssessmentTable.COURSE_ID);

            int isPerformance = c.getInt(performanceIdx);
            boolean isPerf = false;

            if(isPerformance == 1){
                isPerf = true;
            }

            return new Assessment(
                    c.getInt(assessmentIdIdx),
                    c.getString(assessmentTitleIdx),
                    c.getString(assessmentStartIdx),
                    c.getString(assessmentEndIdx),
                    isPerf, c.getInt(courseIdIdx));

        }

        return null;
    }

    public CourseNote getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CourseNoteTable.NAME + " WHERE " + CourseNoteTable._ID + "=?;", new String[]{String.valueOf(id)});
        if(c.moveToFirst()) {
            int noteIdIdx = c.getColumnIndex(CourseNoteTable._ID);
            int noteTitleIdx = c.getColumnIndex(CourseNoteTable.TITLE);
            int noteContextIdx = c.getColumnIndex(CourseNoteTable.CONTENT);
            int noteCourseIdIdx = c.getColumnIndex(CourseNoteTable.COURSE_ID);

            return new CourseNote(
                    c.getInt(noteIdIdx),
                    c.getString(noteTitleIdx),
                    c.getString(noteContextIdx),
                    c.getInt(noteCourseIdIdx)
            );
        }

        return null;
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

    public boolean updateTerm(Term t) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TermTable.TITLE_COLUMN, t.getTitle());
        cv.put(TermTable.START_COLUMN, t.getStart());
        cv.put(TermTable.END_COLUMN, t.getEnd());
        long result = db.update(TermTable.NAME, cv, TermTable._ID + "=?", new String[]{String.valueOf(t.getId())});
        if(result > 0){
            success = true;
        }
        return success;
    }

    public boolean deleteTerm(int id) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + CourseTable._ID + " FROM " + CourseTable.NAME + " WHERE " + CourseTable.TERM_ID + "=?", new String[]{String.valueOf(id)});
        int courseCount = c.getCount();

        if(courseCount <= 0) {
            long result = db.delete(TermTable.NAME, TermTable._ID + "=?", new String[]{String.valueOf(id)});
            if(result > 0){
                success = true;
            }
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

    public boolean updateCourse(Course c) {
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
        String x = String.valueOf(c.getId());
        long result = db.update(CourseTable.NAME,
                cv, CourseTable._ID + "=?",
                new String[]{x});

        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean deleteCourse(int id) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(CourseTable.NAME,
                CourseTable._ID + "=?",
                new String[]{String.valueOf(id)
        });

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
        cv.put(CourseNoteTable.COURSE_ID, note.getCourseId());
        cv.put(CourseNoteTable.CONTENT, note.getContent());

        long result = db.insert(CourseNoteTable.NAME, null, cv);
        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean addAssessment(Assessment assessment){
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AssessmentTable.TITLE, assessment.getTitle());
        cv.put(AssessmentTable.START, assessment.getStart());
        cv.put(AssessmentTable.END, assessment.getEnd());
        cv.put(AssessmentTable.IS_PERFORMANCE, assessment.is_performance());
        cv.put(AssessmentTable.COURSE_ID, assessment.getCourseId());

        long result = db.insert(AssessmentTable.NAME, null, cv);
        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean updateAssessment(Assessment assessment) {
        boolean success = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AssessmentTable.TITLE, assessment.getTitle());
        cv.put(AssessmentTable.START, assessment.getStart());
        cv.put(AssessmentTable.END, assessment.getEnd());
        cv.put(AssessmentTable.IS_PERFORMANCE, assessment.is_performance());

        long result = db.update(AssessmentTable.NAME,cv,
                AssessmentTable._ID + "=?",
                new String[]{String.valueOf(assessment.getId())
                });
        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean updateNote(CourseNote note) {
        boolean success = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CourseNoteTable.TITLE, note.getTitle());
        cv.put(CourseNoteTable.CONTENT, note.getContent());

        long result = db.update(CourseNoteTable.NAME, cv,
                CourseNoteTable._ID + "=?",
                new String[]{String.valueOf(note.getId())
                });
        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean deleteAssessment(int id) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(AssessmentTable.NAME,
                AssessmentTable._ID + "=?",
                new String[]{String.valueOf(id)
                });

        if(result > 0){
            success = true;
        }

        return success;
    }

    public boolean deleteNote(int id) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(CourseNoteTable.NAME,
                CourseNoteTable._ID + "=?",
                new String[]{String.valueOf(id)
                });

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
