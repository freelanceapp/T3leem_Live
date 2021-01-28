package com.t3leem_live.models;

import java.io.Serializable;

public class GroupOfTeacher implements Serializable {
    private int id;
    private int teacher_id;
    private int stage_id;

    private int class_id;
    private String department_id;
    private int subject_id;
    private String title;
    private String title_en;
    private String desc="";
    private int student_limit;
    private StageClassModel subject_fk;


    public GroupOfTeacher(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public int getStage_id() {
        return stage_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public int getSubject_id() {
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

    public int getStudent_limit() {
        return student_limit;
    }

    public StageClassModel getSubject_fk() {
        return subject_fk;
    }
}
