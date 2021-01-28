package com.t3leem_live.models;

import java.io.Serializable;

public class CenterGroupModel implements Serializable {
    private int id;
    private String title;
    private String title_en;
    private int center_id;
    private double group_price;
    private int number_of_teachers;
    private String status;
    private String created_at;
    private String updated_at;
    private int teachers_count;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public int getCenter_id() {
        return center_id;
    }

    public double getGroup_price() {
        return group_price;
    }

    public int getNumber_of_teachers() {
        return number_of_teachers;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getTeachers_count() {
        return teachers_count;
    }
}
