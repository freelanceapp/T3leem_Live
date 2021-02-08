package com.t3leem_live.models;

import java.io.Serializable;

public class TeacherGroupModel implements Serializable {
    private int id;
    private String teacher_id;
    private String stage_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String title;
    private String title_en;
    private String desc;
    private String student_limit;
    private StageClassModel stage_fk;
    private StageClassModel class_fk;
    private StageClassModel department_fk;
    private TeacherModel teacher_fk;
    private StudentBook student_book;
    private LiveStreamFk live_stream_fk;

    public int getId() {
        return id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getStage_id() {
        return stage_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getDesc() {
        return desc;
    }

    public String getStudent_limit() {
        return student_limit;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }

    public StageClassModel getClass_fk() {
        return class_fk;
    }

    public StageClassModel getDepartment_fk() {
        return department_fk;
    }

    public TeacherModel getTeacher_fk() {
        return teacher_fk;
    }

    public StudentBook getStudent_book() {
        return student_book;
    }

    public void setStudent_book(StudentBook student_book) {
        this.student_book = student_book;
    }

    public void setLive_stream_fk(LiveStreamFk live_stream_fk) {
        this.live_stream_fk = live_stream_fk;
    }

    public LiveStreamFk getLive_stream_fk() {
        return live_stream_fk;
    }

    public static class StudentBook implements Serializable{
        private int id;
        private String student_id;
        private String teacher_group_id;
        private String subject_id;

        public int getId() {
            return id;
        }

        public String getStudent_id() {
            return student_id;
        }

        public String getTeacher_group_id() {
            return teacher_group_id;
        }

        public String getSubject_id() {
            return subject_id;
        }
    }
    
    public static class LiveStreamFk implements Serializable{
        private String start_url;

        public String getStart_url() {
            return start_url;
        }
    }
}
