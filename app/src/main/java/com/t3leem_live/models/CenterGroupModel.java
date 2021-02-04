package com.t3leem_live.models;

import java.io.Serializable;

public class CenterGroupModel implements Serializable {
    private int id;
    private String title;
    private String title_en;
    private int center_id;
    private int group_price;
    private int number_of_teachers;
    private String status;
    private String created_at;
    private String updated_at;
    private int teachers_count;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }


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

    public int getGroup_price() {
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

    public void setTitle(String title) {
        this.title = title;
    }
}
