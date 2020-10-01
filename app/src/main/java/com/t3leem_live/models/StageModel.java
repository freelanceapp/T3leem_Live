package com.t3leem_live.models;

import java.io.Serializable;

public class StageModel implements Serializable {
    private int id;
    private String title;
    private String title_en;
    private String desc;
    private String icon;
    private String background;

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
}
