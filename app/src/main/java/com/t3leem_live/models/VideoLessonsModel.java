package com.t3leem_live.models;

import java.io.Serializable;

public class VideoLessonsModel implements Serializable {
    private int id;
    private String title;
    private String title_en;
    private String document_type;
    private String stage_id;
    private String teacher_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String tutorial_id;
    private String file_doc;
    private String desc;
    private boolean status = false;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getDocument_type() {
        return document_type;
    }

    public String getStage_id() {
        return stage_id;
    }

    public String getTeacher_id() {
        return teacher_id;
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

    public String getTutorial_id() {
        return tutorial_id;
    }

    public String getFile_doc() {
        return file_doc;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
