package com.t3leem_live.models;

import java.io.Serializable;

public class TeachersInsideCenterModel implements Serializable {
    private int id;
    private int center_id;
    private int center_group_id;
    private int stage_id;
    private int teacher_id;
    private double teacher_price;
    private double center_discount_value;
    private double teacher_price_after_center_discountce;
    private double admin_price;
    private double teacher_price_after_all_discounts;
    private StageClassModel stage_fk;
    private CenterGroupModel center_group_fk;
    private TeacherModel teacher_fk;

    public int getId() {
        return id;
    }

    public int getCenter_id() {
        return center_id;
    }

    public int getCenter_group_id() {
        return center_group_id;
    }

    public int getStage_id() {
        return stage_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public double getTeacher_price() {
        return teacher_price;
    }

    public double getCenter_discount_value() {
        return center_discount_value;
    }

    public double getTeacher_price_after_center_discountce() {
        return teacher_price_after_center_discountce;
    }

    public double getAdmin_price() {
        return admin_price;
    }

    public double getTeacher_price_after_all_discounts() {
        return teacher_price_after_all_discounts;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }

    public CenterGroupModel getCenter_group_fk() {
        return center_group_fk;
    }

    public TeacherModel getTeacher_fk() {
        return teacher_fk;
    }

    public void setTeacher_fk(TeacherModel teacher_fk) {
        this.teacher_fk = teacher_fk;
    }
}