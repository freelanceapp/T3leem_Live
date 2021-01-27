package com.t3leem_live.models;

import java.io.Serializable;

public class CenterGroupTeacherModel implements Serializable {
    private int id;
    private String center_id;
    private String center_group_id;
    private String stage_id;
    private String teacher_id;
    private String teacher_price;
    private String center_discount_value;
    private String teacher_price_after_center_discountce;
    private String admin_price;
    private String teacher_price_after_all_discounts;
    private String created_at;
    private String updated_at;
    private StageClassModel stage_fk;
    private TeacherModel teacher_fk;

    public int getId() {
        return id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public String getCenter_group_id() {
        return center_group_id;
    }

    public String getStage_id() {
        return stage_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getTeacher_price() {
        return teacher_price;
    }

    public String getCenter_discount_value() {
        return center_discount_value;
    }

    public String getTeacher_price_after_center_discountce() {
        return teacher_price_after_center_discountce;
    }

    public String getAdmin_price() {
        return admin_price;
    }

    public String getTeacher_price_after_all_discounts() {
        return teacher_price_after_all_discounts;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }

    public TeacherModel getTeacher_fk() {
        return teacher_fk;
    }
}
