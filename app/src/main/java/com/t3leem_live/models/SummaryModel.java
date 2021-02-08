package com.t3leem_live.models;

import java.io.Serializable;

public class SummaryModel implements Serializable {
    private int id;
    private String title;
    private String title_en;
    private double price;
    private String teacher_id;
    private String stage_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String desc;
    private String file_doc;
    private String teacher_name;
    private String icon;
    private UserModel.User teacher_fk;
    private StageClassModel stage_fk;
    private StageClassModel class_fk;
    private StageClassModel subject_fk;
    private SummaryFk summary_payment_fk;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public double getPrice() {
        return price;
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

    public String getDesc() {
        return desc;
    }

    public String getFile_doc() {
        return file_doc;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getIcon() {
        return icon;
    }

    public UserModel.User getTeacher_fk() {
        return teacher_fk;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }

    public StageClassModel getClass_fk() {
        return class_fk;
    }

    public StageClassModel getSubject_fk() {
        return subject_fk;
    }

    public SummaryFk getSummary_payment_fk() {
        return summary_payment_fk;
    }

    public void setSummary_payment_fk(SummaryFk summary_payment_fk) {
        this.summary_payment_fk = summary_payment_fk;
    }

    public static class SummaryFk implements Serializable{}
}
