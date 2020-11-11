package com.t3leem_live.models;

import java.io.Serializable;

public class TeacherStudentsModel implements Serializable {
    private int id;
    private String student_id;
    private String teacher_group_id;
    private String subject_id;
    private TeacherModel student_fk;

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

    public TeacherModel getStudent_fk() {
        return student_fk;
    }
}
