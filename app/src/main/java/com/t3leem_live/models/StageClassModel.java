package com.t3leem_live.models;

import java.io.Serializable;

public class StageClassModel implements Serializable {
    private int id;
    private String title;
    private String title_en;
    private String desc;
    private String icon;
    private String background;
    private String file_doc;
    private String stage_id;
    private String class_id;
    private String subject_id;
    private String department_id;

    public int getId() {
        return id;
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

    public String getIcon() {
        return icon;
    }

    public String getBackground() {
        return background;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getStage_id() {
        return stage_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getFile_doc() {
        return file_doc;
    }


}
